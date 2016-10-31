package com.wssholmes.stark.photos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by stark on 31/10/16.
 */

public class ImageGalleryFragment extends android.support.v4.app.Fragment {
    public static String LOG_TAG = ImageGalleryFragment.class.getSimpleName();

    private final int MY_PERMISSION_REQUEST_CAMERA = 0;
    private Intent mIntentOpenCamera;

    private CoordinatorLayout mMediaScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_gallery_layout, container, false);

        //Intent to be used for opening the camera;
        mIntentOpenCamera = new Intent(getActivity(), CameraActivity.class);
        mMediaScreen = (CoordinatorLayout) rootView.findViewById(R.id.media_screen);

//        FAB for opening the camera.
        FloatingActionButton floatingActionButton =
                (FloatingActionButton) rootView.findViewById(R.id.open_camera);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });

        return rootView;

    }

    void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSION_REQUEST_CAMERA);
        } else {
            startActivity(mIntentOpenCamera);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    startActivity(mIntentOpenCamera);
                } else {
                    Snackbar.make(mMediaScreen,
                            getString(R.string.camera_permission_denied),
                            Snackbar.LENGTH_INDEFINITE).show();
                }
                break;
            }
            default:
                Log.v(LOG_TAG, "Unknown permission requested");
        }
    }
}
