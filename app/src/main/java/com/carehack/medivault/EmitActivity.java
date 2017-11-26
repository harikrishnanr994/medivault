package com.carehack.medivault;

import android.*;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.chirp.sdk.CallbackCreate;
import io.chirp.sdk.CallbackRead;
import io.chirp.sdk.ChirpSDK;
import io.chirp.sdk.ChirpSDKListener;
import io.chirp.sdk.ChirpSDKStatusListener;
import io.chirp.sdk.model.Chirp;
import io.chirp.sdk.model.ChirpError;

public class EmitActivity extends AppCompatActivity {
    ChirpSDK chirpSDK;
    ProgressBar progressBar;
    ImageView imageView;
    TextView textView;
    String shared_key,phone;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Utils.pref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mRef = FirebaseDatabase.getInstance().getReference();

        shared_key = sharedPreferences.getString("sharedkey",null);
        phone = sharedPreferences.getString("phone",null);
        setContentView(R.layout.activity_emit);
        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.textview);
        imageView = findViewById(R.id.imageView);
        chirpSDK = new ChirpSDK(getApplicationContext(), getResources().getString(R.string.chirp_app_key), getResources().getString(R.string.chirp_app_secret), new ChirpSDKStatusListener() {
            @Override
            public void onAuthenticationSuccess() {
                Dexter.withActivity(EmitActivity.this)
                        .withPermissions(
                                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                                Manifest.permission.RECORD_AUDIO
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        progressBar.setVisibility(View.GONE);
                        if(report.areAllPermissionsGranted())
                        {
                            imageView.setVisibility(View.VISIBLE);
                            textView.setText("Click on the button below to pair with device");
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Animation animation = AnimationUtils.loadAnimation(EmitActivity.this, R.anim.shake);
                                        imageView.setAnimation(animation);
                                        imageView.setClickable(false);
                                        textView.setText("Sending Audio...");
                                        sendChirp();
                                    }catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        else
                        {
                            PermissionListener dialogPermissionListener =
                                    DialogOnDeniedPermissionListener.Builder
                                            .withContext(EmitActivity.this)
                                            .withTitle("Record permission")
                                            .withMessage("Record permission is needed to authenticate using AudioQR")
                                            .withButtonText(android.R.string.ok)
                                            .withIcon(R.mipmap.ic_launcher)
                                            .build();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

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


    private void sendChirp() throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", shared_key);
        jsonObject.put("type", "Req");
        jsonObject.put("phone", phone);
        Chirp chirp = new Chirp(jsonObject);
        chirpSDK.create(chirp, new CallbackCreate() {
            @Override
            public void onCreateResponse(Chirp chirp) {
                chirpSDK.chirp(chirp);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imageView.clearAnimation();
                                textView.setText("Waiting for Response");
                            }
                        },500);
                        recordChirp();
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
                                final String receivedText,type,message;
                                try {
                                    receivedText = (String) chirp.getJsonData().get("text");
                                    type = (String) chirp.getJsonData().get("type");
                                    message = (String) chirp.getJsonData().get("message");

                                    if(receivedText.equals(shared_key) && type.equals("Ack")) {
                                        if(message.equals("Success")) {
                                            SharedKeyManager sharedKeyManager = new SharedKeyManager(EmitActivity.this);
                                            sharedKeyManager.assignNewSharedKey(phone);
                                            Toast.makeText(getApplicationContext(), "Authentication Successful", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Invalid QR Code..Try Again", Toast.LENGTH_LONG).show();
                                        textView.setText("Click on the button below to pair with device");
                                        imageView.setClickable(true);
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
