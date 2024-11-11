package com.example.sentiment_test.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    // Use the parameter to get corresponding property settings
    public static Properties getProperties(String name) {
        InputStream resourceAsStream=PropertiesUtil.class.getClassLoader().getResourceAsStream(name+".properties");
        Properties props=new Properties();
        try {
            props.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                resourceAsStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props;

    }
}