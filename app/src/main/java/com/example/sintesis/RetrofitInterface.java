package com.example.sintesis;

import com.example.sintesis.auth.LoginResult;
import com.example.sintesis.models.Carrito;
import com.example.sintesis.models.Producto;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @POST("login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("usuarios")
    Call<Void> executeRegistrar(@Body HashMap<String, String> map);

    @GET("carrito")
    Call<Carrito> getCarrito(@Header("token") String token, @Body int id);

    @GET("producto/{id}")
    Call<Producto> getProducto(@Header("token") String token, @Path("id") String id);


}
