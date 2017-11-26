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
        /*for(int i=0;i<10;i++)
        {
            DataClass dataClass = new DataClass();
            dataClass.setDate("Date 1");
            dataClass.setSubtitle("Subtitle 1");
            dataClass.setTitle("Title 1");
            hospitalList.add(dataClass);
        }*/
        DataClass dataclass1 = new DataClass("KIMS","Dr.Vinod Baskar","30-08-2017");
        hospitalList.add(dataclass1);
        DataClass dataclass2 = new DataClass("Amrita Institute","Dr.Raj","08-04-2017");
        hospitalList.add(dataclass2);
        DataClass dataclass3 = new DataClass("Karuna Medical College","Dr.Sarojini","17-05-2017");
        hospitalList.add(dataclass3);
        DataClass dataclass4 = new DataClass("West Fort","Dr.Hell","4-08-2017");
        hospitalList.add(dataclass4);
        DataClass dataclass5 = new DataClass("Daya Medical College","Dr.Sinoj","12-11-2017");
        hospitalList.add(dataclass5);
        DataClass dataclass6 = new DataClass("Sacred Hearts","Dr.Vinu","7-03-2017");
        hospitalList.add(dataclass6);
        DataClass dataclass7 = new DataClass("MI mission hospital","Dr.Raghavan","28-06-2017");
        hospitalList.add(dataclass7);
        DataClass dataclass8 = new DataClass("Govt Medical college","Dr.Manu","01-05-2017");
        hospitalList.add(dataclass8);
        hospitalAdapter.notifyDataSetChanged();
    }
}
