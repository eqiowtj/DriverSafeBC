package edu.northeastern.driversafebc.a7atyourservice.api;

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

    public CallbackHandler(Consumer<T> onResponseHandler) {
        this(onResponseHandler, (System.err::println));
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onResponseHandler.accept(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailureHandler.accept(t);
    }

}
