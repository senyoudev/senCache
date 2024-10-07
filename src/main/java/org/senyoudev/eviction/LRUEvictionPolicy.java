package org.senyoudev.eviction;

import java.util.LinkedList;
import java.util.Map;

/**
 * This class is responsible for managing the eviction policy of the cache.
 * By Eviction policy, we mean the strategy to evict the elements from the cache when the cache is full.
 * @param <K> the type of the key
 */
public final class LRUEvictionPolicy<K> implements EvictionPolicy<K> {
    private final int maxSize;
    private final Map<K,Node<K>> cache;
    private final LinkedList<Node<K>> list;

    public LRUEvictionPolicy(int maxSize, Map<K, Node<K>> cache, LinkedList<Node<K>> list) {
        this.maxSize = maxSize;
        this.cache = cache;
        this.list = list;
    }


    /**
     * This method is called when an element is accessed in the cache.
     * If the element is not present in the cache, it is added to the cache and the front of the list.
     * If the element is already present in the cache, it is moved to the front of the list.
     * @param key the key of the element
     */
    @Override
    public void onAccess(K key) {
        Node<K> node = cache.get(key);
        if(node == null) {
            node = new Node<>(key);
            cache.put(key,node);
        } else {
            list.remove(node);
        }
        list.addFirst(node);
    }

    /**
     * This method is called when a new element is added to the cache.
     * @param key the key of the element
     */
    @Override
    public void onPut(K key) {
        onAccess(key);
    }

    /**
     * This method is called when an element is evicted from the cache.
     * It removes the least recently used element from the cache and the list.
     * @return the key of the evicted element
     */
    @Override
    public K evict() {
        if(list.isEmpty()) {
            return null;
        }
        Node<K> node = list.removeLast();
        cache.remove(node.key);
        return node.key;
    }

    /**
     * This method is called when an element is removed from the cache.
     * It removes the element from the cache and the list.
     * @param key the key of the element
     */
    @Override
    public void onRemove(K key) {
        Node<K> node = cache.remove(key);
        if(node != null) {
            list.remove(node);
        }
    }

    /**
     * This class represents a node in the linked list.
     * @param <T> the type of the key
     */
    public static class Node<T> {
        private T key;

        Node(T key) {
            this.key = key;
        }

        public T getKey() {
            return key;
        }


    }
}
