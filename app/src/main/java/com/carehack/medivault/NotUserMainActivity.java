package com.carehack.medivault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class NotUserMainActivity extends AppCompatActivity {

    EditText mPhoneView;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        mPhoneView = findViewById(R.id.edit_text_phone_number);
        submit = findViewById(R.id.btn_next);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptIntent();
            }
        });
    }

    private void attemptIntent() {

        mPhoneView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid phone
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_email));
            focusView = mPhoneView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Intent intent = new Intent(NotUserMainActivity.this , ListenActivity.class);
            intent.putExtra("phone","+91" + phone);
            startActivity(intent);
        }
    }

    private boolean isPhoneValid(String phone) {
        return phone.length() == 10;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences sharedPreferences = getSharedPreferences(Utils.pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (item.getItemId()) {
            case R.id.log_out:
                editor.clear();
                editor.commit();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(NotUserMainActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }
}
