package com.shrimali.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.shrimali.test.models.ProgramAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {

    ArrayList<String> shedId = new ArrayList<>();
    ArrayList<String> shedName = new ArrayList<>();
    ListView listView;


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



        ProgramAdapter programAdapter = new ProgramAdapter(this, shedId, shedName);
        listView.setAdapter(programAdapter);



    }
}