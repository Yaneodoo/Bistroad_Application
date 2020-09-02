package com.example.yaneodoo;

import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {

    //---------------user----------------//
    //사용자 목록을 조회합니다.
    @GET("users")
    Call<List<User>> getUserList();

    //특정 사용자를 조회합니다.
    @GET("users/{userId}")
    Call<User> getUser(@Path("userId") String userId);

    //새로운 사용자를 등록합니다.
    @FormUrlEncoded
    @POST("users")
    Call<User> postUser(@Field("user") User user);

    //사용자 정보를 일부 수정합니다.
    @PATCH("users/{userId}")
    Call<User> patchUser(@Path("userId") String userId);

    //사용자 계정을 삭제합니다.
    @DELETE("users/{userId}")
    Call<ResponseBody> deleteUser();

    //---------------store----------------//
    //Search stores
    @GET("stores")
    Call<List<Store>> getStoreList();

}
