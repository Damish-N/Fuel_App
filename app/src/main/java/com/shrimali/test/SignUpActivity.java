package com.shrimali.test;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shrimali.test.models.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    EditText vehicleNumber, passwordVehicle, rePasswordVehicle;
    String vehicleNumberString, passwordString, rePasswordString, type, fType;
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
                                    Boolean insert = DB.insertData(vehicleNumberString, passwordString, type, "No district", fType);
                                    if (insert) {
                                        createNewUserOnMongo(vehicleNumberString, fType);
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

    public void createNewUserOnMongo(String VehicleNumber, String fType) {
        String url = "http://192.168.8.100:8081/api/Fuel/UpdateUserArrivalTime";
        RequestQueue mRequestQueue = Volley.newRequestQueue(SignUpActivity.this);
        JSONObject object = new JSONObject();

        try {
            object.put("VehicleNumber", VehicleNumber);
            object.put("FuelType", fType);
//            object.put("ArrivalTime","2022-10-30T01:06:00.000+00:00");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        JSONObject jsonObject = new JSONObject((Map) response);
                        try {
                            if (response.getString("status").equals("Success") && response.get("message").toString().equals("Inserted new Record")) {
                                System.out.println("Successfully added Vehicle accounts");

                                new MaterialAlertDialogBuilder(SignUpActivity.this)
                                        .setMessage("Successfully Created Account")
                                        .setPositiveButton("Login",
                                                (dialogInterface, i) -> {
                                                    Toast.makeText(SignUpActivity.this, "Successfully created Account", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                    startActivity(intent);
                                                }
                                        )
                                        .show();
//                                pId.setVisibility(View.GONE);
//                                regBtnFuelStation.setVisibility(View.VISIBLE);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Response Error: " + error.getMessage());
            }
        }
        ) {
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                Log.d(TAG, "volleyError" + volleyError);
                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "FuelAPI", "123456");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Content-Type", "application/json");
                params.put("Authorization", auth);
                System.out.println(params);
                return params;
            }
        };


        mRequestQueue.add(jsonObjectRequest);
    }
}