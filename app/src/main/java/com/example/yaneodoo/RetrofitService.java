package com.example.yaneodoo;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.Review;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    //---------------user----------------//
    //Get an user profile from given token
    @GET("users/me")
    Call<User> getUserMe(@Header("Authorization") String token);

    //---------------store----------------//
    //Search stores
    @GET("stores")
    Call<List<Store>> getStoreList(@Header("Authorization") String token, @Query("ownerId") String ownerId);

    //Search nearby stores
    //TODO: stores/nearby로
    @GET("stores/nearby")
    Call<List<Store>> getNearbyStoreList(@Header("Authorization") String token, @Query("originLat") Double originLat,
                      @Query("originLng") Double originLng, @Query("radius") Double radius, @Query("sort") String sort);

    //Get a store by ID
    @GET("stores/{id}")
    Call<Store> getStore(@Header("Authorization") String token, @Path("id") String storeId);

    //Create a store
    @POST("stores")
    Call<Store> postStore(@Header("Authorization") String token, @Body Store store);

    //Update or create a store
    @PATCH("stores/{id}")
    Call<Store> patchStore(@Header("Authorization") String token, @Body Store store, @Path("id") String storeId);

    //Delete a store
    @DELETE("stores/{id}")
    Call<Void> deleteStore(@Header("Authorization") String token, @Path("id") String storeId);

    //---------------order----------------//
    //get user orders
    @GET("orders")
    Call<List<Order>> getUserOrders(@Header("Authorization") String token, @Query("userId") String userId);

    //get store orders
    @GET("orders")
    Call<List<Order>> getStoreOrders(@Header("Authorization") String token, @Query("storeId") String storeId, @Query("sort") String sort);

    //get store orders
    @GET("orders")
    Call<List<Order>> getStoreOrder(@Header("Authorization") String token,
                      @Query("storeId") String storeId, @Query("sort") String sort, @Query("size") String size);//, @Query("size") String size);

    //get order info
    @GET("orders/{id}")
    Call<Order> getOrderInfo(@Header("Authorization") String token, @Path("orderId") String orderId);

    //Create an order
    @POST("orders")
    Call<Order> postOrder(@Header("Authorization") String token, @Body Order order);

    //delete order
    @DELETE("orders")
    Call<Order> deleteOrder(@Header("Authorization") String token);

    //Update an order partially
    @PATCH("orders/{id}")
    Call<Order> patchOrder(@Header("Authorization") String token, @Body Order order, @Path("id") String id);

    //---------------store-items-------------//
    //Search items
    @GET("stores/{storeId}/items")
    Call<List<Menu>> getMenuList(@Header("Authorization") String token, @Path("storeId") String storeId);

    //Create an item
    @POST("stores/{storeId}/items")
    Call<Menu> postMenu(@Header("Authorization") String token, @Body Menu menu, @Path("storeId") String storeId);

    //Update an item partially
    @PATCH("stores/{storeId}/items/{id}")
    Call<Menu> patchMenu(@Header("Authorization") String token, @Body Menu menu, @Path("storeId") String storeId, @Path("id") String id);

    //Delete an item
    @DELETE("stores/{storeId}/items/{id}")
    Call<Void> deleteMenu(@Header("Authorization") String token, @Path("storeId") String storeId, @Path("id") String id);

    //--------------store-items-review------------//
    //Search reviews
    @GET("reviews")
    Call<List<Review>> getReviewList(@Header("Authorization") String token, @Query("storeId") String storeId, @Query("itemId") String itemId);

    @FormUrlEncoded
    @POST("stores/{storeId}/items")
    Call<Store> postReview(@Header("Authorization") String token, @Field("review") Review review);

    //--------------store-photo------------------//
    //Upload a store photo
    @POST("stores/{id}/photo")
    Call<Void> postStorePhoto(@Header("Authorization") String token, @Path("id") String id);

    //--------------store-items-photo------------------//
    //Upload a store item photo
    @Multipart
    @POST("stores/{storeId}/items/{id}/photo")
    Call<Menu> postItemPhoto(@Header("Authorization") String token, @Part MultipartBody.Part file, @Path("storeId") String storeId, @Path("id") String id);
}
