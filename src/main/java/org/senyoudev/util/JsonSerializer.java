package org.senyoudev.util;

import org.senyoudev.exception.SerializationException;

/**
 * This class is used to serialize objects to JSON format. It uses the Jackson library to perform the serialization.
 * @param <T>
 */
public class JsonSerializer<T> implements Serializer<T> {

    @Override
    public byte[] serialize(T object) throws SerializationException {
        return new byte[0];
    }

    @Override
    public T deserialize(byte[] data) throws SerializationException {
        return null;
    }
}
