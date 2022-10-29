package com.shrimali.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.shrimali.test.models.DBHelper;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    EditText vehicleNumber, passwordVehicle, rePasswordVehicle;
    String vehicleNumberString, passwordString, rePasswordString, type,fType;
    DBHelper DB;
    Button regBtnVehicle;
    Spinner spinner;
    String[] items = new String[]{"Petrol", "Diesel"};

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
        spinner = findViewById(R.id.typeOfFuel);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);


        DB = new DBHelper(this);


//        signUp btn clicked
        regBtnVehicle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vehicleNumberString = vehicleNumber.getText().toString();
                        passwordString = passwordVehicle.getText().toString();
                        rePasswordString = rePasswordVehicle.getText().toString();
                        fType = spinner.getSelectedItem().toString();
                        type = "1";
                        if (vehicleNumberString.isEmpty() && passwordString.isEmpty() && rePasswordString.isEmpty()) {
                            System.out.println();
                            Toast.makeText(SignUpActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        } else {
                            if (rePasswordString.equals(passwordString)) {
                                Boolean checkUser = DB.checkUsername(vehicleNumberString);
                                if (!checkUser) {
                                    Boolean insert = DB.insertData(vehicleNumberString, passwordString, type,"No district",fType);
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