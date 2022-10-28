package com.shrimali.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class StartPage extends AppCompatActivity {

    Button vehicleOwner, fuelStation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        Objects.requireNonNull(getSupportActionBar()).hide();
        vehicleOwner = findViewById(R.id.vehicleOwner);
        fuelStation = findViewById(R.id.fuelStation);


        vehicleOwner.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(StartPage.this, SignInActivity.class);
                        startActivity(intent);
                    }
                }
        );

        fuelStation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(StartPage.this, SignInFuelStation.class);
                        startActivity(intent);
                    }
                }
        );


    }
}