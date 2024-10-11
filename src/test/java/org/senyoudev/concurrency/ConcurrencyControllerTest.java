package org.senyoudev.concurrency;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConcurrencyControllerTest {
  private ConcurrencyController concurrencyController;
  private AtomicInteger sharedResource;

  @BeforeEach
  void setUp() {
    concurrencyController = new ConcurrencyController();
    sharedResource = new AtomicInteger(0);
  }

  @Test
  void shouldAllowMultipleReadsconcurrently() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    Runnable readTask =
        () -> {
          Integer result = concurrencyController.read(sharedResource::get);
          assertEquals(0, result);
        };

    for (int i = 0; i < 3; i++) {
      executorService.submit(readTask);
    }

    executorService.shutdown();
    assertTrue(executorService.awaitTermination(1, TimeUnit.SECONDS));
  }

  @Test
  void shouldAllowOnlyOneWriteAtATime() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    AtomicInteger writeCount = new AtomicInteger(0);

    Runnable writeTask =
        () ->
            concurrencyController.write(
                () -> {
                  concurrencyController.write(writeCount::incrementAndGet);
                  try {
                    TimeUnit.MILLISECONDS.sleep(200);
                  } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                  } finally {
                    writeCount.decrementAndGet();
                  }
                });

    executorService.submit(writeTask);
    executorService.submit(writeTask);

    executorService.shutdown();
    assertTrue(executorService.awaitTermination(1, TimeUnit.SECONDS));
    assertEquals(0, writeCount.get(), "Only one write should be allowed at a time");
  }

  @Test
  void shouldAllowWriteWhileReading() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    Runnable readTask =
        () -> {
          concurrencyController.read(
              () -> {
                try {
                  TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
                return sharedResource.get();
              });
        };

    Runnable writeTask =
        () ->
            concurrencyController.write(
                () -> {
                  concurrencyController.write(() -> sharedResource.set(30));
                });

    executorService.submit(readTask);
    executorService.submit(writeTask);

    executorService.shutdown();
    assertTrue(executorService.awaitTermination(1, TimeUnit.SECONDS));
    assertEquals(30, sharedResource.get());
  }

  @Test
  void shouldAllowWriteWithResult() {
    Integer result =
        concurrencyController.writeWithResult(
            () -> {
              sharedResource.set(10);
              return sharedResource.get();
            });
    assertEquals(10, result);
  }
}
