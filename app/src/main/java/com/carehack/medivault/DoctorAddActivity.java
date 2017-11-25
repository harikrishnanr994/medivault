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
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_add);

        spinner = (Spinner) findViewById(R.id.disease_select);
        String[] objects = {"Fever","Headache","Body Pain","Throat pain","Back Pain"};
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,objects));

    }
}
