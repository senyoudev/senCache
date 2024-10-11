package org.senyoudev.concurrency;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * This class is responsible for managing the concurrency control of the cache.
 * By concurrency control, we mean the strategy to control the access to the cache when multiple threads are accessing the cache.
 * It uses a ReadWriteLock to control the access to the cache.
 * It allows multiple threads to hold the read lock at the same time as long as no thread holds the write lock.
 * It allows only one thread to hold the write lock at a time.
 * It is also reentrant, which means that a thread can acquire the same lock multiple times.
 */
public class ConcurrencyController {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true); // Fairness policy which means that the longest waiting thread gets the lock

    /**
     * This method is used to read from the cache.
     * It acquires the read lock before reading from the cache and releases the read lock after reading from the cache.
     * @param readOperation It's a supplier which is a functional interface that takes no arguments and returns a result.
     *                      which means we pass to it a function that will get executed and return a result.
     */
    public <T> T read(Supplier<T> readOperation) {
        rwLock.readLock().lock();
        try {
            return readOperation.get();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * This method is used to write to the cache.
     * It acquires the write lock before writing to the cache and releases the write lock after writing to the cache.
     * @param writeOperation It's a runnable which is a functional interface that takes no arguments and returns no result.
     *                       which means we pass to it a function that will get executed without returning a result.
     */
    public void write(Runnable writeOperation) {
        rwLock.writeLock().lock();
        try {
            writeOperation.run();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * This method is used to write to the cache and return a result.
     * It acquires the write lock before writing to the cache and releases the write lock after writing to the cache.
     * @param writeOperation It's a supplier which is a functional interface that takes no arguments and returns a result.
     *                      which means we pass to it a function that will get executed and return a result.
     */
    public <T> T writeWithResult(Supplier<T> writeOperation) {
        rwLock.writeLock().lock();
        try {
            return writeOperation.get();
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
