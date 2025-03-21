package com.github.danielrenes.sumo.metrics.controller;

import com.github.danielrenes.sumo.metrics.generated.Sumo.SignalState;
import com.github.danielrenes.sumo.metrics.generated.Sumo.Vehicle;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/metrics/traffic-lights")
public class TrafficLightMetricsController {
    @Autowired
    private StreamsBuilderFactoryBean streamsBuilderFactory;

    @GetMapping("/signal-states/count")
    public Map<SignalState, Long> getSignalStateCounts() {
        final var kafkaStreams = streamsBuilderFactory.getKafkaStreams();
        assert kafkaStreams != null;
        final var store = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        "signal-state-counts", QueryableStoreTypes.<SignalState, Long>keyValueStore()
                )
        );
        final var counts = new HashMap<SignalState, Long>();
        try (final var iterator = store.all()) {
            iterator.forEachRemaining(kv -> counts.put(kv.key, kv.value));
        }
        return counts;
    }

    @GetMapping("/lanes/vehicles")
    public Map<String, Long> getVehicleCountsByLane() {
        final var kafkaStreams = streamsBuilderFactory.getKafkaStreams();
        assert kafkaStreams != null;
        final var store = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        "vehicles-by-lane", QueryableStoreTypes.<String, Vehicle>keyValueStore()
                )
        );
        final var vehicleCountsByLane = new HashMap<String, Long>();
        try (final var iterator = store.all()) {
            iterator.forEachRemaining(kv -> {
                vehicleCountsByLane.computeIfAbsent(kv.key, key -> 0L);
                vehicleCountsByLane.put(kv.key, vehicleCountsByLane.get(kv.key) + 1);
            });
        }
        return vehicleCountsByLane;
    }
}
