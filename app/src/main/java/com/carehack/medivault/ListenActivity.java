package com.carehack.medivault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.chirp.sdk.CallbackCreate;
import io.chirp.sdk.CallbackRead;
import io.chirp.sdk.ChirpSDK;
import io.chirp.sdk.ChirpSDKListener;
import io.chirp.sdk.ChirpSDKStatusListener;
import io.chirp.sdk.model.Chirp;
import io.chirp.sdk.model.ChirpError;

public class ListenActivity extends AppCompatActivity {
    ChirpSDK chirpSDK;
    AVLoadingIndicatorView avLoadingIndicatorView;
    ProgressBar progressBar;
    ImageView imageView;
    TextView textView;
    private DatabaseReference mRef;
    String name,phone_intent,role;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Utils.pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        name = sharedPreferences.getString("name","");
        role = sharedPreferences.getString("role","");
        phone_intent = getIntent().getStringExtra("phone");
        setContentView(R.layout.activity_listen);
        avLoadingIndicatorView = findViewById(R.id.avi);
        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.textview);
        imageView = findViewById(R.id.imageView);
        mRef = FirebaseDatabase.getInstance().getReference();
        chirpSDK = new ChirpSDK(getApplicationContext(), getResources().getString(R.string.chirp_app_key), getResources().getString(R.string.chirp_app_secret), new ChirpSDKStatusListener() {
            @Override
            public void onAuthenticationSuccess() {
                Dexter.withActivity(ListenActivity.this)
                        .withPermission(
                                android.Manifest.permission.RECORD_AUDIO
                        ).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        textView.setText("Click on the button below to start Listening");
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imageView.setVisibility(View.GONE);
                                textView.setText("Listening... Please wait...");
                                avLoadingIndicatorView.setVisibility(View.VISIBLE);
                                recordChirp();
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        PermissionListener dialogPermissionListener =
                                DialogOnDeniedPermissionListener.Builder
                                        .withContext(ListenActivity.this)
                                        .withTitle("Record permission")
                                        .withMessage("Record permission is needed to authenticate using AudioQR")
                                        .withButtonText(android.R.string.ok)
                                        .withIcon(R.mipmap.ic_launcher)
                                        .build();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
            }

            @Override
            public void onChirpError(ChirpError chirpError) {

            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        chirpSDK.stop();
    }

    private void sendChirp(String receivedText, final String phone , final String message) throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", receivedText);
        jsonObject.put("phone", phone);
        jsonObject.put("type", "Ack");
        jsonObject.put("message", message);

        Chirp chirp = new Chirp(jsonObject);
        chirpSDK.create(chirp, new CallbackCreate() {
            @Override
            public void onCreateResponse(Chirp chirp) {
                chirpSDK.chirp(chirp);

                mRef.child("Users").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(role.equals("Laboratory"))
                        {
                            final Map<String, String> map = new HashMap<>();
                            map.put("Lab Name", name);
                            map.put("Name", dataSnapshot.child("Name").getValue(String.class));
                            mRef.child("Pending Reports").child("By Phone").child(phone).push().setValue(map, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    map.put("Phone", phone);
                                    map.remove("Lab Name");
                                    mRef.child("Pending Reports").child("By Name").child(name).push().setValue(map, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            startActivity(new Intent(ListenActivity.this, ViewPendingReportsActivity.class));
                                        }
                                    });
                                }
                            });
                        }
                        else if(role.equals("Doctor"))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(ListenActivity.this , DoctorPrescriptionActivity.class);
                                            intent.putExtra("phone" , phone);
                                            startActivity(intent);
                                        }
                                    },2000);
                                }
                            });
                        }
                }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCreateError(ChirpError chirpError) {
                Log.d("ChirpError", "onCreateError: " + chirpError.getMessage());
            }
        });
    }

    private void recordChirp() {
        chirpSDK.setListener(new ChirpSDKListener() {

            @Override
            public void onChirpHeard(final Chirp chirp) {
                chirpSDK.read(chirp, new CallbackRead() {
                    @Override
                    public void onReadResponse(final Chirp chirp) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("Playing...Please Wait..");
                                avLoadingIndicatorView.setVisibility(View.VISIBLE);
                                final String receivedText,phone,type;
                                try {
                                    receivedText = (String) chirp.getJsonData().get("text");
                                    phone = (String) chirp.getJsonData().get("phone");
                                    type = (String) chirp.getJsonData().get("type");
                                    Log.d("Phone",phone);
                                    Log.d("Type",type);

                                    if(phone_intent.equals(phone))
                                    {

                                    if(type.equals("Req")) {
                                        mRef.child("Shared Key").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String key = dataSnapshot.getValue(String.class);
                                                if (key != null) {
                                                    if (key.equals(receivedText)) {
                                                        try {
                                                            sendChirp(receivedText, phone,"Success");
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
                                        sendChirp(receivedText, phone , "Invalid Phone Number");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onReadError(ChirpError chirpError) {

                    }
                });

            }

            @Override
            public void onChirpHearStarted() {

            }

            @Override
            public void onChirpHearFailed() {
                avLoadingIndicatorView.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onChirpError(ChirpError chirpError) {
                Log.d("ChirpError",chirpError.toString());
                Log.d("ChirpError",chirpError.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setVisibility(View.VISIBLE);
                    }
                });
                Toast.makeText(getApplicationContext(),"QR Error",Toast.LENGTH_LONG).show();
            }
        });
        chirpSDK.start();
    }
}
