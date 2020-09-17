package com.example.dilemma_mobile.consumeApi;

import com.example.dilemma_mobile.model.Store;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CustomerServiceAPI {

    @GET("/api/v1/customers/{customerId}/journey")
    Call<List<Store>> getJourney(@Path("id") long id);
}
