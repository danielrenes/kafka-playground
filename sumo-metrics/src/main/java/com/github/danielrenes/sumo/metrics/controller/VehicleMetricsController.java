package com.github.danielrenes.sumo.metrics.controller;

import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/metrics/vehicles")
public class VehicleMetricsController {
    @Autowired
    private StreamsBuilderFactoryBean streamsBuilderFactory;

    @GetMapping("/speed/min")
    public Map<String, Double> getMinSpeeds() {
        final var kafkaStreams = streamsBuilderFactory.getKafkaStreams();
        assert kafkaStreams != null;
        final var store = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        "min-speeds-store", QueryableStoreTypes.<String, Double>keyValueStore()
                )
        );
        final var minSpeeds = new HashMap<String, Double>();
        try (final var iterator = store.all()) {
            iterator.forEachRemaining(kv -> minSpeeds.put(kv.key, kv.value));
        }
        return minSpeeds;
    }

    @GetMapping("/speed/max")
    public Map<String, Double> getMaxSpeeds() {
        final var kafkaStreams = streamsBuilderFactory.getKafkaStreams();
        assert kafkaStreams != null;
        final var store = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        "max-speeds-store", QueryableStoreTypes.<String, Double>keyValueStore()
                )
        );
        final var maxSpeeds = new HashMap<String, Double>();
        try (final var iterator = store.all()) {
            iterator.forEachRemaining(kv -> maxSpeeds.put(kv.key, kv.value));
        }
        return maxSpeeds;
    }

    @GetMapping("/speed/avg")
    public Map<String, Double> getAvgSpeeds() {
        final var kafkaStreams = streamsBuilderFactory.getKafkaStreams();
        assert kafkaStreams != null;
        final var store = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        "avg-speeds-store", QueryableStoreTypes.<String, Double>keyValueStore()
                )
        );
        final var avgSpeeds = new HashMap<String, Double>();
        try (final var iterator = store.all()) {
            iterator.forEachRemaining(kv -> avgSpeeds.put(kv.key, kv.value));
        }
        return avgSpeeds;
    }

    @GetMapping("/{id}/speed/min")
    public double getMinSpeed(@PathVariable String id) {
        final var kafkaStreams = streamsBuilderFactory.getKafkaStreams();
        assert kafkaStreams != null;
        final var store = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        "min-speeds-store", QueryableStoreTypes.<String, Double>keyValueStore()
                )
        );
        return store.get(id);
    }

    @GetMapping("/{id}/speed/max")
    public double getMaxSpeed(@PathVariable String id) {
        final var kafkaStreams = streamsBuilderFactory.getKafkaStreams();
        assert kafkaStreams != null;
        final var store = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        "max-speeds-store", QueryableStoreTypes.<String, Double>keyValueStore()
                )
        );
        return store.get(id);
    }

    @GetMapping("/{id}/speed/avg")
    public double getAvgSpeed(@PathVariable String id) {
        final var kafkaStreams = streamsBuilderFactory.getKafkaStreams();
        assert kafkaStreams != null;
        final var store = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        "avg-speeds-store", QueryableStoreTypes.<String, Double>keyValueStore()
                )
        );
        return store.get(id);
    }
}
