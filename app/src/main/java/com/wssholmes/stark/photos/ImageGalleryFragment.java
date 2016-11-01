package com.wssholmes.stark.photos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by stark on 31/10/16.
 */

public class ImageGalleryFragment extends android.support.v4.app.Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{
    public static String LOG_TAG = ImageGalleryFragment.class.getSimpleName();

    private final int MY_PERMISSION_REQUEST_CAMERA = 0;
    private final int MY_PERMISSION_REQUEST_READ_IMAGES = 1;
    private Intent mIntentOpenCamera;

    private CoordinatorLayout mMediaScreen;
    private RecyclerView mImageRecyclerView;
    private ImageGalleryAdapter mImageGalleryAdapter;
    private TextView mImageGalleryEmptyView;

    private static final int IMAGE_GALLERY_CURSOR_ID = 100;
    private static final String[] IMAGE_COLUMNS = new String[]{
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };

    public static final int COLUMN_IMAGE = 0;
    public static final int COLUMN_DATE = 1;
    public static final int COLUMN_SIZE = 2;
    public static final int COLUMN_BUCKET_NAME = 3;

    private final static Uri mUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkReadingPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_gallery_layout, container, false);

        //Intent to be used for opening the camera;
        mIntentOpenCamera = new Intent(getActivity(), CameraActivity.class);
        mMediaScreen = (CoordinatorLayout) rootView.findViewById(R.id.media_screen);
        mImageGalleryEmptyView = (TextView) rootView.findViewById(R.id.image_gallery_empty_view);

        mImageRecyclerView = (RecyclerView) rootView.findViewById(R.id.image_gallery);
        mImageRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                getActivity().getResources().getInteger(R.integer.image_gallery_columns)));
        mImageGalleryAdapter = new ImageGalleryAdapter(getActivity());
        mImageRecyclerView.setAdapter(mImageGalleryAdapter);

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

    void checkReadingPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_READ_IMAGES);
        } else {
            getLoaderManager().initLoader(IMAGE_GALLERY_CURSOR_ID, null, this);
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
            case MY_PERMISSION_REQUEST_READ_IMAGES: {
                if(grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED){
                    getLoaderManager().initLoader(IMAGE_GALLERY_CURSOR_ID, null, this);
                } else {
                    Snackbar.make(mMediaScreen,
                            getString(R.string.reading_permission_denied),
                            Snackbar.LENGTH_INDEFINITE).show();
                }
                break;
            }
            default:
                Log.v(LOG_TAG, "Unknown permission requested");
        }
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Always remember this query.
        return new CursorLoader(getActivity(),
                mUri,
                IMAGE_COLUMNS,
                MediaStore.Images.Media.DATA + " like ? ",
                new String[] {"%Pictures/Photos%"},
                null
                );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            Log.v(LOG_TAG, "uri used to get the images " + mUri);
            Log.v(LOG_TAG, "total no. of images loaded " + data.getCount());
            mImageGalleryAdapter.swapCursor(data);
            mImageGalleryEmptyView.setVisibility(View.GONE);
        } else {
            mImageGalleryEmptyView.setVisibility(View.VISIBLE);
            Log.v(LOG_TAG, "cursor is empty");
        }

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }
}
