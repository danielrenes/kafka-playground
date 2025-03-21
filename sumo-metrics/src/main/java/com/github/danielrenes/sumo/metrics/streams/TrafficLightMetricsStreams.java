package com.github.danielrenes.sumo.metrics.streams;

import com.github.danielrenes.sumo.metrics.generated.Sumo.*;
import com.github.danielrenes.sumo.metrics.serialization.ProtobufEnumSerde;
import com.github.danielrenes.sumo.metrics.serialization.ProtobufMessageSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

@Component
public class TrafficLightMetricsStreams {
    @Autowired
    public void countBySignalState(StreamsBuilder builder) {
        builder.stream("traffic-lights", Consumed.with(Serdes.String(), new ProtobufMessageSerde<>(TrafficLight.parser())))
                .flatMapValues(TrafficLightMetricsStreams::getCurrentPhases)
                .groupBy(
                        (key, signalPhase) -> signalPhase.getState(),
                        Grouped.with(
                                new ProtobufEnumSerde<>(SignalState::forNumber),
                                new ProtobufMessageSerde<>(SignalPhase.parser())
                        )
                )
                .count(
                        Materialized.<SignalState, Long, KeyValueStore<Bytes, byte[]>>as("signal-state-counts")
                                .withKeySerde(new ProtobufEnumSerde<>(SignalState::forNumber))
                                .withValueSerde(Serdes.Long())
                );
    }

    @Autowired
    public void getVehiclesByLane(StreamsBuilder builder) {
        final var vehicleLanes = builder
                .stream("vehicles", Consumed.with(Serdes.String(), new ProtobufMessageSerde<>(Vehicle.parser())))
                .map((key, vehicle) -> KeyValue.pair(vehicle.getRelation().getLaneId(), vehicle));
        final var intersectionLanes = builder
                .stream("traffic-lights", Consumed.with(Serdes.String(), new ProtobufMessageSerde<>(TrafficLight.parser())))
                .flatMap(
                        (key, trafficLight) -> getControlledLanes(trafficLight).stream()
                                .map(lane -> KeyValue.pair(lane, trafficLight)).toList()
                );
        vehicleLanes
                .join(
                        intersectionLanes,
                        (vehicle, trafficLight) -> vehicle,
                        JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(60)),
                        StreamJoined.with(
                                Serdes.String(),
                                new ProtobufMessageSerde<>(Vehicle.parser()),
                                new ProtobufMessageSerde<>(TrafficLight.parser())
                        )
                )
                .toTable(
                        Materialized.<String, Vehicle, KeyValueStore<Bytes, byte[]>>as("vehicles-by-lane")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new ProtobufMessageSerde<>(Vehicle.parser()))
                );
    }

    private static List<SignalPhase> getCurrentPhases(TrafficLight trafficLight) {
        return trafficLight.getSignalGroupsList().stream()
                .map(SignalGroup::getPhase)
                .toList();
    }

    private static List<String> getControlledLanes(TrafficLight trafficLight) {
        return trafficLight.getSignalGroupsList().stream()
                .flatMap(signalGroup -> signalGroup.getConnectionList().stream())
                .flatMap(connection -> Stream.of(connection.getIngressLaneId(), connection.getEgressLaneId()))
                .toList();
    }
}
