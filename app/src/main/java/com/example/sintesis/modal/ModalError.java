package com.example.sintesis.modal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.sintesis.R;

public class ModalError extends AppCompatActivity {

    Button btnAceptar;
    private Dialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private TextView tvMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modal_error);
    }
}