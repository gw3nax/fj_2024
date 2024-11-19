package ru.gw3nax.lesson15.jmh.config;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class RabbitMQClientConfig {
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String exchange;
    private final String queue;
    private final String routingKey;

    private Connection connection;

    public RabbitMQClientConfig(String host, int port, String username, String password, String exchange, String queue, String routingKey) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
    }

    public void initializeConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        this.connection = factory.newConnection();
    }

    public List<Channel> createProducers(int count) throws IOException {
        List<Channel> producers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT, true);
            channel.queueDeclare(queue, true, false, false, null);
            channel.queueBind(queue, exchange, routingKey);
            producers.add(channel);
        }
        return producers;
    }

    public List<Channel> createConsumers(int count) throws IOException {
        List<Channel> consumers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queue, true, false, false, null);
            channel.basicQos(1);
            consumers.add(channel);
        }
        return consumers;
    }

    public void closeConnection() throws IOException {
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }
}
