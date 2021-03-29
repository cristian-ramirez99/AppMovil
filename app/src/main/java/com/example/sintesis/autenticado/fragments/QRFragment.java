package com.example.sintesis.autenticado.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sintesis.R;
import com.example.sintesis.RetrofitInterface;
import com.example.sintesis.auth.Login;
import com.example.sintesis.models.Producto;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QRFragment extends Fragment {

    public String token;

    private Dialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private TextView tvMensaje;
    private Button btnAceptar;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000/api/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(QRFragment.this);

        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan QR code");
        integrator.setBeepEnabled(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);


        Intent intent = getActivity().getIntent();
        token = intent.getStringExtra(Login.TOKEN);

        integrator.initiateScan();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_q_r, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                if (result.getContents().contains("http://localhost:4200/dashboard/producto/")) {
                    Toast.makeText(getContext(), "URL ok mi pana", Toast.LENGTH_LONG).show();

                    //Obtenemos la posicion del ultimo '/' en el url que nos da el QR
                    int posicionUltimoSlash = result.getContents().lastIndexOf("/");

                    //Obtenemos el idProducto
                    String idProducto = result.getContents().substring(posicionUltimoSlash + 1);

                    //Hacemos peticion HTTP GETProducto por id
                    getProducto(idProducto);

                } else {
                    Toast.makeText(getContext(), "Scanned : " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void getProducto(String idProducto) {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //Hace peticion @POST(/usuarios)
        Call<Producto> call = retrofitInterface.getProducto(token, idProducto);

        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                //Si todo ok peticion GETProducto
                if (response.code() == 200) {
                    Producto producto = response.body();
                    modalProducto(producto);
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                //No lo tengo claro, porque el modal no va
                String mensaje = getString(R.string.error_conexion_DB);
                open_modal(mensaje);
            }
        });
    }

    private void open_modal(String mensaje) {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View modalView = getLayoutInflater().inflate(R.layout.activity_modal_error, null);

        //View de los widgets del modal
        btnAceptar = modalView.findViewById(R.id.btnAceptarModalError);
        tvMensaje = modalView.findViewById(R.id.tvMensajeModalError);

        //Cambiamos el mensaje
        tvMensaje.setText(mensaje);

        //Cerra modal onClick
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setView(modalView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void modalProducto(Producto producto) {

    }
}