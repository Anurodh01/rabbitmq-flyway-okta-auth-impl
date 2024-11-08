package com.amount.customers.utils;

import com.amount.customers.exception.TypeMisMatchException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
public class CustomIntegerSerializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        try {
            return jsonParser.readValuesAs(Integer.class).next();
        } catch (Exception e) {
            throw new TypeMisMatchException(jsonParser.currentName() + " must be a number.");
        }
    }
}
