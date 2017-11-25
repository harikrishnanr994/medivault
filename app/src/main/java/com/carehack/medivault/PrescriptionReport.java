package com.carehack.medivault;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bineesh on 26/11/2017.
 */

public class PrescriptionReport extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterPrescriptionReport adapterc;
    List<DataClass> preList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_view); Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView =  findViewById(R.id.recyclerview);
        adapterc = new AdapterPrescriptionReport(preList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterc);
        load();

    }

    private void load() {
        for(int i=0;i<10;i++)
        {
            DataClass dataClass = new DataClass();
            dataClass.setDate("Date 1");
            dataClass.setSubtitle("Subtitle 1");
            dataClass.setTitle("Title 1");
            preList.add(dataClass);
        }
        adapterc.notifyDataSetChanged();
    }
}