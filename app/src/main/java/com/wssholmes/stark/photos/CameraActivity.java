package com.wssholmes.stark.photos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends AppCompatActivity {
    public static final String LOG_TAG = CameraActivity.class.getSimpleName();

    private final int MY_APP_WRITING_PERMISSION = 0;

    //TODO find a better way to get this string value
    private static final String MY_APP_NAME = "Photos";

    private static final int CAMERA_ID_BACK = 0;
    private static final int CAMERA_ID_FRONT = 1;

    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private CoordinatorLayout mPreview;
    private CoordinatorLayout mCameraScreen;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if(pictureFile == null){
                Log.d(LOG_TAG, "Error creating media file, check storage permissions");
            }

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(pictureFile);
                fileOutputStream.write(bytes);
                fileOutputStream.close();
            } catch (FileNotFoundException e){
                Log.d(LOG_TAG, "File not found: " + e.getMessage());
            } catch (IOException e){
                Log.d(LOG_TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    private OrientationEventListener mChangeCameraOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mCameraScreen = (CoordinatorLayout) findViewById(R.id.camera_screen);
        mPreview = (CoordinatorLayout) findViewById(R.id.camera_preview);

        mChangeCameraOrientation = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == ORIENTATION_UNKNOWN) return;
                android.hardware.Camera.CameraInfo info =
                        new android.hardware.Camera.CameraInfo();
                android.hardware.Camera.getCameraInfo(CAMERA_ID_BACK, info);
                orientation = (orientation + 45) / 90 * 90;
                int rotation = 0;
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    rotation = (info.orientation - orientation + 360) % 360;
                } else {  // back-facing camera
                    rotation = (info.orientation + orientation) % 360;
                }
//                Log.v(LOG_TAG, "rotation set: " + rotation);
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setRotation(rotation);
                mCamera.setParameters(parameters);
            }
        };

        FloatingActionButton captureButton = (FloatingActionButton) findViewById(R.id.capture_image);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkWritingPermission();
            }
        });

        //Create an instance of camera
//        mCamera = getCameraInstance();
//        if (mCamera != null) {
//            mCameraPreview = new CameraPreview(this, mCamera);
//            mPreview.addView(mCameraPreview);
//        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_APP_WRITING_PERMISSION:
            {
                if(grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED){
                    //get image from the camera
                    mCamera.takePicture(null, null, mPicture);
                } else {
                    Snackbar.make(mCameraScreen, getString(R.string.writing_permission_denied), Snackbar.LENGTH_INDEFINITE).show();
                }
                break;
            }
            default:
                Log.v(LOG_TAG, "Unkown permission requested");
        }
    }

    private void checkWritingPermission(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_APP_WRITING_PERMISSION);
        } else {
            //get image from the camera
            mCamera.takePicture(null, null, mPicture);
        }
    }

    public static Camera getCameraInstance(){
        Camera camera = null;
        Log.v(LOG_TAG, Camera.getNumberOfCameras() + "");

        //Always use this to prevent app from crashing if camera doesn't exists.
        try {
            //trying to get a Camera Instance.
            camera = Camera.open(CAMERA_ID_BACK);
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

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.v(LOG_TAG, "null file returned");
            return null;
        }
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), MY_APP_NAME);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreview.removeAllViews();
        //Create an instance of camera
        mCamera = getCameraInstance();

        mChangeCameraOrientation.enable();


        // set Camera parameters
        Camera.Parameters params = mCamera.getParameters();

        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        if (params.getMaxNumMeteringAreas() > 0){ // check that metering areas are supported
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();

            Rect areaRect1 = new Rect(-100, -100, 100, 100);    // specify an area in center of image
            meteringAreas.add(new Camera.Area(areaRect1, 600)); // set weight to 60%
            Rect areaRect2 = new Rect(800, -1000, 1000, -800);  // specify an area in upper right of image
            meteringAreas.add(new Camera.Area(areaRect2, 400)); // set weight to 40%
            params.setMeteringAreas(meteringAreas);
        }

        mCamera.setParameters(params);


        mCameraPreview = new CameraPreview(this, mCamera);
        mPreview.addView(mCameraPreview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mChangeCameraOrientation.disable();
        releaseCamera();
    }
}
