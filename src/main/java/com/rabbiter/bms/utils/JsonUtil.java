package com.rabbiter.bms.utils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// Allow POJO to have fields that do not exist in the JSON string.
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);// Allow for annotations
    }

    public static <T>T parseObject(InputStream inputStream, Class<T> tClass)  {
        Reader reader = new InputStreamReader(inputStream);
        try {
            return objectMapper.readValue(reader, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T>T parseObject(String json,Class<T> tClass){
        try {
            return objectMapper.readValue(json,tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T>List<T> parseList(String json,Class<T> tClass) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, tClass);
        try {
            List<T> list  = objectMapper.readValue(json, javaType);
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <K,V>Map<K,V> parseMap(String json, Class<K> tclass1, Class<V> tclass2) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(HashMap.class,tclass1,tclass2);
        try {
            Map<K,V> map  = objectMapper.readValue(json, javaType);
            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonString(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}