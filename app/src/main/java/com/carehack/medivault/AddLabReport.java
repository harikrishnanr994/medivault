package com.carehack.medivault;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class AddLabReport extends AppCompatActivity {
    private Spinner dataSelectSpinner;
    private TextView labResultValueTV;
    private Button submitReportBtn;
    private String labName;
    private ArrayList<String> options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lab_report);
        options=new ArrayList<>();
        options.add("<Select Data>");
        options.add("Data Set 1");
        options.add("Data Set 2");
        options.add("Data Set 3");
        dataSelectSpinner=findViewById(R.id.dataSelectSpinner);
        labResultValueTV=findViewById(R.id.labResultValuesTV);
        submitReportBtn=findViewById(R.id.submitLabReportButton);
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
               String report=labResultValueTV.getText().toString();
               DataClass data=new DataClass();
               data.setContent(report);
               data.setTitle(labName);
                Calendar cal=Calendar.getInstance();
                data.setDate(cal.YEAR+" "+cal.MONTH+" "+cal.DAY_OF_MONTH);
            }
        });

    }
}
