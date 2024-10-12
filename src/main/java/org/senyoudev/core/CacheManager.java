package org.senyoudev.core;

import java.util.Objects;
import org.senyoudev.concurrency.ConcurrencyController;
import org.senyoudev.config.CacheConfig;
import org.senyoudev.datasource.DataSource;
import org.senyoudev.serialization.SerializationFactory;
import org.senyoudev.serialization.SerializationType;
import org.senyoudev.serialization.Serializer;

public class CacheManager<K, V> {

  private final CacheStore<K, V> cacheStore;
  private final DataSource dataSource;
  private final ConcurrencyController concurrencyController;
  private final Serializer serializer;
  private final CacheConfig cacheConfig;

  public CacheManager(CacheConfig cacheConfig) {
    this.cacheConfig = Objects.requireNonNull(cacheConfig, "CacheConfig cannot be null");
    this.cacheStore = new CacheStore<>(cacheConfig.policyEviction(), (int) cacheConfig.maxSize());
    this.dataSource = cacheConfig.dataSource();
    this.serializer = instantiateSerializer(cacheConfig.serializationtype());
    this.concurrencyController = new ConcurrencyController();
  }

  private Serializer instantiateSerializer(SerializationType serializationType) {
    try {
      Class<? extends Serializer> serializerClass =
          SerializationFactory.getSerializer(serializationType);
      if (serializerClass == null) {
        throw new IllegalArgumentException("Serializer not found for format: " + serializationType);
      }
      return serializerClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Error instantiating serializer", e);
    }
  }
}
