package ru.gw3nax.lesson15.jmh.benchmark;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.openjdk.jmh.annotations.*;
import ru.gw3nax.lesson15.jmh.config.KafkaClientConfig;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class KafkaBenchmark {

    private final String bootstrapServers = "localhost:9092";
    private final String topic = "test-topic";

    private List<Producer<String, String>> producers;
    private List<Consumer<String, String>> consumers;

    @Param({"1:1", "3:1", "1:3", "3:3", "10:10"})
    private String scenario;

    @Setup(Level.Trial)
    public void setUp() {
        KafkaClientConfig config = new KafkaClientConfig(bootstrapServers, topic);

        String[] parts = scenario.split(":");
        int producerCount = Integer.parseInt(parts[0]);
        int consumerCount = Integer.parseInt(parts[1]);

        producers = config.createProducers(producerCount);
        consumers = config.createConsumers(consumerCount);
    }

    @Benchmark
    public void testProducerConsumer() {
        for (Producer<String, String> producer : producers) {
            producer.send(new ProducerRecord<>(topic, "key", "SIMPLE TEST"), (metadata, exception) -> {
            });
        }

        for (Consumer<String, String> consumer : consumers) {
            consumer.poll(Duration.ofMillis(100));
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        producers.forEach(Producer::close);
        consumers.forEach(Consumer::close);
    }
}