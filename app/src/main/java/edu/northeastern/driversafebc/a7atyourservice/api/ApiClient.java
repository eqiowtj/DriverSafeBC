package edu.northeastern.driversafebc.a7atyourservice.api;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://opentdb.com";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(
                            GsonConverterFactory
                                    .create(new GsonBuilder()
                                            .registerTypeAdapter(String.class, new Base64Adapter())
                                            .create())
                    ).build();
        }
        return retrofit;
    }

}
