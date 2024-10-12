package org.senyoudev.datasource;

public sealed interface DataSource<K, V> permits FileDataSource {

  String getType();

  byte[] read(K key) throws DataSourceException;

  void clear();

  void remove();

  void write(K key, V value);
}
