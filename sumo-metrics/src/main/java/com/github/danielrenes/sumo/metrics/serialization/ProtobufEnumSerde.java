package com.github.danielrenes.sumo.metrics.serialization;

import com.google.protobuf.ProtocolMessageEnum;
import org.apache.kafka.common.serialization.Serdes;

import java.util.function.IntFunction;

public final class ProtobufEnumSerde<E extends ProtocolMessageEnum> extends Serdes.WrapperSerde<E> {
    public ProtobufEnumSerde(IntFunction<E> factory) {
        super(new ProtobufEnumSerializer<>(), new ProtobufEnumDeserializer<>(factory));
    }
}
