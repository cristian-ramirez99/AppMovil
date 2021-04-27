package com.example.sintesis.autenticado.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sintesis.environments.Environments;
import com.example.sintesis.models.LineaPedido;
import com.example.sintesis.results.ProductoResult;
import com.example.sintesis.R;
import com.example.sintesis.RetrofitInterface;
import com.example.sintesis.auth.Login;
import com.example.sintesis.models.Producto;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
    public String idPedido;

    private Dialog dialog;
    private AlertDialog.Builder dialogBuilder;

    private Button btnCancelar;
    private Button btnAceptar;
    private TextView tvStock;
    private TextView tvNombreProducto;
    private TextView tvPrecioProducto;
    private TextView tvPrecioTotalProducto;
    private EditText etCantidad;
    private ImageView ivSuma;
    private ImageView ivResta;
    private ImageView ivProducto;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private Button btn_abrir_scanner_QR;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r, container, false);

        //Instanciamos intent para obtener datos de anteriores intents
        Intent intent = getActivity().getIntent();

        //Obtenemos token del usuarios
        token = intent.getStringExtra(Login.TOKEN);

        Bundle args = getArguments();
        idPedido = args.getString("idPedido");
        System.out.println(idPedido);

        //Obtnemos referencia de los widgets
        btn_abrir_scanner_QR = view.findViewById(R.id.btnActivarScannerQR);

        //OnClik abrir escaner QR
        btn_abrir_scanner_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initQR();
            }
        });

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
            } else {
                //Si el QR tiene el link de un producto nuestro
                if (result.getContents().contains("http://localhost:4200/dashboard/producto/")) {
                    //Obtenemos la posicion del ultimo '/' en el url que nos da el QR
                    int posicionUltimoSlash = result.getContents().lastIndexOf("/");

                    //Obtenemos el idProducto
                    String idProducto = result.getContents().substring(posicionUltimoSlash + 1);

                    //Hacemos peticion HTTP GETProducto por id
                    getProducto(idProducto);

                } else {
                    //Si se lee un QR, pero no es un producto nuestro
                    Toast.makeText(getContext(), "El QR no pertenece a ningun producto", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void getProducto(String idProducto) {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(Environments.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //Hace peticion @GET Producto
        Call<ProductoResult> call = retrofitInterface.getProducto(token, idProducto);

        call.enqueue(new Callback<ProductoResult>() {
            @Override
            public void onResponse(Call<ProductoResult> call, Response<ProductoResult> response) {
                //Si todo ok peticion GETProducto
                if (response.code() == 200) {
                    Producto producto = response.body().getProducto();
                    open_modal_producto(producto);
                }
            }

            @Override
            public void onFailure(Call<ProductoResult> call, Throwable t) {
                String mensaje = getString(R.string.error_conexion_DB);
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearLineaPedido(Producto producto, String strCantidad) {
        int cantidadSeleccionada = Integer.valueOf(strCantidad);

        if (producto.getStock() >= cantidadSeleccionada) {
            //Convertimos HTTP API in to interface de java
            retrofit = new Retrofit.Builder()
                    .baseUrl(Environments.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //Crear interface
            retrofitInterface = retrofit.create(RetrofitInterface.class);
            Toast.makeText(getContext(), "IdPEdido: " + idPedido, Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "IdProducto: " + producto.getId(), Toast.LENGTH_SHORT).show();

            HashMap<String, String> map = new HashMap<>();
            map.put("producto", producto.getId());
            map.put("pedido", idPedido);
            map.put("cantidad", strCantidad);

            //Hace peticion @POST lineaPedido
            Call<Void> call = retrofitInterface.crearLineaPedido(token, map);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    //Si todo ok peticion GETProducto
                    if (response.code() == 200) {
                        Toast.makeText(getContext(), R.string.producto_añadido, Toast.LENGTH_SHORT).show();
                        actualizarStock(producto, strCantidad);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    String mensaje = getString(R.string.error_conexion_DB);
                    Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.error_stock_insuficiente, Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarStock(Producto producto, String strCantidad) {
        //Convertimos HTTP API in to interface de java
        retrofit = new Retrofit.Builder()
                .baseUrl(Environments.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Crear interface
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        int cantidadSeleccionada = Integer.valueOf(strCantidad);

        HashMap<String, Integer> map = new HashMap<>();
        int stock = producto.getStock() - cantidadSeleccionada;
        map.put("stock", stock);

        //Hace peticion @PUT(/productos)
        Call<Void> call = retrofitInterface.actualizarStock(token, producto.getId(), map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Stock actualizado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Si no se puede conectar al servidor
                System.out.println(t.getMessage());
            }
        });
    }

    private void open_modal_producto(Producto producto) {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View modalView = getLayoutInflater().inflate(R.layout.activity_modal_producto, null);

        //View de los widgets del modal
        btnAceptar = modalView.findViewById(R.id.btnAceptarModalProducto);
        btnCancelar = modalView.findViewById(R.id.btnCancelarModalProducto);
        tvStock = modalView.findViewById(R.id.tvStockModalProducto);
        tvNombreProducto = modalView.findViewById(R.id.tvNombreProductoModalProducto);
        tvPrecioProducto = modalView.findViewById(R.id.tvPrecioModalProducto);
        tvPrecioTotalProducto = modalView.findViewById(R.id.tvPrecioTotalModalProducto);
        etCantidad = modalView.findViewById(R.id.etCantidadModalProducto);
        ivSuma = modalView.findViewById(R.id.ivSumaModalProducto);
        ivResta = modalView.findViewById(R.id.ivRestaModalProducto);
        ivProducto = modalView.findViewById(R.id.ivProductoModaProducto);
        
        if (producto.getStock() > 0) {
            tvStock.setVisibility(View.INVISIBLE);
        }

        String url = producto.getImg();
        Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();

        //setImagen del producto
        Glide.with(getContext()).load(url).into(ivProducto);

        tvNombreProducto.setText(producto.getNombre());

        //Formato con dos decimales
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
                String strCantidad = etCantidad.getText().toString();

                crearLineaPedido(producto, strCantidad);
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