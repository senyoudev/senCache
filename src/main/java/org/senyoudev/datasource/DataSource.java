package org.senyoudev.datasource;

import org.senyoudev.exception.DataSourceException;

public sealed interface DataSource<K, V> permits FileDataSource {

  String getType();

  byte[] read(K key) throws DataSourceException;

  void clear() throws DataSourceException;

  void remove(K key) throws DataSourceException;

  void write(K key, byte[] value) throws DataSourceException;
}
