package com.example.yaneodoo;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    //Get an user profile from given token
    @GET("users/me")
    Call<User> getUserMe(@Header("Authorization") String token);

    //---------------store----------------//
    //Search stores
    @GET("stores")
    Call<List<Store>> getStoreList(@Header("Authorization") String token, @Query("ownerId") String ownerId);

    //Search nearby stores
    //TODO: stores/nearby로
    @GET("stores")
    Call<List<Store>> getNearbyStoreList(@Header("Authorization") String token);

    //Get a store by ID
    @GET("stores/{id}")
    Call<Store> getStore(@Header("Authorization") String token, @Path("id") String storeId);

    //Create a store
    @FormUrlEncoded
    @POST("stores")
    Call<Store> postStore(@Header("Authorization") String token, @Field("store") Store store);

    //---------------order----------------//

    //get user orders
    @GET("orders")
    Call<List<Order>> getUserOrders(@Header("Authorization") String token, @Query("userId") String userId);

    //get store orders
    @GET("orders")
    Call<List<Order>> getStoreOrders(@Header("Authorization") String token, @Query("storeId") String storeId);

    //get store orders
    @GET("orders?size=1")
    Call<Order> getStoreOrder(@Header("Authorization") String token, @Query("storeId") String storeId);

    //get order info
    @GET("orders/{id}")
    Call<Order> getOrderInfo(@Header("Authorization") String token, @Path("orderId") String orderId);

    //send order
    @FormUrlEncoded
    @POST("orders")
    Call<Order> postOrder(@Header("Authorization") String token);

    //delete order
    @DELETE("orders")
    Call<Order> deleteOrder(@Header("Authorization") String token);

    //edit order
    @PATCH("orders/{id}")
    Call<Order> patchOrder(@Header("Authorization") String token, @Path("orderId") String orderId);

    //---------------store-items-------------//
    //Search items
    @GET("stores/{storeId}/items")
    Call<List<Menu>> getMenuList(@Header("Authorization") String token, @Path("storeId") String storeId);

    //Create an item
    @FormUrlEncoded
    @POST("stores/{storeId}/items")
    Call<Store> postStore(@Header("Authorization") String token, @Path("storeId") String storeId, @Field("menu") Menu menu);
}
