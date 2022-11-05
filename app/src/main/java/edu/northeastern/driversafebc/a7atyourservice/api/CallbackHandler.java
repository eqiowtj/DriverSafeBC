package edu.northeastern.driversafebc.a7atyourservice.api;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallbackHandler<T> implements Callback<T> {

    private final Consumer<T> onResponseHandler;
    private final Consumer<Throwable> onFailureHandler;

    public CallbackHandler(Consumer<T> onResponseHandler, Consumer<Throwable> onFailureHandler) {
        this.onResponseHandler = onResponseHandler;
        this.onFailureHandler = onFailureHandler;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onResponseHandler.accept(response.body());
        } else {
            onFailureHandler.accept(new RuntimeException("Request failed."));
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, Throwable t) {
        t.printStackTrace();
        onFailureHandler.accept(t);
    }

}
