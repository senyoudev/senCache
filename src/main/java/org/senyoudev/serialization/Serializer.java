package org.senyoudev.serialization;

import org.senyoudev.exception.SerializationException;
import org.senyoudev.serialization.JsonSerializer;

/**
 * Interface for serializing and deserializing objects.
 * @param <T> the type of objects that can be serialized and deserialized
 */
public sealed interface Serializer<T> permits JsonSerializer {
    byte[] serialize(T object) throws SerializationException;
    T deserialize(byte[] data) throws SerializationException;
}
