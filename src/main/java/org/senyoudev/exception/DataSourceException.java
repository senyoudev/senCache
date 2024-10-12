package org.senyoudev.exception;

public class DataSourceException extends Exception {

  public DataSourceException(String message) {
    super(message);
  }

  public DataSourceException(String message, Throwable cause) {
    super(message, cause);
  }
}
