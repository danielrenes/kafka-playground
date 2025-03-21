package com.github.danielrenes.sumo.metrics.serialization;

import com.google.protobuf.ProtocolMessageEnum;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.Serializer;

public final class ProtobufEnumSerializer<E extends ProtocolMessageEnum> implements Serializer<E> {
    private final Serializer<Integer> delegate = new IntegerSerializer();

    @Override
    public byte[] serialize(String topic, E data) {
        return delegate.serialize(topic, data.getNumber());
    }
}
