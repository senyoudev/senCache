package org.senyoudev.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.senyoudev.exception.DataSourceException;

// This keeps reading from the file everytime we want to do an operation
// TODO : Fix this issue above
public non-sealed class FileDataSource<K, V> implements DataSource<K, V> {

  private final Path filePath;
  private final ObjectMapper objectMapper;

  public FileDataSource(Path filePath) {
    this.filePath = filePath;
    this.objectMapper = new ObjectMapper();
    initializeFile();
  }

  @Override
  public String getType() {
    return "FILE";
  }

  private void initializeFile() {
    if (!Files.exists(filePath)) {
      try {
        Files.createFile(filePath);
      } catch (IOException e) {
        throw new RuntimeException("Error creating file", e);
      }
    }
  }

  @Override
  public byte[] read(K key) throws DataSourceException {
    try {
      Map<K, byte[]> dataMap = readMap();
      return dataMap.get(key);
    } catch (IOException e) {
      throw new DataSourceException("Error reading data source", e);
    }
  }

  @Override
  public void clear() throws DataSourceException {
    try {
      writeMap(new HashMap<>());
    } catch (IOException e) {
      throw new DataSourceException("Error clearing data source", e);
    }
  }

  @Override
  public void write(K key, byte[] value) throws DataSourceException {
    try {
      Map<K, byte[]> dataMap = readMap();
      dataMap.put(key, value);
      writeMap(dataMap);
    } catch (IOException e) {
      throw new DataSourceException("Error Writing to data source", e);
    }
  }

  @Override
  public void remove(K key) throws DataSourceException {
    try {
      Map<K, byte[]> dataMap = readMap();
      dataMap.remove(key);
      writeMap(dataMap);
    } catch (IOException e) {
      throw new DataSourceException("Error removing key from data source", e);
    }
  }

  private void writeMap(Map<K, byte[]> map) throws IOException {
    try (OutputStream os = new FileOutputStream(filePath.toFile())) {
      objectMapper.writeValue(os, map);
    }
  }

  private Map<K, byte[]> readMap() throws IOException {
    try (InputStream is = new FileInputStream(filePath.toFile())) {
      return objectMapper.readValue(is, Map.class);
    }
  }
}
