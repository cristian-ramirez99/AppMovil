package com.example.sintesis.autenticado.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sintesis.R;
import com.example.sintesis.RetrofitInterface;

import retrofit2.Retrofit;

public class CarritoFragment extends Fragment {
    private final String BASE_URL = "http://10.0.2.2:3000/api/";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carrito, container, false);
    }

    private void getProductos() {
    }
}