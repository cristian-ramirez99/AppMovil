package com.example.sintesis;

import com.example.sintesis.auth.LoginResult;
import com.example.sintesis.models.Producto;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Call<ArrayList<Producto>> getCarrito(@Header("x-token") String token);

    @GET("producto/{id}")
    Call<Producto> getProducto(@Header("x-token") String token, @Path("id") String id);

    @DELETE("carritoProducto/{id}")
    Call<Void> deleteProducto(@Header("x-token") String token, @Path("id") String id);

}
