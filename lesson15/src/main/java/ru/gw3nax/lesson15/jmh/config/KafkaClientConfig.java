package ru.gw3nax.lesson15.jmh.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KafkaClientConfig {

    private final String bootstrapServers;
    private final String topic;

    public KafkaClientConfig(String bootstrapServers, String topic) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
    }

    public List<Producer<String, String>> createProducers(int count) {
        List<Producer<String, String>> producers = new ArrayList<>();
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        for (int i = 0; i < count; i++) {
            producers.add(new KafkaProducer<>(producerProps));
        }

        return producers;
    }

    public List<Consumer<String, String>> createConsumers(int count) {
        List<Consumer<String, String>> consumers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Properties consumerProps = new Properties();
            consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group" + i);
            consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            Consumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
            consumer.subscribe(Collections.singletonList(topic));
            consumers.add(consumer);
        }

        return consumers;
    }
}