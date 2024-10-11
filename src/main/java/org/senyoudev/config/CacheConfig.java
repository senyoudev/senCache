package org.senyoudev.config;

import org.senyoudev.datasource.DataSource;
import org.senyoudev.eviction.EvictionPolicy;
import org.senyoudev.serialization.SerializationType;

/**
 * Cache configuration class
 *
 * @param maxSize maximum size of the cache
 * @param policyEviction eviction policy(LRU, MRU, LFU, etc)
 * @param dataSource data source type
 */
public record CacheConfig(
    double maxSize,
    EvictionPolicy policyEviction,
    DataSource dataSource,
    SerializationType serializationtype) {}
