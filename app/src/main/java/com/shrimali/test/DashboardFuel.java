package com.shrimali.test;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shrimali.test.models.ProgramAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DashboardFuel extends AppCompatActivity {

    ArrayList<String> shedId = new ArrayList<>();
    ArrayList<String> shedName = new ArrayList<>();
    ListView listView;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar progressFuel;

    String url = "http://192.168.8.100:8081/api/Fuel/GetFuelStatus";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_fuel);

        Objects.requireNonNull(getSupportActionBar()).hide();
        listView = findViewById(R.id.mainItemFuel);
        progressFuel = findViewById(R.id.progressFuel);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String s1 = sh.getString("userName", "");

        System.out.println("Your email is:" + s1);
        progressFuel.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        getData(s1, "Petrol");
        getData(s1, "Diesel");


    }

    private void getData(String s, String oil) {
        mRequestQueue = Volley.newRequestQueue(this);

//        url = url + "?FuelStation=" + s + "&FuelType=Petrol" + oil;
        url = "http://192.168.8.100:8081/api/Fuel/GetFuelStatus?FuelStation=" + s + "&FuelType=" + oil;
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = getJsonObjectRequest(url, s, oil);

        mRequestQueue.add(jsonObjectRequest);
        ProgramAdapter programAdapter = new ProgramAdapter(this, shedId, shedName);
        listView.setAdapter(programAdapter);
    }

    JsonObjectRequest getJsonObjectRequest(String url, String s, String oil) {
        return new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response);
                    JSONObject j = new JSONObject(String.valueOf(response.get("data")));
                    shedName.add((String) j.get("FuelStation") + ":" + (String) j.get("FuelType"));
                    System.out.println(j.get("id"));
                    shedId.add((String) j.get("id"));
//                    if (oil.equals("Diesel")) {
                    progressFuel.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
//                    }
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