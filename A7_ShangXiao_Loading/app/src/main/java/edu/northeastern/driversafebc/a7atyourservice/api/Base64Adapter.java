package edu.northeastern.driversafebc.a7atyourservice.api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Base64;

public class Base64Adapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        out.value(Base64.getEncoder().encodeToString(value.getBytes()));
    }

    @Override
    public String read(JsonReader in) throws IOException {
        return new String(Base64.getDecoder().decode(in.nextString()));
    }
}
