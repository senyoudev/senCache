package org.senyoudev.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.senyoudev.config.CacheConfig;
import org.senyoudev.datasource.DataSource;
import org.senyoudev.datasource.FileDataSource;
import org.senyoudev.eviction.EvictionPolicy;
import org.senyoudev.eviction.LRUEvictionPolicy;
import org.senyoudev.serialization.JsonSerializer;
import org.senyoudev.serialization.SerializationType;
import org.senyoudev.serialization.Serializer;

class CacheManagerTest {

  private CacheManager<String, String> cacheManager;
  private CacheConfig cacheConfig;
  private DataSource dataSource;
  private EvictionPolicy<String> evictionPolicy;
  private Serializer<String> serializer;

  @BeforeEach
  void setUp() {
    evictionPolicy = Mockito.mock(LRUEvictionPolicy.class);
    dataSource = Mockito.mock(FileDataSource.class);
    serializer = Mockito.mock(JsonSerializer.class);
    cacheConfig = Mockito.mock(CacheConfig.class);

    when(cacheConfig.policyEviction()).thenReturn(evictionPolicy);
    when(cacheConfig.maxSize()).thenReturn(Double.valueOf(3));
    when(cacheConfig.dataSource()).thenReturn(dataSource);
    when(cacheConfig.serializationType()).thenReturn(SerializationType.JSON);

    cacheManager = new CacheManager<>(cacheConfig);
  }

  @Test
  void shouldPutAndGetElementFromCache() {
    cacheManager.put("key1", "value1");
    String value = cacheManager.get("key1");
    assertEquals(value, "value1");
  }

  @Test
  void shouldReturnNullIfElementNotPresentInCache() {
    String value = cacheManager.get("key2");
    assertNull(value);
  }

  @Test
  void shouldRemoveElementFromCache() {
    cacheManager.put("key1", "value1");
    cacheManager.remove("key1");
    assertFalse(cacheManager.containsKey("key1"));
  }

  @Test
  void shouldClearCache() {
    cacheManager.put("key1", "value1");
    cacheManager.put("key2", "value2");
    cacheManager.clear();
    assertEquals(0, cacheManager.size());
  }
}
