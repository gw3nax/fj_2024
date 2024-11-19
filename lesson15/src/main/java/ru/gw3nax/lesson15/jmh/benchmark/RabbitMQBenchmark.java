package ru.gw3nax.lesson15.jmh.benchmark;

import com.rabbitmq.client.Channel;
import org.openjdk.jmh.annotations.*;
import ru.gw3nax.lesson15.jmh.config.RabbitMQClientConfig;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class RabbitMQBenchmark {
    private RabbitMQClientConfig rabbitConfig;
    private List<Channel> producers;
    private List<Channel> consumers;

    @Param({"1:1", "3:1", "1:3", "3:3", "10:10"})
    private String scenario;

    @Setup(Level.Trial)
    public void setUp() throws IOException, TimeoutException {
        rabbitConfig = new RabbitMQClientConfig(
                "localhost",
                5673,
                "admin",
                "admin",
                "test-exchange",
                "test-queue",
                "test-key"
        );

        rabbitConfig.initializeConnection();

        String[] parts = scenario.split(":");
        int producerCount = Integer.parseInt(parts[0]);
        int consumerCount = Integer.parseInt(parts[1]);

        producers = rabbitConfig.createProducers(producerCount);
        consumers = rabbitConfig.createConsumers(consumerCount);
    }

    @Benchmark
    public void testProducerConsumer() throws IOException {
        String message = "SIMPLE TEST";
        for (Channel producer : producers) {
            producer.basicPublish("test-exchange", "test-key", null, message.getBytes());
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() throws IOException, TimeoutException {
        if (producers != null) {
            for (Channel producer : producers) {
                if (producer.isOpen()) {
                    producer.close();
                }
            }
        }
        if (consumers != null) {
            for (Channel consumer : consumers) {
                if (consumer.isOpen()) {
                    consumer.close();
                }
            }
        }
        rabbitConfig.closeConnection();
    }
}
