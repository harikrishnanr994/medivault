package com.carehack.medivault;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by harihara on 25/11/17.
 */

public class DoctorPrescriptionActivity extends AppCompatActivity {

    Button submit;
    com.jaredrummler.materialspinner.MaterialSpinner spinner;
    Toolbar toolbar;
    Button btn_submit;
    EditText editText;
    String phone;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Utils.pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mRef = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_doctor_add);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Report");
        btn_submit = findViewById(R.id.btn_submit);
        setSupportActionBar(toolbar);
        spinner = findViewById(R.id.disease_select);
        editText = findViewById(R.id.editText);
        phone = getIntent().getStringExtra("phone");
        spinner.setItems("Fever","Headache","Back pain", "Knee pain");
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        DoctorPrescriptionActivity.this);

                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Are you sure you want the following details to " + phone);
                alertDialog.setIcon(R.mipmap.ic_launcher);

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final ProgressDialog progressDialog = new ProgressDialog(DoctorPrescriptionActivity.this);
                        progressDialog.setTitle("Saving");
                        progressDialog.show();
                        String disease = spinner.getItems().get(spinner.getSelectedIndex()).toString();
                        String pre = editText.getText().toString();
                        Map<String,String> map = new HashMap<>();
                        map.put("Disease",disease);
                        map.put("Prescriptions",pre);
                        mRef.child("Prescriptions").child(phone).push().setValue(map, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });
    }
}
