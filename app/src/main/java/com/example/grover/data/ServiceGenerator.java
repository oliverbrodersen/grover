package com.example.grover.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("https://trefle.io/")
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static TrefleApi trefleApi = retrofit.create(TrefleApi.class);

    public static TrefleApi getTrefleApi() {
        return trefleApi;
    }
}
