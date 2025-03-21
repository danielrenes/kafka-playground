package com.github.danielrenes.sumo.metrics.serialization;

import com.google.protobuf.Message;
import org.apache.kafka.common.serialization.Serializer;

public final class ProtobufMessageSerializer<T extends Message> implements Serializer<T> {
    @Override
    public byte[] serialize(String topic, T data) {
        return data.toByteArray();
    }
}
