package com.example.grover.data;

import com.example.grover.models.trefle.TrefleJSONRecived;
import com.example.grover.models.trefle.trefleSpeciesComplete.Data;
import com.example.grover.models.trefle.trefleSpeciesComplete.Root;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrefleApi {
    @GET("/api/v1/plants/{id}?token=U5iJxQo3mMAF3rXHjrOBPMPYCMYRAZLOhEM8ln_OskE")
    Call<Root> getDataFromId(@Path("id") int id);

    @GET("/api/v1/plants/search?token=U5iJxQo3mMAF3rXHjrOBPMPYCMYRAZLOhEM8ln_OskE")
    Call<TrefleJSONRecived> search(@Query("q") String query);
}
