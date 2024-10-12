package org.senyoudev.config;

import org.senyoudev.datasource.DataSource;
import org.senyoudev.eviction.EvictionPolicy;
import org.senyoudev.serialization.SerializationType;

/** Cache configuration class */
public class CacheConfig {

  private double maxSize;
  private EvictionPolicy policyEviction;
  private DataSource dataSource;
  private SerializationType serializationType;

  private CacheConfig() {}

  public CacheConfig addMaxSize(double maxSize) {
    this.maxSize = maxSize;
    return this;
  }

  public CacheConfig addPolicyEviction(EvictionPolicy policyEviction) {
    this.policyEviction = policyEviction;
    return this;
  }

  public CacheConfig addDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    return this;
  }

  public CacheConfig addSerializationType(SerializationType serializationType) {
    this.serializationType = serializationType;
    return this;
  }

  public CacheConfig build() {
    return this;
  }

  public static CacheConfig create() {
    return new CacheConfig();
  }

  public double maxSize() {
    return maxSize;
  }

  public EvictionPolicy policyEviction() {
    return policyEviction;
  }

  public DataSource dataSource() {
    return dataSource;
  }

  public SerializationType serializationType() {
    return serializationType;
  }
}
