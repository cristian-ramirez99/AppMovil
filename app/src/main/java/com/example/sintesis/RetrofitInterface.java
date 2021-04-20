package com.example.sintesis;

import com.example.sintesis.autenticado.fragments.LineaPedidoResult;
import com.example.sintesis.autenticado.fragments.PedidoResult;
import com.example.sintesis.auth.LoginResult;
import com.example.sintesis.auth.RenewResult;
import com.example.sintesis.models.LineaPedido;
import com.example.sintesis.models.Pedido;
import com.example.sintesis.models.Producto;
import com.example.sintesis.models.Usuario;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @POST("login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @GET("login/renew")
    Call<RenewResult> renew(@Header("x-token") String token);

    @POST("usuarios")
    Call<Void> executeRegistrar(@Body HashMap<String, String> map);

    @POST("pedidos")
    Call<PedidoResult> crearPedido(@Header("x-token") String token, @Body HashMap<String, String> map);

    @GET("pedidos/temp/{id}")
    Call<PedidoResult> getPedidoTemp(@Header("x-token") String token, @Path("id") String id);

    @GET("lineaPedidos/{id}")
    Call<LineaPedidoResult> getLineaPedido(@Header("x-token") String token, @Path("id") String id);

    @GET("productos/{id}")
    Call<Producto> getProducto(@Header("x-token") String token, @Path("id") String id);

    @PUT("productos/{id}")
    Call<Void> actualizarStock(@Header("x-token") String token, @Path("id") String id, @Body HashMap<String, Integer> map);

    @POST("lineaPedidos")
    Call<Void> crearLineaPedido(@Header("x-token") String token, @Path("id") String id);

    @DELETE("lineaPedidos/{id}")
    Call<Void> deleteLineaPedido(@Header("x-token") String token, @Path("id") String id);


}
