package org.senyoudev.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.senyoudev.exception.SerializationException;


/**
 * This class is used to serialize objects to JSON format. It uses the Jackson library to perform the serialization.
 * @param <T>
 */
public final class JsonSerializer<T> implements Serializer<T> {
    private final ObjectMapper objectMapper;
    private final Class<T> type;

    public JsonSerializer(Class<T> type) {
        this.type = type;
        this.objectMapper = JsonMapper
                        .builder()
                        .addModule(new JavaTimeModule())
                        .build();
    }

    /**
     * Serializes the given object to a JSON byte array.
     * @param object the object to serialize
     * @return the serialized object as a byte array
     * @throws SerializationException if an error occurs during serialization
     */
    @Override
    public byte[] serialize(T object) throws SerializationException {
        if(object == null) {
            throw new SerializationException("Cannot serialize a null object.");
        }
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
        throw new SerializationException("Error serializing object of type: " + object.getClass().getName(), e);
        } catch (Exception e) {
            throw new SerializationException("Unexpected error during serialization.", e);
        }
    }

    /**
     * Deserializes the given byte array to an object of type T.
     * @param data the byte array to deserialize
     * @return the deserialized object
     * @throws SerializationException if an error occurs during deserialization
     */
    @Override
    public T deserialize(byte[] data) throws SerializationException {
        try {
            return objectMapper.readValue(data, type);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing object of type: " + type.getName(), e);
        } catch (Exception e) {
            throw new SerializationException("Unexpected error during deserialization.", e);
        }
    }
}
