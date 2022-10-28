package com.shrimali.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shrimali.test.models.DBHelper;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    EditText vehicleNumber, passwordVehicle, rePasswordVehicle;
    String vehicleNumberString, passwordString, rePasswordString, type;
    DBHelper DB;
    Button regBtnVehicle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();

//        set the XML variable to the controller file
        vehicleNumber = findViewById(R.id.vehicleNumber);
        passwordVehicle = findViewById(R.id.passwordVehicle);
        rePasswordVehicle = findViewById(R.id.rePasswordVehicle);
        regBtnVehicle = findViewById(R.id.regBtnVehicle);


        DB = new DBHelper(this);


//        signUp btn clicked
        regBtnVehicle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vehicleNumberString = vehicleNumber.getText().toString();
                        passwordString = passwordVehicle.getText().toString();
                        rePasswordString = rePasswordVehicle.getText().toString();
                        type = "1";
                        if (vehicleNumberString.isEmpty() && passwordString.isEmpty() && rePasswordString.isEmpty()) {
                            System.out.println();
                            Toast.makeText(SignUpActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        } else {
                            if (rePasswordString.equals(passwordString)) {
                                Boolean checkUser = DB.checkUsername(vehicleNumberString);
                                if (!checkUser) {
                                    Boolean insert = DB.insertData(vehicleNumberString, passwordString, type,"No district");
                                    if (insert) {
                                        Toast.makeText(SignUpActivity.this, "Successfully created Account", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Error in creating", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Already has a acc", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignUpActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );


    }
}