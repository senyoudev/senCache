package org.senyoudev.serialization;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senyoudev.exception.SerializationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class JsonSerializerTest {
    private JsonSerializer<TestObject> serializer;

    @BeforeEach
    void setUp() {
        serializer = new JsonSerializer<>(TestObject.class);
    }

    @Test
    void shouldSerializeValidObject() {
        TestObject testObject = new TestObject("Alice", LocalDate.of(1990, 1, 1));
        assertDoesNotThrow(() -> {
            byte[] serialized = serializer.serialize(testObject);
            assertNotNull(serialized);
            assertTrue(serialized.length > 0);
        });
    }

    @Test
    void shouldThrowExceptionWhenSerializingNullObject() {
        assertThrows(SerializationException.class, () -> {
            serializer.serialize(null);
        }, "SerializationException was expected when serializing a null object.");
    }

    @Test
    void shouldDeserializeValidData() {
        TestObject testObject = new TestObject("Alice", LocalDate.of(1990, 1, 1));
        byte[] serialized = assertDoesNotThrow(() -> serializer.serialize(testObject));
        assertDoesNotThrow(() -> {
            TestObject deserialized = serializer.deserialize(serialized);
            assertNotNull(deserialized);
            assertEquals(testObject.getName(), deserialized.getName());
            assertEquals(testObject.getDateOfBirth(), deserialized.getDateOfBirth());
        });
    }

    @Test
    void shouldThrowExceptionWhenDeserializingInvalidData() {
        byte[] invalidData = "invalid data".getBytes();
        assertThrows(SerializationException.class, () -> {
            serializer.deserialize(invalidData);
        }, "SerializationException was expected when deserializing invalid data.");
    }


    static class TestObject {
        private String name;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateOfBirth;

        public TestObject() {
        }

        public TestObject(String name, LocalDate dateOfBirth) {
            this.name = name;
            this.dateOfBirth = dateOfBirth;
        }

        public String getName() {
            return name;
        }

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }
    }
}