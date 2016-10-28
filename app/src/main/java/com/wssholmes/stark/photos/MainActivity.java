package com.wssholmes.stark.photos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private final int MY_PERMISSION_REQUEST_CAMERA = 0;
    private Intent mIntentOpenCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntentOpenCamera = new Intent(this, CameraActivity.class);

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.open_camera);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });

    }

    void checkCameraPermission(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA},
                    MY_PERMISSION_REQUEST_CAMERA);
        } else {
            startActivity(mIntentOpenCamera);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST_CAMERA:
            {
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                   startActivity(mIntentOpenCamera);
                } else{
                    Snackbar.make(findViewById(R.id.home_screen),
                            getString(R.string.camera_permission_denied),
                            Snackbar.LENGTH_INDEFINITE).show();
                }
            }
            default:
                Log.v(LOG_TAG, "Unknown permission requested");
        }
    }

    @Override
    protected void onDestroy() {
        super.onPause();
    }
}
