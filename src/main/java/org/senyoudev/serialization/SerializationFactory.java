package org.senyoudev.serialization;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class to create serialization objects.
 */
public class SerializationFactory {
    // Map of serializers keyed by format name (case-insensitive)
    private static final Map<String, Class<? extends Serializer>> registeredSerializers = new HashMap<>();

    static {
        registeredSerializers.put("json",JsonSerializer.class);
    }

    /**
     * Get the serializer for the given format.
     *
     * @param format the format of the serializer (e.g, "json", "csv")
     * @return the serializer class or null if not found
     */
    public static Class<? extends Serializer> getSerializer(String format) {
        return registeredSerializers.get(format.toLowerCase());
    }

    /**
     * Register a new serializer.
     *
     * @param format the format of the serializer (e.g, "json", "csv")
     * @param serializer the serializer class
     */
    public static void registerSerializer(String format, Class<? extends Serializer> serializer) {
        registeredSerializers.put(format.toLowerCase(), serializer);
    }
}
