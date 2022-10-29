package com.shrimali.test;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shrimali.test.models.ProgramAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {

    ArrayList<String> shedId = new ArrayList<>();
    ArrayList<String> shedName = new ArrayList<>();
    ListView listView;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Objects.requireNonNull(getSupportActionBar()).hide();
        listView = findViewById(R.id.mainItem);

        shedId.add("Item-1");
        shedName.add("colombo");
        shedId.add("Item-1");
        shedName.add("colombo");
        shedId.add("Item-1");
        shedName.add("colombo");
        shedId.add("Item-1");
        shedName.add("colombo");
        shedId.add("Item-1");
        shedName.add("colombo");
        shedId.add("Item-1");
        shedName.add("colombo");
        shedId.add("Item-1");
        shedName.add("colombo");
        shedId.add("Item-1");
        shedName.add("colombo");
        shedId.add("Item-1");
        shedName.add("colombo");

//        JsonRequest
        getData();


        ProgramAdapter programAdapter = new ProgramAdapter(this, shedId, shedName);
        listView.setAdapter(programAdapter);


    }

    private void getData() {
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
//                        Toast.makeText(getApplicationContext(), "Response :" + response.toString(), Toast.LENGTH_LONG).show();
//                        shedId.add()
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }
}