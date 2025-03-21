package com.github.danielrenes.sumo.metrics.streams;

import com.github.danielrenes.sumo.metrics.generated.Sumo.Speed;
import com.github.danielrenes.sumo.metrics.generated.Sumo.Vehicle;
import com.github.danielrenes.sumo.metrics.serialization.AverageSerde;
import com.github.danielrenes.sumo.metrics.serialization.ProtobufMessageSerde;
import com.github.danielrenes.sumo.metrics.utils.Average;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VehicleMetricsStreams {
    private static final double KPH_TO_MS = 3.6;
    private static final double MPH_TO_MS = 2.237;

    @Autowired
    public void calcMinSpeeds(StreamsBuilder builder) {
        builder.stream("vehicles", Consumed.with(Serdes.String(), new ProtobufMessageSerde<>(Vehicle.parser())))
                .map((key, vehicle) -> KeyValue.pair(key, calcSpeedInMetersPerSecond(vehicle.getSpeed())))
                .filter((key, speed) -> speed != null)
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .aggregate(
                        () -> 0.0,
                        (key, value, aggregate) -> Math.min(value, aggregate),
                        Materialized.<String, Double, KeyValueStore<Bytes, byte[]>>as("min-speeds-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Double())
                );
    }

    @Autowired
    public void calcMaxSpeeds(StreamsBuilder builder) {
        builder.stream("vehicles", Consumed.with(Serdes.String(), new ProtobufMessageSerde<>(Vehicle.parser())))
                .map((key, vehicle) -> KeyValue.pair(key, calcSpeedInMetersPerSecond(vehicle.getSpeed())))
                .filter((key, speed) -> speed != null)
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .aggregate(
                        () -> 0.0,
                        (key, value, aggregate) -> Math.max(value, aggregate),
                        Materialized.<String, Double, KeyValueStore<Bytes, byte[]>>as("max-speeds-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Double())
                );
    }

    @Autowired
    public void calcAvgSpeeds(StreamsBuilder builder) {
        builder.stream("vehicles", Consumed.with(Serdes.String(), new ProtobufMessageSerde<>(Vehicle.parser())))
                .map((key, vehicle) -> KeyValue.pair(key, calcSpeedInMetersPerSecond(vehicle.getSpeed())))
                .filter((key, speed) -> speed != null)
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .aggregate(
                        () -> new Average(0.0, 0L),
                        (key, value, aggregate) -> new Average(aggregate.sum() + value, aggregate.count() + 1),
                        Materialized.with(Serdes.String(), new AverageSerde())
                )
                .toStream()
                .mapValues(Average::get)
                .toTable(
                        Materialized.<String, Double, KeyValueStore<Bytes, byte[]>>as("avg-speeds-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Double())
                );
    }

    private static Double calcSpeedInMetersPerSecond(Speed speed) {
        return switch (speed.getUnit()) {
            case UNRECOGNIZED -> null;
            case METERS_PER_SECOND -> speed.getAmount();
            case KILOMETERS_PER_HOUR -> speed.getAmount() / KPH_TO_MS;
            case MILES_PER_HOUR -> speed.getAmount() / MPH_TO_MS;
        };
    }
}
