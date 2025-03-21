package com.github.danielrenes.sumo.metrics.serialization;

import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import org.apache.kafka.common.serialization.Serdes;

public final class ProtobufMessageSerde<T extends Message> extends Serdes.WrapperSerde<T> {
    public ProtobufMessageSerde(Parser<T> parser) {
        super(new ProtobufMessageSerializer<>(), new ProtobufMessageDeserializer<>(parser));
    }
}
