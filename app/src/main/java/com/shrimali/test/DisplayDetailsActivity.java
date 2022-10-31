package com.shrimali.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class DisplayDetailsActivity extends AppCompatActivity {

    TextView fuelStationNameAv, fuelStationTypeAv, fuelStationArriavalAv, fuelStationQueueAv;
    Button addingToQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);

        Objects.requireNonNull(getSupportActionBar()).hide();
        fuelStationNameAv = findViewById(R.id.fuelStationNameAv);
        fuelStationTypeAv = findViewById(R.id.fuelStationTypeAv);
        fuelStationArriavalAv = findViewById(R.id.fuelStationArriavalAv);
        fuelStationQueueAv = findViewById(R.id.fuelStationQueueAv);
        addingToQueue = findViewById(R.id.addingToQueue);


        Intent intent = getIntent();
        String nameOfStation = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");
        String arrivalTime = intent.getStringExtra("arrivalTime");

        System.out.println(nameOfStation);
        String[] split = nameOfStation.split(":");

        fuelStationNameAv.setText("Fuel Station : " + split[0].toString());
        fuelStationTypeAv.setText("Fuel Type : " + split[1]);
        fuelStationArriavalAv.setText("Arrival Time : " + arrivalTime);

//        getQueueLenght(split[0],split[1]);


    }
}