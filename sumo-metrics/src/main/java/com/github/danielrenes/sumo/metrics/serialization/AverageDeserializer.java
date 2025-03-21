package com.github.danielrenes.sumo.metrics.serialization;

import com.github.danielrenes.sumo.metrics.utils.Average;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;

public final class AverageDeserializer implements Deserializer<Average> {
    @Override
    public Average deserialize(String topic, byte[] data) {
        final var buffer = ByteBuffer.wrap(data);
        final var sum = buffer.getDouble();
        final var count = buffer.getLong();
        return new Average(sum, count);
    }
}
