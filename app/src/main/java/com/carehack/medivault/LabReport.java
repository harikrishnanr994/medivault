package com.carehack.medivault;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

public class LabReport extends AppCompatActivity {

    private FirebaseDatabase firebase;
    private DatabaseReference ref;
    private ArrayList<DataClass> labReports;
    private RecyclerView labReportRV;
    private RecyclerView.Adapter labReportsAdapter;
    private SlidingUpPanelLayout mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lab Records");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLayout = findViewById(R.id.sliding_layout);
        setSupportActionBar(toolbar);
        labReports=new ArrayList<>();
        firebase=FirebaseDatabase.getInstance();
        ref=firebase.getReference().child("Lab Report");

        labReports.add(new DataClass());
        labReports.get(0).setTitle("Riverrun Test Clinic");
        labReports.get(0).setDate("21-09-2017");

        labReports.add(new DataClass());
        labReports.get(1).setTitle("Aerys Clinic");
        labReports.get(1).setDate("2-11-2017");

        labReportRV=findViewById(R.id.recyclerview);
        labReportRV.setLayoutManager(new LinearLayoutManager(LabReport.this));
        labReportsAdapter=new LabReportsRVAdapter(labReports);
        labReportRV.setAdapter(labReportsAdapter);

        labReportRV.addOnItemTouchListener(new RecyclerItemClickListener(this, labReportRV, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        return true;
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
