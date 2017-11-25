package com.carehack.medivault;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by harihara on 25/11/17.
 */

public class DoctorAddActivity extends AppCompatActivity {

    Button submit;
    com.jaredrummler.materialspinner.MaterialSpinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_add);

        spinner = findViewById(R.id.disease_select);
        spinner.setItems("Fever","Headache","Back pain", "Knee pain");
    }
}
