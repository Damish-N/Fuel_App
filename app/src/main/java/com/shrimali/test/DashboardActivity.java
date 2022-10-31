package com.shrimali.test;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shrimali.test.models.ProgramAdapter;
import com.shrimali.test.models.ProgramAdapterAvailable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {

    ArrayList<String> shedId = new ArrayList<>();
    ArrayList<String> shedName = new ArrayList<>();
    ArrayList<String> arriveTime = new ArrayList<>();
    ListView listView;
    CardView cardView;
    String userName, fType;
    TextView vehicle_details, vehicle_shed, waiting_time, vStatus;
    Button dptButton;


    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Objects.requireNonNull(getSupportActionBar()).hide();
        listView = findViewById(R.id.mainItem);
        vehicle_details = findViewById(R.id.vehicle_details);
        vehicle_shed = findViewById(R.id.vehicle_shed);
        waiting_time = findViewById(R.id.waiting_time);
        cardView = findViewById(R.id.statusOfVehicle);
        dptButton = findViewById(R.id.dptButton);
        vStatus = findViewById(R.id.vStatus);
        cardView.setVisibility(View.GONE);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        userName = sh.getString("userName", "");
        fType = sh.getString("fType", "");

        vStatus.setText("Vehicle : " + userName);

//        shedId.add("Item-1");
//        shedName.add("colombo");
//        shedId.add("Item-1");
//        shedName.add("colombo");
//        shedId.add("Item-1");
//        shedName.add("colombo");
//        shedId.add("Item-1");
//        shedName.add("colombo");
//        shedId.add("Item-1");
//        shedName.add("colombo");
//        shedId.add("Item-1");
//        shedName.add("colombo");

//        JsonRequest
        getData(userName);


        dptButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        depatureFromQueue(userName);
                    }
                }
        );


    }

    private void depatureFromQueue(String userName) {
        String url = "http://192.168.8.101:8081/api/Fuel/UpdateUserDepartTime";
        JSONObject object = new JSONObject();
        try {
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
                        new MaterialAlertDialogBuilder(DashboardActivity.this)
                                .setMessage("Successfully Departure")
                                .setPositiveButton("Ok",
                                        (dialogInterface, i) -> {
                                            Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
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

    private void getData(String userName) {

        String url = "http://192.168.8.101:8081/api/Fuel/GetQueueWaitingTime?vehicleNumber=" + userName;
        mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String split[] = response.get("message").toString().split(":");
                    if (response.getString("status").equals("Success") && split[0].equals("Vehicle is in the queue on station")) {
                        cardView.setVisibility(View.VISIBLE);
                        vehicle_details.setText(userName);
                        vehicle_shed.setText("Waiting shed: " + split[1]);
                        waiting_time.setText("Waiting time" + response.getString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getTheShedDetails();
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

    private void getTheShedDetails() {
        String url = "http://192.168.8.101:8081/api/Fuel/GetAllFuelStations?FuelType=" + fType;
        mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("Success")) {
                        JSONArray jsonArray = response.getJSONArray("data");
//                        for(:response.getJSONArray("data"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String shedNameS = new JSONObject(String.valueOf(jsonArray.get(i))).get("FuelStation").toString();
                            String arrTime = new JSONObject(String.valueOf(jsonArray.get(i))).get("ArrivalTime").toString();
                            shedName.add(shedNameS + ":" + fType);
                            shedId.add("Fuel Available");
                            arriveTime.add(arrTime);
                            System.out.println(new JSONObject(String.valueOf(jsonArray.get(i))).get("FuelStation"));
                        }
                        ProgramAdapterAvailable programAdapterAvailable = new ProgramAdapterAvailable(getBaseContext(), shedId, shedName, arriveTime);
                        listView.setAdapter(programAdapterAvailable);

//                        System.out.println(response.getString("data"));
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