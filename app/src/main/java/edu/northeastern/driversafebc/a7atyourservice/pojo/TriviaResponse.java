package edu.northeastern.driversafebc.a7atyourservice.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TriviaResponse {

    @SerializedName("response_code")
    public int responseCode;

    @SerializedName("results")
    public List<Trivia> results;

    @NonNull
    @Override
    public String toString() {
        return "TriviaResponse{" +
                "responseCode=" + responseCode +
                ", results=" + results +
                '}';
    }
}
