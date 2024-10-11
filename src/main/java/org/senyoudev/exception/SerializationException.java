package org.senyoudev.exception;

/** Exception thrown when an error occurs during serialization. */
public class SerializationException extends Exception {
  /**
   * Constructs a new SerializationException with the specified detail message.
   *
   * @param message the detail message
   */
  public SerializationException(String message) {
    super(message);
  }

  /**
   * Constructs a new SerializationException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public SerializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
