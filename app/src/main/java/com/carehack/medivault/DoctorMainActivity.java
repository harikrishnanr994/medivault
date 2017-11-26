package com.carehack.medivault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carehack.medivault.custom_font.MyTextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sachin on 26/11/17.
 */

public class DoctorMainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    MyTextView name_txt,dob_txt,height_txt,weight_txt;

    CardView connect,hospital,card_view_prescription,lab_report;
    String phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_auth_main);
        name_txt = findViewById(R.id.name);
        dob_txt = findViewById(R.id.dob);
        height_txt = findViewById(R.id.height);
        weight_txt = findViewById(R.id.weight);
        phone = getIntent().getStringExtra("phone");
        sharedPreferences = getSharedPreferences(Utils.pref,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String name = sharedPreferences.getString("name","");
        String dob = sharedPreferences.getString("dob","");
        String height = sharedPreferences.getString("height","");
        String weight = sharedPreferences.getString("weight","");
        String allergies = sharedPreferences.getString("allergies","");
        name_txt.setText(name);
        dob_txt.setText(dob);
        height_txt.setText(height);
        weight_txt.setText(weight);
        connect = findViewById(R.id.card_view_connect);
        hospital = findViewById(R.id.card_view_hospital);
        lab_report = findViewById(R.id.card_view_lab_report);
        card_view_prescription = findViewById(R.id.card_view_prescription);
        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorMainActivity.this,DoctorViewHospitalActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
        });
        lab_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorMainActivity.this,DoctorViewLapReportActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
        });
    }
}
