package com.shrimali.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shrimali.test.models.DBHelper;

import java.util.Objects;

public class SignInFuelStation extends AppCompatActivity {

    EditText fuelStationUserName,fuelStationPassword;
    TextView signUpBtnInFuelStation;
    Button signInBtnFuelStation;
    DBHelper DB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_fuel_station);
        Objects.requireNonNull(getSupportActionBar()).hide();


        fuelStationUserName = findViewById(R.id.fuelStationUserName);
        fuelStationPassword = findViewById(R.id.fuelStationPassword);
        signUpBtnInFuelStation = findViewById(R.id.signUpBtnInFuelStation);
        signInBtnFuelStation = findViewById(R.id.signInBtnFuelStation);
        DB = new DBHelper(this);



        signUpBtnInFuelStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInFuelStation.this, SignUpFuelStation.class);
                startActivity(intent);
            }
        });

        signInBtnFuelStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String  userName = fuelStationUserName.getText().toString();
                String password = fuelStationPassword.getText().toString();
                System.out.println(userName + password);
                if (userName.isEmpty() && password.isEmpty()) {
                    Toast.makeText(SignInFuelStation.this, "please fill fully", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUser = DB.checkUserNamePassword(userName, password, "0");
                    if (checkUser) {
                        Toast.makeText(SignInFuelStation.this, "Signing", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInFuelStation.this, DashboardActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignInFuelStation.this, "UserName or Password wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });







    }
}