package org.senyoudev.util;

import org.senyoudev.exception.SerializationException;

/**
 * Interface for serializing and deserializing objects.
 * @param <T> the type of objects that can be serialized and deserialized
 */
public sealed interface Serializer<T> permits JsonSerializer {
    byte[] serialize(T object) throws SerializationException;
    T deserialize(byte[] data) throws SerializationException;
}
