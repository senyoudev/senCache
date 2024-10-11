package org.senyoudev.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senyoudev.eviction.EvictionPolicy;
import org.senyoudev.eviction.LRUEvictionPolicy;

class CacheStoreTest {

  private CacheStore<String, String> cacheStore;
  private EvictionPolicy<String> evictionPolicy;

  @BeforeEach
  void setUp() {
    evictionPolicy = new LRUEvictionPolicy<>(3);
    cacheStore = new CacheStore<>(evictionPolicy, 3);
  }

  @Test
  void shouldPutAndGetElementFromCache() {
    cacheStore.put("key1", "value1");
    String value = cacheStore.get("key1");
    assertEquals("value1", value);
  }

  @Test
  void shouldEvictElementWhenCacheIsFull() {
    cacheStore.put("key1", "value1");
    cacheStore.put("key2", "value2");
    cacheStore.put("key3", "value3");
    cacheStore.put("key4", "value4");
    assertNull(cacheStore.get("key1"), "Expected one of the earlier keys to be evicted");
  }

  @Test
  void shouldRemoveElementFromCache() {
    cacheStore.put("key1", "value1");
    cacheStore.remove("key1");
    assertNull(cacheStore.get("key1"), "Expected key to be removed from cache");
  }

  @Test
  void shouldClearCache() {
    cacheStore.put("key1", "value1");
    cacheStore.put("key2", "value2");
    cacheStore.clear();
    assertEquals(0, cacheStore.size(), "Expected cache to be empty");
  }

  @Test
  void shouldThrowExceptionWhenKeyIsNull() {
    assertThrows(NullPointerException.class, () -> cacheStore.put(null, "value"));
    assertThrows(NullPointerException.class, () -> cacheStore.get(null));
    assertThrows(NullPointerException.class, () -> cacheStore.remove(null));
    assertThrows(NullPointerException.class, () -> cacheStore.containsKey(null));
  }

  @Test
  void shouldEvictElementWhenManuallyEvicted() {
    cacheStore.put("key1", "value1");

    cacheStore.evict();
    assertFalse(cacheStore.containsKey("key1"));
  }
}
