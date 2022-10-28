package com.shrimali.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shrimali.test.models.DBHelper;

import java.util.Objects;

public class SignUpFuelStation extends AppCompatActivity {

    EditText fuelStationName, district, fuelStationPw, fuelStationRPw;
    Button regBtnFuelStation;
    String userName, password, rePassword, districtOfFuelStation, type;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_fuel_stattion);
        Objects.requireNonNull(getSupportActionBar()).hide();

        fuelStationName = findViewById(R.id.fuelStationName);
        district = findViewById(R.id.district);
        fuelStationPw = findViewById(R.id.fuelStationPw);
        fuelStationRPw = findViewById(R.id.fuelStationRPw);
        regBtnFuelStation = findViewById(R.id.regBtnFuelStation);

        DB = new DBHelper(this);


        regBtnFuelStation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userName = fuelStationName.getText().toString();
                        password = fuelStationPw.getText().toString();
                        rePassword = fuelStationRPw.getText().toString();
                        districtOfFuelStation = district.getText().toString();
                        type = "1";
                        if (userName.isEmpty() && password.isEmpty() && rePassword.isEmpty()) {
                            Toast.makeText(SignUpFuelStation.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        } else {
                            if (rePassword.equals(password)) {
                                Boolean checkUser = DB.checkUsername(userName);
                                if (!checkUser) {
                                    Boolean insert = DB.insertData(userName, password, "0", districtOfFuelStation);
                                    if (insert) {
                                        Toast.makeText(SignUpFuelStation.this, "Successfully created Account", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpFuelStation.this, SignInFuelStation.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SignUpFuelStation.this, "Error in creating", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(SignUpFuelStation.this, "Already has a acc", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignUpFuelStation.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );


    }
}