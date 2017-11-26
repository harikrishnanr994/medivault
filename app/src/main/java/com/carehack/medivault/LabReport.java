package com.carehack.medivault;

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

import java.util.ArrayList;

public class LabReport extends AppCompatActivity {

    private FirebaseDatabase firebase;
    private DatabaseReference ref;
    private ArrayList<DataClass> labReports;
    private RecyclerView labReportRV;
    private RecyclerView.Adapter labReportsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lab_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        labReportRV=findViewById(R.id.labReportRV);
        labReportRV.setLayoutManager(new LinearLayoutManager(LabReport.this));
        labReportsAdapter=new LabReportsRVAdapter(labReports);
        labReportRV.setAdapter(labReportsAdapter);
    }

}
