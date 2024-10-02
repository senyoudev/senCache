package org.senyoudev.config;


import org.senyoudev.datasource.DataSource;
import org.senyoudev.eviction.EvictionPolicy;

public record CacheConfig(
        double maxSize,
        EvictionPolicy policyEviction,
        DataSource dataSourceUrl
) {
}
