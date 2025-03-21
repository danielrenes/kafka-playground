package com.github.danielrenes.sumo.metrics.serialization;

import com.github.danielrenes.sumo.metrics.utils.Average;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public final class AverageSerde implements Serde<Average> {
    @Override
    public Serializer<Average> serializer() {
        return new AverageSerializer();
    }

    @Override
    public Deserializer<Average> deserializer() {
        return new AverageDeserializer();
    }
}
