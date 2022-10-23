package edu.northeastern.driversafebc.a7atyourservice.api;

import edu.northeastern.driversafebc.a7atyourservice.pojo.TriviaResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenTriviaApi {

    @GET("api.php?encode=base64")
    Call<TriviaResponse> getTrivia(@Query("amount") int amount,
                                   @Query("difficulty") String difficulty,
                                   @Query("type") String type);

}
