package com.example.sintesis.autenticado.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sintesis.R;
import com.example.sintesis.RetrofitInterface;
import com.example.sintesis.auth.Login;
import com.example.sintesis.auth.LoginResult;
import com.example.sintesis.models.Producto;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QRFragment extends Fragment {
    private final String SIMBOLO_EURO = "\u20ac";

    public String token;

    private Dialog dialog;
    private AlertDialog.Builder dialogBuilder;

    private Button btnCancelar;
    private Button btnAceptar;
    private TextView tvNombreProducto;
    private TextView tvPrecioProducto;
    private TextView tvPrecioTotalProducto;
    private EditText etCantidad;
    private ImageView ivSuma;
    private ImageView ivResta;
    private ImageView ivProducto;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000/api/";

    private Button btn_abrir_scanner_QR;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r, container, false);

        //Instanciamos intent para obtener datos de anteriores intents
        Intent intent = getActivity().getIntent();

        //Obtenemos token del usuarios
        token = intent.getStringExtra(Login.TOKEN);

        //Obtnemos referencia de los widgets
        btn_abrir_scanner_QR = view.findViewById(R.id.btnActivarScannerQR);

        //OnClik abrir escaner QR
        btn_abrir_scanner_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initQR();
            }
        });


        open_modal_producto(new Producto("Mouse Gayming", "Muy polivalente", 50.24, "no-image", "1233", 50));

        // Inflate the layout for this fragment
        return view;
    }

    //Inicia el escaner QR
    private void initQR() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(QRFragment.this);

        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan QR code");
        integrator.setBeepEnabled(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //Si el QR tiene el link de un producto nuestro
                if (result.getContents().contains("http://localhost:4200/dashboard/producto/")) {
                    Toast.makeText(getContext(), "URL ok mi pana", Toast.LENGTH_LONG).show();

                    //Obtenemos la posicion del ultimo '/' en el url que nos da el QR
                    int posicionUltimoSlash = result.getContents().lastIndexOf("/");

                    //Obtenemos el idProducto
                    String idProducto = result.getContents().substring(posicionUltimoSlash + 1);

                    //Hacemos peticion HTTP GETProducto por id
                    getProducto(idProducto);

                } else {
                    //Si se lee un QR, pero no es un producto nuestro
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

        //Hace peticion @GET Producto
        Call<Producto> call = retrofitInterface.getProducto(token, idProducto);

        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                //Si todo ok peticion GETProducto
                if (response.code() == 200) {
                    Producto producto = response.body();
                    open_modal_producto(producto);
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                String mensaje = getString(R.string.error_conexion_DB);
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void open_modal_producto(Producto producto) {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View modalView = getLayoutInflater().inflate(R.layout.activity_modal_producto, null);

        //View de los widgets del modal
        btnAceptar = modalView.findViewById(R.id.btnAceptarModalProducto);
        btnCancelar = modalView.findViewById(R.id.btnCancelarModalProducto);
        tvNombreProducto = modalView.findViewById(R.id.tvNombreProductoModalProducto);
        tvPrecioProducto = modalView.findViewById(R.id.tvPrecioModalProducto);
        tvPrecioTotalProducto = modalView.findViewById(R.id.tvPrecioTotalModalProducto);
        etCantidad = modalView.findViewById(R.id.etCantidadModalProducto);
        ivSuma = modalView.findViewById(R.id.ivSumaModalProducto);
        ivResta = modalView.findViewById(R.id.ivRestaModalProducto);
        ivProducto = modalView.findViewById(R.id.ivProductoModaProducto);

        //setImagen del producto
        String url = BASE_URL + "upload/usuarios/56487648-6690-4a8d-b80e-2665c5539578.png";
        Glide.with(getContext()).load(url).into(ivProducto);

        tvNombreProducto.setText(producto.nombre);

        //Formato con dos decimnales
        DecimalFormat format = new DecimalFormat("#.00");// el numero de ceros despues del entero
        String precio = format.format(producto.getPrecio());

        //setPrecio del producto
        tvPrecioProducto.setText(precio + SIMBOLO_EURO);

        //Por default el precio y el precio total es igual
        tvPrecioTotalProducto.setText(precio + SIMBOLO_EURO);

        //Si la cantidad cambia actualizamos el precioTotal
        etCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    //Obtner cantidad de View
                    String strCantidad = etCantidad.getText().toString();

                    //Str to int
                    int nuevaCantidad = Integer.valueOf(strCantidad);

                    //Calcular precioTotal
                    Double precioTotalProducto = calcularPrecioTotalProducto(producto.getPrecio(), nuevaCantidad);

                    //Darle formato de dos decimales
                    String strPrecioTotalProducto = format.format(precioTotalProducto);

                    //Cambiar el precioTotal en View
                    tvPrecioTotalProducto.setText(strPrecioTotalProducto + SIMBOLO_EURO);

                } catch (Exception e) {
                    //Siempre tiene que tener como minimo 1 la cantidad
                    etCantidad.setText("1");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //OnClick se suma uno a la cantidad y se actualiza el precioTotal
        ivSuma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtner cantidad de View
                String strCantidad = etCantidad.getText().toString();

                //Parsear cantidad y sumar uno
                int nuevaCantidad = Integer.valueOf(strCantidad) + 1;
                String strNuevaCantidad = String.valueOf(nuevaCantidad);

                //Cambiar la cantidad en View
                etCantidad.setText(strNuevaCantidad);

                //Calcular precioTotal
                Double precioTotalProducto = calcularPrecioTotalProducto(producto.getPrecio(), nuevaCantidad);

                //Darle formato de dos decimales
                String strPrecioTotalProducto = format.format(precioTotalProducto);

                //Cambiar el precioTotal en View
                tvPrecioTotalProducto.setText(strPrecioTotalProducto + SIMBOLO_EURO);
            }
        });

        //OnClick se resta uno a la cantidad (si cantidad > 1) y se actualiza el precioTotal
        ivResta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtner cantidad de View
                String strCantidad = etCantidad.getText().toString();

                if (!strCantidad.equals("1")) {

                    //Parsear la cantidad y restar 1
                    int nuevaCantidad = Integer.valueOf(strCantidad) - 1;
                    String strNuevaCantidad = String.valueOf(nuevaCantidad);

                    //Cambiar la cantidad en View
                    etCantidad.setText(strNuevaCantidad);

                    //Calcular precioTotal
                    Double precioTotalProducto = calcularPrecioTotalProducto(producto.getPrecio(), nuevaCantidad);

                    //Darle formato de dos decimales
                    String strPrecioTotalProducto = format.format(precioTotalProducto);

                    //Cambiar el precioTotal
                    tvPrecioTotalProducto.setText(strPrecioTotalProducto + SIMBOLO_EURO);
                }
            }
        });
        //OnClick en el btnAceptar añadimos el producto al carrito y cerramos modal
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.producto_añadido, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        //OnClick en btnCancelar cerra modal
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Mostrar modal
        dialogBuilder.setView(modalView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    //Devuelve el subtotal de un producto
    private double calcularPrecioTotalProducto(Double precio, int cantidad) {
        return precio * cantidad;
    }

}