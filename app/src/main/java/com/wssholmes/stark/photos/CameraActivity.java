package com.wssholmes.stark.photos;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class CameraActivity extends AppCompatActivity {
    public static final String LOG_TAG = CameraActivity.class.getSimpleName();

    private Camera mCamera;
    private CameraPreview mCameraPreview;
    CoordinatorLayout mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mPreview = (CoordinatorLayout) findViewById(R.id.camera_preview);

        //Create an instance of camera
//        mCamera = getCameraInstance();
//        if (mCamera != null) {
//            mCameraPreview = new CameraPreview(this, mCamera);
//            mPreview.addView(mCameraPreview);
//        }
    }

    public static Camera getCameraInstance(){
        Camera camera = null;
        Log.v(LOG_TAG, Camera.getNumberOfCameras() + "");

        //Always use this to prevent app from crashing if camera doesn't exists.
        try {
            //trying to get a Camera Instance.
            camera = Camera.open(0);
        } catch (Exception e){
            Log.v(LOG_TAG, "camera is not available");
        }
        return camera;
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreview.removeAllViews();
        //Create an instance of camera
        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        mPreview.addView(mCameraPreview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }
}
