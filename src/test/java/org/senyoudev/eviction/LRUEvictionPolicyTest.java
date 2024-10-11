package org.senyoudev.eviction;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LRUEvictionPolicyTest {
  private LRUEvictionPolicy<Integer> lruEvictionPolicy;
  private Map<Integer, LRUEvictionPolicy.Node<Integer>> cache;
  private LinkedList<LRUEvictionPolicy.Node<Integer>> list;

  @BeforeEach
  void setUp() {
    cache = new HashMap<>();
    list = new LinkedList<>();
    lruEvictionPolicy = new LRUEvictionPolicy<>(3, cache, list);
  }

  @Test
  void shouldAddElementToCacheAndListWhenAccessed() {
    lruEvictionPolicy.onAccess(1);
    assertEquals(1, cache.size());
    assertEquals(1, list.size());
    assertEquals(1, list.getFirst().getKey());
  }

  @Test
  void shouldMoveElementtoFrontOnAccess() {
    lruEvictionPolicy.onAccess(1);
    lruEvictionPolicy.onAccess(2);
    lruEvictionPolicy.onAccess(3);
    lruEvictionPolicy.onAccess(1);
    assertEquals(3, cache.size());
    assertEquals(3, list.size());
    assertEquals(1, list.getFirst().getKey());
  }

  @Test
  void shouldEvictLeastUsedElement() {
    lruEvictionPolicy.onAccess(1);
    lruEvictionPolicy.onAccess(2);
    lruEvictionPolicy.onAccess(3);
    lruEvictionPolicy.onAccess(4);
    Integer evicted = lruEvictionPolicy.evict();
    assertEquals(3, cache.size());
    assertEquals(3, list.size());
    assertEquals(1, evicted);
    assertFalse(cache.containsKey(1));
  }

  @Test
  void shouldNotEvictWhenCacheIsEmpty() {
    Integer evicted = lruEvictionPolicy.evict();
    assertNull(evicted);
  }

  @Test
  void shouldRemoveElementFromCacheAndListOnRemove() {
    lruEvictionPolicy.onAccess(1);
    lruEvictionPolicy.onRemove(1);
    assertEquals(0, cache.size());
    assertEquals(0, list.size());
  }
}
