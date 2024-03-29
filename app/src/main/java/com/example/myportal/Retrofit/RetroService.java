package com.example.myportal.Retrofit;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetroService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("name") Integer id,
                                    @Field("password") String password);
    @POST("loginName")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("name") String name,
                                 @Field("id") Integer id,
                                 @Field("password") String password);
    @POST("loginEmail")
    @FormUrlEncoded
    Observable<String> loginUserEmail(@Field("email") String name,
                                 @Field("password") String password);
}
