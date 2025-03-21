package com.github.danielrenes.sumo.metrics.serialization;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Objects;

public final class ProtobufMessageDeserializer<T extends Message> implements Deserializer<T> {
    private final Parser<T> parser;

    public ProtobufMessageDeserializer(Parser<T> parser) {
        this.parser = Objects.requireNonNull(parser);
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return parser.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
