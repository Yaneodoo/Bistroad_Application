package com.example.yaneodoo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {
    @GET("/users")
    Call<String> getBistroList();
}
