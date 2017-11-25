package com.carehack.medivault;

import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.List;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        avLoadingIndicatorView = findViewById(R.id.avi);
        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.textview);
        imageView = findViewById(R.id.imageView);
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
                        textView.setText("Click on the button below to start recording");
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imageView.setVisibility(View.GONE);
                                textView.setText("Recording Audio...");
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
                                avLoadingIndicatorView.setVisibility(View.INVISIBLE);
                                final String receivedText;
                                try {
                                    receivedText = (String) chirp.getJsonData().get("text");
                                    Toast.makeText(getApplicationContext(),receivedText,Toast.LENGTH_LONG).show();
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
                imageView.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.setVisibility(View.INVISIBLE);
            }
        });
        chirpSDK.start();
    }
}
