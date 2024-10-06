package org.senyoudev.eviction;

import java.util.LinkedList;
import java.util.Map;

public final class LRUEvictionPolicy<K,V> implements EvictionPolicy<K,V> {
    private final int maxSize;
    private final Map<K,Node<K>> cache;
    private final LinkedList<Node<K>> list;

    public LRUEvictionPolicy(int maxSize, Map<K, Node<K>> cache, LinkedList<Node<K>> list) {
        this.maxSize = maxSize;
        this.cache = cache;
        this.list = list;
    }


    @Override
    public void onAccess(K key) {

    }

    @Override
    public void onPut(K key) {

    }

    @Override
    public K evict() {
        return null;
    }

    private static class Node<T> {
        T key;

        Node(T key) {
            this.key = key;
        }
    }
}
