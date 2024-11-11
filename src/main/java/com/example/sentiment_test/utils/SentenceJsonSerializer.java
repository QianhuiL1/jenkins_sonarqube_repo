package com.example.sentiment_test.utils;

import com.alibaba.fastjson.JSON;
import com.example.sentiment_test.dto.SentenceDto;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class SentenceJsonSerializer implements Serializer<SentenceDto> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, SentenceDto data) {
        return JSON.toJSONBytes(data);
    }

    @Override
    public void close() {

    }

}
