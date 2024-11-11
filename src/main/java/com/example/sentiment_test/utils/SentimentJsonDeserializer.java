package com.example.sentiment_test.utils;

import com.alibaba.fastjson.JSON;
import com.example.sentiment_test.dto.SentimentDto;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class SentimentJsonDeserializer implements Deserializer<SentimentDto> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public SentimentDto deserialize(String topic, byte[] data) {
        return JSON.parseObject(data, SentimentDto.class);
    }

    @Override
    public void close() {

    }

}
