package org.senyoudev.core;

import java.util.Objects;
import org.senyoudev.concurrency.ConcurrencyController;
import org.senyoudev.config.CacheConfig;
import org.senyoudev.datasource.DataSource;
import org.senyoudev.exception.CacheException;
import org.senyoudev.exception.DataSourceException;
import org.senyoudev.exception.SerializationException;
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

  /** This method is used to instantiate the serializer based on the serialization type. */
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

  public CacheConfig getCacheConfig() {
    return cacheConfig;
  }

  public int size() {
    return concurrencyController.read(cacheStore::size);
  }

  public boolean containsKey(K key) {
    return concurrencyController.read(() -> cacheStore.containsKey(key));
  }

  public void clear() {
    concurrencyController.write(
        () -> {
          cacheStore.clear();
          clearDataSource();
        });
  }

  public void remove(K key) {
    concurrencyController.write(
        () -> {
          cacheStore.remove(key);
          try {
            removeFromDataSource(key);
          } catch (CacheException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public V get(K key) {
    return concurrencyController.read(
        () -> {
          V value = cacheStore.get(key);
          if (value == null) {
            value =
                concurrencyController.read(
                    () -> {
                      try {
                        V fetchedData = fetchFromDataSource(key);
                        if (fetchedData != null) {
                          cacheStore.put(key, fetchedData);
                        }
                        return fetchedData;
                      } catch (CacheException e) {
                        throw new RuntimeException(e);
                      }
                    });
          }
          return value;
        });
  }

  public void put(K key, V value) {
    concurrencyController.write(
        () -> {
          if (value != cacheStore.get(key)) {
            cacheStore.put(key, value);
            try {
              writeToDataSource(key, value);
            } catch (CacheException e) {
              throw new RuntimeException(e);
            }
          }
        });
  }

  private void clearDataSource() {
    try {
      dataSource.clear();
    } catch (Exception e) {
      throw new RuntimeException("Error clearing data source", e);
    }
  }

  private void writeToDataSource(K key, V value) throws CacheException {
    try {
      dataSource.write(key, serializeValue(value));
    } catch (DataSourceException e) {
      throw new CacheException("Error writing to data source", e);
    } catch (SerializationException e) {
      throw new CacheException("Error serializing object with key: " + key, e);
    }
  }

  private V fetchFromDataSource(K key) throws CacheException {
    try {
      byte[] serializedData = dataSource.read(key);
      return serializedData != null ? deserializeValue(serializedData) : null;
    } catch (Exception e) {
      throw new CacheException("Error fetching value from data source", e);
    }
  }

  private void removeFromDataSource(K key) throws CacheException {
    try {
      dataSource.remove(key);
    } catch (Exception e) {
      throw new CacheException("Error Removing from data source", e);
    }
  }

  private byte[] serializeValue(V value) throws SerializationException {
    return serializer.serialize(value);
  }

  private V deserializeValue(byte[] data) throws SerializationException {
    return (V) serializer.deserialize(data);
  }
}
