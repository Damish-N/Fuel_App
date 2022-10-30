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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SubmitDetailsActivity extends AppCompatActivity {

    EditText stationName, fuelType, arrivalTime, departureTime;
    Button submitArrival, departureTimeFuelBtn;
    RequestQueue mRequestQueue;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_details);
        Objects.requireNonNull(getSupportActionBar()).hide();


        Intent intent = getIntent();
        String nameOfStation = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");

        System.out.println(nameOfStation);
        String[] split = nameOfStation.split(":");

        stationName = findViewById(R.id.stationName);
        fuelType = findViewById(R.id.fuelType);
        arrivalTime = findViewById(R.id.arrivalTime);
        submitArrival = findViewById(R.id.submitArrival);
        departureTime = findViewById(R.id.departureTime);
        departureTimeFuelBtn = findViewById(R.id.departureTimeFuelBtn);


        getData(split[0], split[1]);


//        DatePicker simpleDatePicker = (DatePicker) findViewById(R.id.simpleDatePicker); // initiate a date picker

        stationName.setText(split[0]);
        fuelType.setText(split[1]);

//        simpleDatePicker.setSpinnersShown(true);
//
//        simpleDatePicker.setMinDate(System.currentTimeMillis());
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            simpleDatePicker.setOnDateChangedListener(
//                    new DatePicker.OnDateChangedListener() {
//                        @Override
//                        public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
//                            int day = datePicker.getDayOfMonth();
//                            int month = datePicker.getMonth() + 1;
//                            int year = datePicker.getYear();
//                            System.out.println(day);
//                            arrivalTime.setText(day);
//
//                        }
//                    }
//            );
//        }

        submitArrival.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateArrivalTime(split);
                    }
                }
        );

        departureTimeFuelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateFinishedTime(split);
                    }
                }
        );

    }

    private void updateFinishedTime(String[] split) {
        String url = "http://192.168.8.100:8081/api/Fuel/UpdateFuelFinishTime";
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        System.out.println(timeStamp);
        JSONObject object = new JSONObject();
        try {
            object.put("FuelStation", split[0].toString());
            object.put("FuelType", split[1].toString());
            object.put("FinishTime",timeStamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try {
                    if (response.get("status").equals("Success")) {
                        new MaterialAlertDialogBuilder(SubmitDetailsActivity.this)
                                .setMessage("Updated the Finished")
                                .setPositiveButton("Ok",
                                        (dialogInterface, i) -> {
                                            Intent intent = new Intent(SubmitDetailsActivity.this, DashboardFuel.class);
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

    private void updateArrivalTime(String[] split) {
        String url = "http://192.168.8.100:8081/api/Fuel/UpdateFuelArriveTime";
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        System.out.println(timeStamp);
        JSONObject object = new JSONObject();
        try {
            object.put("FuelStation", split[0].toString());
            object.put("FuelType", split[1].toString());
            object.put("ArrivalTime", timeStamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRequestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try {
                    if (response.get("status").equals("Success")) {
                        new MaterialAlertDialogBuilder(SubmitDetailsActivity.this)
                                .setMessage("Updated the ArrivalTime")
                                .setPositiveButton("Ok",
                                        (dialogInterface, i) -> {
                                            Intent intent = new Intent(SubmitDetailsActivity.this, DashboardFuel.class);
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

    private void getData(String s, String oil) {
        mRequestQueue = Volley.newRequestQueue(this);

//        url = url + "?FuelStation=" + s + "&FuelType=Petrol" + oil;
        String url = "http://192.168.8.100:8081/api/Fuel/GetFuelStatus?FuelStation=" + s + "&FuelType=" + oil;
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = getJsonObjectRequest(url, s, oil);

        mRequestQueue.add(jsonObjectRequest);

    }

    JsonObjectRequest getJsonObjectRequest(String url, String s, String oil) {
        return new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response);
                    JSONObject j = new JSONObject(String.valueOf(response.get("data")));
                    System.out.println(j.get("id"));
                    arrivalTime.setText(j.get("ArrivalTime").toString());
                    departureTime.setText(j.get("FinishTime").toString());

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

    }
}