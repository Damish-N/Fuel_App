package com.shrimali.test;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.shrimali.test.models.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpFuelStation extends AppCompatActivity {

    EditText fuelStationName, district, fuelStationPw, fuelStationRPw;
    Button regBtnFuelStation;
    String userName, password, rePassword, districtOfFuelStation, type;
    DBHelper DB;
    ProgressBar pId;


    @SuppressLint("MissingInflatedId")
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
        pId = findViewById(R.id.pId);
        pId.setVisibility(View.GONE);

        DB = new DBHelper(this);


        regBtnFuelStation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pId.setVisibility(View.VISIBLE);
                        regBtnFuelStation.setVisibility(View.GONE);
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
                                    Boolean insert = DB.insertData(userName, password, "0", districtOfFuelStation, "p&d");
                                    if (insert) {
                                        createFuelStationOnMongoDB(userName);
                                        Toast.makeText(SignUpFuelStation.this, "Successfully created Account", Toast.LENGTH_SHORT).show();
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
                        pId.setVisibility(View.GONE);
                        regBtnFuelStation.setVisibility(View.VISIBLE);

//                        Call for Mongo cluster create a mongoDB on fuel station


                    }
                }
        );


    }

    private void createFuelStationOnMongoDB(String userName) {
        String url = "http://192.168.8.100:8081/api/Fuel/UpdateFuelArriveTime";
        RequestQueue mRequestQueue = Volley.newRequestQueue(SignUpFuelStation.this);
        //Create a object with without Arrival time
        JSONObject objectForPetrol = new JSONObject();
        JSONObject objectForDiesel = new JSONObject();
        try {
            objectForPetrol.put("FuelStation", userName);
            objectForPetrol.put("FuelType", "Petrol");
            objectForDiesel.put("FuelStation", userName);
            objectForDiesel.put("FuelType", "Diesel");
//            object.put("ArrivalTime","2022-10-30T01:06:00.000+00:00");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequestForPetrol = getJsonObjectRequest(url, objectForPetrol);
        JsonObjectRequest jsonObjectRequestForDiesel = getJsonObjectRequest(url, objectForDiesel);

        mRequestQueue.add(jsonObjectRequestForPetrol);
        mRequestQueue.add(jsonObjectRequestForDiesel);


    }

    JsonObjectRequest getJsonObjectRequest(String url, JSONObject object) {
        return new JsonObjectRequest(
                Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        JSONObject jsonObject = new JSONObject((Map) response);
                        try {
                            if (response.getString("status").equals("Success") && object.get("FuelType").toString().equals("Diesel")) {
                                System.out.println("Successfully added both accounts");
                                pId.setVisibility(View.GONE);
                                regBtnFuelStation.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(SignUpFuelStation.this, SignInFuelStation.class);
                                startActivity(intent);
                                finish();
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
    }
}