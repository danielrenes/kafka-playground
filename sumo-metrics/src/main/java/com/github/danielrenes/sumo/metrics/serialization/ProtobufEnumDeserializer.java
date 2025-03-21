package com.github.danielrenes.sumo.metrics.serialization;

import com.google.protobuf.ProtocolMessageEnum;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import java.util.Objects;
import java.util.function.IntFunction;

public final class ProtobufEnumDeserializer<E extends ProtocolMessageEnum> implements Deserializer<E> {
    private final Deserializer<Integer> delegate = new IntegerDeserializer();
    private final IntFunction<E> factory;

    public ProtobufEnumDeserializer(IntFunction<E> factory) {
        this.factory = Objects.requireNonNull(factory);
    }

    @Override
    public E deserialize(String topic, byte[] data) {
        return factory.apply(delegate.deserialize(topic, data));
    }
}
