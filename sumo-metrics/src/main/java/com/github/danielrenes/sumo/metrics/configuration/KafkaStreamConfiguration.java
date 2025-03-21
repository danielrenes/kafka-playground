package com.github.danielrenes.sumo.metrics.configuration;

import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.List;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamConfiguration {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private List<String> bootstrapServers;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration streamsConfiguration() {
        final var properties = new HashMap<String, Object>() {{
            put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            put(StreamsConfig.APPLICATION_ID_CONFIG, "sumo-metrics-app");
        }};
        return new KafkaStreamsConfiguration(properties);
    }
}
