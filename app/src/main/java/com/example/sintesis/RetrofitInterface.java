package com.example.sintesis;

import com.example.sintesis.results.LineaPedidoResult;
import com.example.sintesis.results.PedidoResult;
import com.example.sintesis.results.LoginResult;
import com.example.sintesis.results.RenewResult;
import com.example.sintesis.results.ProductoResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @POST("login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @GET("login/renew")
    Call<RenewResult> renew(@Header("x-token") String token);

    @PUT("login/recuperarPassword")
    Call<Void> recuperarPassword(@Body HashMap<String,String> map);

    @POST("usuarios")
    Call<Void> executeRegistrar(@Body HashMap<String, String> map);

    @POST("pedidos")
    Call<PedidoResult> crearPedido(@Header("x-token") String token, @Body HashMap<String, String> map);

    @GET("pedidos/temp/{id}")
    Call<PedidoResult> getPedidoTemp(@Header("x-token") String token, @Path("id") String id);

    @GET("lineaPedidos/{id}")
    Call<LineaPedidoResult> getLineaPedido(@Header("x-token") String token, @Path("id") String id);

    @GET("productos/{id}")
    Call<ProductoResult> getProducto(@Header("x-token") String token, @Path("id") String id);

    @PUT("productos/{id}")
    Call<Void> actualizarStock(@Header("x-token") String token, @Path("id") String id, @Body HashMap<String, Integer> map);

    @POST("lineaPedidos")
    Call<Void> crearLineaPedido(@Header("x-token") String token, @Body HashMap<String, String> map);

    @DELETE("lineaPedidos/{id}")
    Call<Void> deleteLineaPedido(@Header("x-token") String token, @Path("id") String id);
}
