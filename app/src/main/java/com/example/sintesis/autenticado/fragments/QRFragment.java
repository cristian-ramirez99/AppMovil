package com.example.sintesis.autenticado.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sintesis.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(QRFragment.this);

        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan QR code");
        integrator.setBeepEnabled(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);


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

                Toast.makeText(getContext(), "Scanned : " + result.getContents(), Toast.LENGTH_LONG).show();


            }
        }
    }
}