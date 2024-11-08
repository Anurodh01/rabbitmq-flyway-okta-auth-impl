package com.amount.orders.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Log {
    public static String requestResponseData(Object object){
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try{
            return writer.writeValueAsString(object);
        }catch(JsonProcessingException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
}
