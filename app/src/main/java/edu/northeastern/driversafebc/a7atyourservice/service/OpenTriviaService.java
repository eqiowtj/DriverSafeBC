package edu.northeastern.driversafebc.a7atyourservice.service;

import java.util.function.Consumer;

import edu.northeastern.driversafebc.a7atyourservice.api.ApiClient;
import edu.northeastern.driversafebc.a7atyourservice.api.CallbackHandler;
import edu.northeastern.driversafebc.a7atyourservice.api.OpenTriviaApi;
import edu.northeastern.driversafebc.a7atyourservice.pojo.TriviaResponse;
import retrofit2.Retrofit;

public class OpenTriviaService {

    private final OpenTriviaApi openTriviaApi;

    public OpenTriviaService() {
        Retrofit retrofit = ApiClient.getClient();
        openTriviaApi = retrofit.create(OpenTriviaApi.class);
    }

    public void getTrivia(int amount, String difficulty, String type, Consumer<TriviaResponse> onResponseHandler) {
        openTriviaApi.getTrivia(amount, difficulty, type)
                .enqueue(new CallbackHandler<>(onResponseHandler));
    }

}
