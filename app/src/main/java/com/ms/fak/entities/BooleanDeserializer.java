package com.ms.fak.entities;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BooleanDeserializer implements JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            if ("true".equals(je.getAsString())) {
                return true;
            }
            if ("false".equals(je.getAsString())) {
                return false;
            }
            return je.getAsInt() == 1;
    }   
}
