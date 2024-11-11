package com.example.sentiment_test;

import com.example.sentiment_test.dto.SentenceDto;
import com.example.sentiment_test.dto.SentimentDto;
import com.example.sentiment_test.utils.PropertiesUtil;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.common.config.SaslConfigs.SASL_MECHANISM;

public class ConsumerController {

    public static void main(final String[] args) {

        final Properties props = PropertiesUtil.getProperties("consumer");

        final String topic = "sa_response_topic";

        try (final Consumer<String, SentimentDto> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));
            while (true) {
                ConsumerRecords<String, SentimentDto> records = consumer.poll(100);
                for (ConsumerRecord<String, SentimentDto> record : records) {
                    //打印结果
                    System.out.println("Consume message: " + record.value().getSentence() + record.value().getPolarity());

                }
            }
        }

    }
}