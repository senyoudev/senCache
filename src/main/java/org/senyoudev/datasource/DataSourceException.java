package org.senyoudev.datasource;

public class DataSourceException extends Exception {

  public DataSourceException(String message) {
    super(message);
  }

  public DataSourceException(String message, Throwable cause) {
    super(message, cause);
  }
}
