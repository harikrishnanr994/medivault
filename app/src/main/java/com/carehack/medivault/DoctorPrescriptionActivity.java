package com.carehack.medivault;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by harihara on 25/11/17.
 */

public class DoctorPrescriptionActivity extends AppCompatActivity {

    Button submit;
    com.jaredrummler.materialspinner.MaterialSpinner spinner;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_add);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Lab Report");
        setSupportActionBar(toolbar);
        spinner = findViewById(R.id.disease_select);
        spinner.setItems("Fever","Headache","Back pain", "Knee pain");
    }
}
