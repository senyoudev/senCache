package org.senyoudev.serialization;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SerializationFactoryTest {

  @Test
  void shouldReturnJsonSerializerWhenFormatIsJson() {
    Class<? extends Serializer> serializerClass = SerializationFactory.getSerializer("json");
    assertNotNull(serializerClass);
    assertEquals(JsonSerializer.class, serializerClass);
  }

  @Test
  void shouldNotReturnSerializerWhenFormatIsNotRegistered() {
    Class<? extends Serializer> serializerClass = SerializationFactory.getSerializer("csv");
    assertNull(serializerClass);
  }

  @Test
  void shouldReturnNullWhenfromatIsUnknown() {
    Class<? extends Serializer> serializerClass = SerializationFactory.getSerializer("unkown");
    assertNull(serializerClass);
  }

  @Test
  void shouldNotRegisterSerializerWhenSerializerInterfaceDoesNotPermitsIt() {
    Class<? extends Serializer> nonPermittedSerializer = Mockito.mock(Class.class);
    assertThrows(
        IllegalArgumentException.class,
        () -> SerializationFactory.registerSerializer("csv", nonPermittedSerializer));
  }
}
