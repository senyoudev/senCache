package org.senyoudev.core;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.senyoudev.eviction.EvictionPolicy;

/** This class is our in-memory cache store. */
public class CacheStore<K, V> {
  private final Map<K, V> store;
  private final EvictionPolicy<K> evictionPolicy;
  private final int maxSize;

  public CacheStore(EvictionPolicy<K> evictionPolicy, int maxSize) {
    if (maxSize <= 0) {
      throw new IllegalArgumentException("Max Size must be positive");
    }
    this.store = new ConcurrentHashMap<>(maxSize);
    this.evictionPolicy = evictionPolicy;
    this.maxSize = maxSize;
  }

  public V get(K key) {
    Objects.requireNonNull(key, "Key cannot be null");
    V value = store.get(key);
    if (value != null) {
      evictionPolicy.onAccess(key);
    }
    return value;
  }

  public void put(K key, V value) {
    Objects.requireNonNull(key, "Key cannot be null");
    Objects.requireNonNull(value, "Value cannot be null");
    if (store.size() >= maxSize && !store.containsKey(key)) {
      evictionPolicy.evict();
    }
    store.put(key, value);
    evictionPolicy.onPut(key);
  }

  public void remove(K key) {
    Objects.requireNonNull(key, "Key cannot be null");
    store.remove(key);
    evictionPolicy.onRemove(key);
  }

  public void clear() {
    store.clear();
    evictionPolicy.clear();
  }

  public int size() {
    return store.size();
  }

  public boolean containsKey(K key) {
    Objects.requireNonNull(key, "Key cannot be null");
    return store.containsKey(key);
  }

  public void evict() {
    K keyToEvict = evictionPolicy.evict();
    if (keyToEvict != null) {
      store.remove(keyToEvict);
    }
  }
}
