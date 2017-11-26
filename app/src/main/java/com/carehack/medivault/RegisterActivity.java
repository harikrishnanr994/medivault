package com.carehack.medivault;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sachin on 19/11/17.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText name_edit,dob_edit,address_edit,height_edit,weight_edit,allergies_edit;
    private Button btn_continue;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private static String phone,uid,role;
    private DatabaseReference mRef;
    ProgressBar progressBar;
    Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Utils.pref,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.contains("address"))
        {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        name_edit = findViewById(R.id.name);
        dob_edit = findViewById(R.id.dob);
        height_edit = findViewById(R.id.height);
        weight_edit = findViewById(R.id.weight);
        allergies_edit = findViewById(R.id.allergies);

        address_edit = findViewById(R.id.address);
        progressBar = findViewById(R.id.progressbar);
        btn_continue = findViewById(R.id.btn_continue);
        mRef = FirebaseDatabase.getInstance().getReference();
        myCalendar  = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dob_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        checkAddress();
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate()
    {
        String name = name_edit.getText().toString();
        String dob = dob_edit.getText().toString();
        String address = address_edit.getText().toString();
        String height = height_edit.getText().toString();
        String weight = weight_edit.getText().toString();
        String allergies = allergies_edit.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            name_edit.setError("Field is Empty");
        }
        else if(TextUtils.isEmpty(dob))
        {
            dob_edit.setError("Field is Empty");
        }
        else if(TextUtils.isEmpty(address))
        {
            address_edit.setError("Field is Empty");
        }
        else if(TextUtils.isEmpty(height))
        {
            height_edit.setError("Field is Empty");
        }
        else if(TextUtils.isEmpty(weight))
        {
            weight_edit.setError("Field is Empty");
        }
        else if(name.length()<4)
        {
            name_edit.setError("Name should be more than 4 characters");
        }
        else
        {
            registerDetails(name,dob,address,height,weight,allergies);
        }
    }

    private void registerDetails(final String name, final String dob, final String address ,final String height, final String weight , final String allergies) {
        role = sharedPreferences.getString("role", "");
        phone = sharedPreferences.getString("phone", "");
        Map<String,String> map = new HashMap<>();
        map.put("Name",name);
        map.put("Address",address);
        map.put("DOB",dob);
        map.put("Weight",weight);
        map.put("Height",height);
        map.put("Allergies",allergies);
        map.put("Role",role);
        map.put("UID",uid);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Registering Details");
        progressDialog.show();
        mRef.child("Users").child(phone).setValue(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                editor.putString("address",address);
                editor.putString("name",name);
                editor.putString("dob",dob);
                editor.putString("height",height);
                editor.putString("weight",weight);
                editor.putString("allergies",allergies);
                editor.apply();
                progressDialog.dismiss();
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });
    }


    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        dob_edit.setText(sdf.format(myCalendar.getTime()));
    }

    public void checkAddress()
    {
        if(sharedPreferences.contains("uid")) {
            uid = sharedPreferences.getString("uid", "");
            phone = sharedPreferences.getString("phone", "");
            mRef.child("Users").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("Name").getValue(String.class);
                    String dob = dataSnapshot.child("DOB").getValue(String.class);
                    String height = dataSnapshot.child("Height").getValue(String.class);
                    String weight = dataSnapshot.child("Weight").getValue(String.class);
                    String allergies = dataSnapshot.child("Allergies").getValue(String.class);
                    String address = dataSnapshot.child("Address").getValue(String.class);
                    name_edit.setText(name);
                    dob_edit.setText(dob);
                    address_edit.setText(address);
                    height_edit.setText(height);
                    weight_edit.setText(weight);
                    allergies_edit.setText(allergies);
                    name_edit.setVisibility(View.VISIBLE);
                    dob_edit.setVisibility(View.VISIBLE);
                    address_edit.setVisibility(View.VISIBLE);
                    height_edit.setVisibility(View.VISIBLE);
                    allergies_edit.setVisibility(View.VISIBLE);
                    weight_edit.setVisibility(View.VISIBLE);
                    findViewById(R.id.textView).setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    btn_continue.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
