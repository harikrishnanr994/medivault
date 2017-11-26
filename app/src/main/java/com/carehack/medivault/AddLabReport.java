package com.carehack.medivault;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddLabReport extends AppCompatActivity {
    private Spinner dataSelectSpinner;
    private TextView labResultValueTV;
    private Button submitReportBtn;
    private String labName;
    private ArrayList<String> options;
    DataClass dataClass;
    private DatabaseReference mRef;
    String name;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lab_report);
        sharedPreferences = getSharedPreferences(Utils.pref, MODE_PRIVATE);
        options=new ArrayList<>();
        options.add("<Select Data>");
        options.add("Data Set 1");
        options.add("Data Set 2");
        options.add("Data Set 3");
        name = sharedPreferences.getString("name","");
        dataClass = (DataClass) getIntent().getSerializableExtra("data");
        dataSelectSpinner=findViewById(R.id.dataSelectSpinner);
        labResultValueTV=findViewById(R.id.labResultValuesTV);
        submitReportBtn=findViewById(R.id.submitLabReportButton);
        mRef = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddLabReport.this, android.R.layout.simple_spinner_item, options);
        dataSelectSpinner.setAdapter(adapter);

        dataSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int choice=dataSelectSpinner.getSelectedItemPosition();
                switch (choice)
                {
                    case 0: labResultValueTV.setText("");
                        break;
                    case 1: labResultValueTV.setText("WBC -- 13.0(N) -- 4.8-20.8 \nRBC -- 5.4(N) -- 4.7-6.1\nHb -- 14.1(N) -- 14.0-18.0");
                        break;
                    case 2: labResultValueTV.setText("WBC -- 24.6(H) -- 4.8-20.8 \nRBC -- 1.9(L) -- 4.7-6.1\nHb -- 12.0(L) -- 14.0-18.0");
                        break;
                    case 3: labResultValueTV.setText("WBC -- 6.9(N) -- 4.8-20.8 \nRBC -- 1.8(L) -- 4.7-6.1\nHb -- 6.5(L) -- 14.0-18.0");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        submitReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        AddLabReport.this);
                final String phone = dataClass.getSubtitle();
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Are you sure you want the following details to " + phone);
                alertDialog.setIcon(R.mipmap.ic_launcher);

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final ProgressDialog progressDialog = new ProgressDialog(AddLabReport.this);
                        progressDialog.setTitle("Saving");
                        progressDialog.show();
                        String report=labResultValueTV.getText().toString();
                        Map<String,String> map = new HashMap<>();
                        map.put("Report",report);
                        Log.d("NAme",name);
                        mRef.child("Lab Reports").child(phone).push().setValue(map, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                mRef.child("Pending Reports").child("By Name").child(name).orderByChild("Phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d("DS_PD",dataSnapshot.toString());
                                        dataSnapshot.getRef().removeValue();
                                        mRef.child("Pending Reports").child("By Phone").child(phone).orderByChild("Lab Name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().removeValue();
                                                progressDialog.dismiss();
                                                Intent intent = getIntent();
                                                intent.putExtra("data", dataClass);
                                                setResult(RESULT_OK, intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
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
