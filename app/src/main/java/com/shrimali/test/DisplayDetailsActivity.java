package com.shrimali.test;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shrimali.test.models.ProgramAdapterAvailable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DisplayDetailsActivity extends AppCompatActivity {

    TextView fuelStationNameAv, fuelStationTypeAv, fuelStationArriavalAv, fuelStationQueueAv;
    Button addingToQueue;
    private RequestQueue mRequestQueue;
    String userName, fType;


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

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        userName = sh.getString("userName", "");
        fType = sh.getString("fType", "");


        Intent intent = getIntent();
        String nameOfStation = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");
        String arrivalTime = intent.getStringExtra("arrivalTime");

        System.out.println(nameOfStation);
        String[] split = nameOfStation.split(":");

        fuelStationNameAv.setText("Fuel Station : " + split[0].toString());
        fuelStationTypeAv.setText("Fuel Type : " + split[1]);
        fuelStationArriavalAv.setText("Arrival Time : " + arrivalTime);

        getQueueLenght(split[0], split[1]);


        addingToQueue.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addToThequeueCall(userName, split[0]);
                    }
                }
        );


    }

    private void addToThequeueCall(String userName, String s) {
        String url = "http://192.168.8.101:8081/api/Fuel/UpdateUserArrivalTime";
        JSONObject object = new JSONObject();
        try {
            object.put("FuelStation", s);
            object.put("VehicleNumber", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try {
                    if (response.get("status").equals("Success")) {
                        new MaterialAlertDialogBuilder(DisplayDetailsActivity.this)
                                .setMessage(response.getString("message"))
                                .setPositiveButton("Ok",
                                        (dialogInterface, i) -> {
                                            Intent intent = new Intent(DisplayDetailsActivity.this, DashboardActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                )
                                .show();
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
            }
        };
        mRequestQueue.add(jsonObjectRequest);

    }

    private void getQueueLenght(String fuelStationName, String fType) {
        String url = "http://192.168.8.101:8081/api/Fuel/GetFuelQueue?fuelStation=" + fuelStationName + "&fuelType=" + fType;
        mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("Success")) {
                        System.out.println(response.getString("data"));
                        fuelStationQueueAv.setText("Queue Length : " + response.getString("data"));
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
            }
        };

        mRequestQueue.add(jsonObjectRequest);

    }
}