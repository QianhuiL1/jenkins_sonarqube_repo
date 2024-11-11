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
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

@CrossOrigin(origins = "*")
@RestController
public class SentimentController {

    @PostMapping("/sentiment")
    public SentimentDto sentimentAnalysis(@RequestBody SentenceDto sentenceDto) {
        System.out.println("Receive message: " + sentenceDto.getSentence());
        String key = send_to_kafka(sentenceDto);
        System.out.println("Message key: " + key);
        return consume_from_kafka(key);
    }

    @GetMapping("/testHealth")
    public void testHealth() {
    }

    public String send_to_kafka(SentenceDto sentenceDto) {
        // Get the property configuration for the producer
        Producer<String, SentenceDto> producer = new KafkaProducer<>(PropertiesUtil.getProperties("producer"));
        final Random rnd = new Random();
        String key = Integer.toString(rnd.nextInt());
        //Send messages
        producer.send(new ProducerRecord<String, SentenceDto>("sa-request-topic", key, sentenceDto));
        System.out.println("Send message: " + sentenceDto.getSentence());

        producer.close();
        return key;
    }

    public SentimentDto consume_from_kafka(String key) {
        final Properties props = PropertiesUtil.getProperties("consumer");

        final String topic = "sa-response-topic";

        try (final Consumer<String, SentimentDto> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));
            while (true) {
                ConsumerRecords<String, SentimentDto> records = consumer.poll(100);
                for (ConsumerRecord<String, SentimentDto> record : records) {
                    //打印结果
                    System.out.println("Consume message: " + record.value().getSentence() + record.value().getPolarity());
                    System.out.println(record.key());
                    System.out.println(key);
                    if (record.key().equals(key)){
                        System.out.println("sent");
                        return record.value();
                    }
                }
            }
        }
    }
}


