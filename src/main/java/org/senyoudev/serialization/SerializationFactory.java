package org.senyoudev.serialization;

import java.util.HashMap;
import java.util.Map;

/** Factory class to create serialization objects. */
public class SerializationFactory {

  // Map of serializers keyed by format name (case-insensitive)
  private static final Map<SerializationType, Class<? extends Serializer>> registeredSerializers =
      new HashMap<>();

  static {
    registeredSerializers.put(SerializationType.JSON, JsonSerializer.class);
  }

  /**
   * Get the serializer for the given format.
   *
   * @param format the format of the serializer (e.g, SerializationType.JSON)
   * @return the serializer class or null if not found
   */
  public static Class<? extends Serializer> getSerializer(SerializationType format) {
    return registeredSerializers.get(format);
  }

  /**
   * Register a new serializer.
   *
   * @param format the format of the serializer (e.g, SerializationType.JSON)
   * @param serializer the serializer class
   */
  public static void registerSerializer(
      SerializationType format, Class<? extends Serializer> serializer) {
    if (serializer == null) {
      throw new IllegalArgumentException("Serializer cannot be null");
    }
    if (!checkValidSerializer(serializer)) {
      throw new IllegalArgumentException("Invalid serializer class");
    }
    registeredSerializers.put(format, serializer);
  }

  private static boolean checkValidSerializer(Class<? extends Serializer> serializer) {
    return serializer.isAssignableFrom(Serializer.class);
  }
}
