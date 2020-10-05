package com.example.yaneodoo;

import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.Review;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.Info.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
    @GET("users/{id}")
    Call<User> getUser(@Header("Authorization") String token, @Path("id") String id);
    //@GET("users/{userId}")
    //Call<User> getUser(@Header("Authorization") String token, @Path("userId") String userId);

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
    @POST("stores")
    Call<Store> postStore(@Header("Authorization") String token, @Body Store store);

    //Update or create a store
    @PATCH("stores/{id}")
    Call<Store> patchStore(@Header("Authorization") String token, @Body Store store, @Path("id") String storeId);

    //Delete a store
    @DELETE("stores/{id}")
    Call<Void> deleteStore(@Header("Authorization") String token, @Path("id") String storeId);

    //---------------order----------------//

    //get orders
    @GET("orders")
    Call<List<Order>> getOrders(@Header("Authorization") String token);

    //get user orders
    @GET("orders")
    Call<List<Order>> getUserOrders(@Header("Authorization") String token, @Query("userId") String userId);

    //get store orders
    @GET("orders")
    Call<List<Order>> getStoreOrders(@Header("Authorization") String token, @Query("storeId") String storeId);

    //get store orders
    @GET("orders")
    Call<List<Order>> getStoreOrder(@Header("Authorization") String token, @Query("storeId") String storeId, @Query("sort") String sort, @Query("size") String size);//, @Query("size") String size);

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

    //----------------order------------------//
    //Search orders
    @GET("orders")
    Call<List<Order>> getOrderList(@Header("Authorization") String token, @Query("storeId") String storeId);
}
