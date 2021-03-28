package com.example.sintesis;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("api/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("api/usuarios")
    Call<Void> executeSignUp(@Body HashMap<String, String> map);


}
