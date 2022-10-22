package edu.northeastern.driversafebc.a7atyourservice.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TriviaResponse {

    @SerializedName("response_code")
    private int responseCode;

    @SerializedName("results")
    private List<Trivia> results;

    public int getResponseCode() {
        return responseCode;
    }

    public List<Trivia> getResults() {
        return results;
    }
}
