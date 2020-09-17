package com.example.dilemma_mobile.consumeApi;

import com.example.dilemma_mobile.model.Frequentation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FrequentationServiceAPI {
    @POST("/api/v1/frequentations/")
    Call<Frequentation> save(@Body Frequentation frequentation);
}
