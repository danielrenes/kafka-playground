package com.github.danielrenes.sumo.metrics.serialization;

import com.github.danielrenes.sumo.metrics.utils.Average;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;

public final class AverageSerializer implements Serializer<Average> {
    @Override
    public byte[] serialize(String topic, Average data) {
        final var buffer = ByteBuffer.allocate(16);
        buffer.putDouble(data.sum());
        buffer.putLong(data.count());
        return buffer.array();
    }
}
