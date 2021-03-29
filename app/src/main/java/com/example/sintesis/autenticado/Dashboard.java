package com.example.sintesis.autenticado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sintesis.auth.Login;
import com.example.sintesis.R;
import com.example.sintesis.autenticado.fragments.CarritoFragment;
import com.example.sintesis.autenticado.fragments.QRFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {
    Fragment carritoFragment = new CarritoFragment();
    Fragment QRFragment = new QRFragment();


    public String token;

    private TextView tvCorreo;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        token = intent.getStringExtra(Login.TOKEN);
        String correo = intent.getStringExtra(Login.CORREO);

        tvCorreo = findViewById(R.id.tvCorreoDashboard);
        tvCorreo.setText(correo);

        //Navbar
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ic_qr:
                    loadFragment(QRFragment);
                    Toast.makeText(Dashboard.this, token, Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.ic_carrito:
                    loadFragment(carritoFragment);
                    Toast.makeText(Dashboard.this, token, Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.ic_signout:
                    change_activity_to_login();
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    private void change_activity_to_login() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}