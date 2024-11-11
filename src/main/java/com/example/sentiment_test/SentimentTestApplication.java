package com.example.sentiment_test;

import com.example.sentiment_test.dto.SentimentDto;
import com.example.sentiment_test.utils.PropertiesUtil;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
public class SentimentTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentimentTestApplication.class, args);
    }

}
