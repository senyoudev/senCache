package org.senyoudev.serialization;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SerializationFactoryTest {

  @Test
  void shouldReturnJsonSerializerWhenFormatIsJson() {
    Class<? extends Serializer> serializerClass =
        SerializationFactory.getSerializer(SerializationType.JSON);
    assertNotNull(serializerClass);
    assertEquals(JsonSerializer.class, serializerClass);
  }

  @Test
  void shouldNotReturnSerializerWhenFormatIsNotRegistered() {
    Class<? extends Serializer> serializerClass =
        SerializationFactory.getSerializer(SerializationType.CSV);
    assertNull(serializerClass);
  }

  @Test
  void shouldReturnNullWhenfromatIsUnknown() {
    Class<? extends Serializer> serializerClass = SerializationFactory.getSerializer(null);
    assertNull(serializerClass);
  }

  @Test
  void shouldNotRegisterSerializerWhenClassIsNull() {
    assertThrows(
        IllegalArgumentException.class,
        () -> SerializationFactory.registerSerializer(SerializationType.CSV, null));
  }
}
