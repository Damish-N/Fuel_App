package com.shrimali.test;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shrimali.test.models.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignInFuelStation extends AppCompatActivity {

    EditText fuelStationUserName, fuelStationPassword;
    TextView signUpBtnInFuelStation;
    Button signInBtnFuelStation;
    DBHelper DB;
    RequestQueue mRequestQueue;
    JsonObjectRequest request;

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

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();


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
                String userName = fuelStationUserName.getText().toString();
                String password = fuelStationPassword.getText().toString();
                System.out.println(userName);
                myEdit.putString("userName", userName);
                myEdit.putString("fType", "Fuel Type");

                myEdit.commit();
//                try {
//                    getData();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                if (userName.isEmpty() && password.isEmpty()) {
//                    Toast.makeText(SignInFuelStation.this, "please fill fully", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUser = DB.checkUserNamePassword(userName, password, "0");
                    if (checkUser) {
                        Toast.makeText(SignInFuelStation.this, "Signing", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInFuelStation.this, DashboardFuel.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignInFuelStation.this, "UserName or Password wrong", Toast.LENGTH_SHORT).show();
                    }
                }


//                JSONObject object = new JSONObject();
//                try {
//                    object.put("FuelType", "Petrol");
//                    object.put("FuelStattion", "Colombo");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                request = new JsonObjectRequest(Request.Method.GET, url, object,
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                },
//                );


//            }
//
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                String creds = String.format("%s:%s", "USERNAME", "PASSWORD");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                return params;
//            }


//
            }
        });

    }

    /* Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("VehicleNumber", "AAA-0000");
        postParam.put("FuelStation", "Colombo");*/
    private void getData() throws JSONException {
        String url = "http://192.168.8.101:8081/api/Fuel/GetFuelStatus?FuelStation=F-0001&FuelType=Petrol";
        mRequestQueue = Volley.newRequestQueue(SignInFuelStation.this);
        JSONObject object = new JSONObject();
        try {
            object.put("FuelStation", "F-0001");
            object.put("FuelType", "Petrol");
//            object.put("ArrivalTime", "2022-10-30T01:06:00.000+00:00");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        Toast.makeText(SignInFuelStation.this, "Okey", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignInFuelStation.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
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


                //
                ////                String text = "key";
                //                String text = String.format("%s:%s", "FuelAPI", "123456");
                //                byte[] data = null;
                //                try {
                //                    data = text.getBytes("UTF-8");
                //                } catch (UnsupportedEncodingException e1) {
                //                    e1.printStackTrace();
                //                }
                //                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                //                //  Authorization: Basic $auth
                //                HashMap<String, String> headers = new HashMap<String, String>();
                //
                ////                        headers.put("User-Agent","Bing Search Client for Android");
                ////                        headers.put("Content-Type", "application/x-www-form-urlencoded");
                //                headers.put("Content-Type", "application/json; charset=utf-8");
                //                headers.put("Authorization", "Basic " + base64);
                //                return headers;
            }
        };
        //        jRequest.setRetryPolicy(
        //                new RetryPolicy() {
        //                    @Override
        //                    public int getCurrentTimeout() {
        //                        return 50000;
        //                    }
        //
        //                    @Override
        //                    public int getCurrentRetryCount() {
        //                        return 50000;
        //                    }
        //
        //                    @Override
        //                    public void retry(VolleyError error) throws VolleyError {
        //
        //                    }
        //                }
        //        );
        mRequestQueue.add(jRequest);
    }
}