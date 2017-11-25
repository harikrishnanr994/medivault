package com.carehack.medivault;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class HospitalViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HospitalAdapter hospitalAdapter;
    List<DataClass> hospitalList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView =  findViewById(R.id.recyclerview);
        hospitalAdapter = new HospitalAdapter(hospitalList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hospitalAdapter);

        prepareDummyData();
    }

    private void prepareDummyData() {
        for(int i=0;i<10;i++)
        {
            DataClass dataClass = new DataClass();
            dataClass.setDate("Date 1");
            dataClass.setSubtitle("Subtitle 1");
            dataClass.setTitle("Title 1");
            hospitalList.add(dataClass);
        }
        hospitalAdapter.notifyDataSetChanged();
    }
}
