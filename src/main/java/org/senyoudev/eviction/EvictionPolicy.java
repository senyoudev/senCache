package org.senyoudev.eviction;

public sealed interface EvictionPolicy<K> permits LRUEvictionPolicy {
  void onAccess(K key);

  void onPut(K key);

  K evict();

  void onRemove(K key);

  void clear();
}
