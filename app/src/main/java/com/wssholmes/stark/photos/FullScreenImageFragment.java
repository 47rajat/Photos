package com.wssholmes.stark.photos;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by stark on 19/11/16.
 */

public class FullScreenImageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = FullScreenImageFragment.class.getSimpleName();

    private static final int IMAGE_GALLERY_LOADER_ID = 0;

    private static final String[] IMAGE_COLUMNS = new String[] {
            MediaStore.Images.Media.DATA
    };

    public static final int COLUMN_IMAGE = 0;

    private final static Uri mUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private ImageView mImageView;
    private int mImagePosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args.containsKey(FullScreenImagePagerAdapter.IMAGE_POSITION_KEY)){
            mImagePosition = args.getInt(FullScreenImagePagerAdapter.IMAGE_POSITION_KEY);
        } else {
            mImagePosition = -1;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(IMAGE_GALLERY_LOADER_ID, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fullscreen_image, container, false);
        mImageView = (ImageView) rootView.findViewById(R.id.full_image_view);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                mUri,
                IMAGE_COLUMNS,
                MediaStore.Images.Media.DATA + " like ? ",
                new String[] {"%Pictures/Photos%"},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0){
            if (mImagePosition > -1){
                data.moveToPosition(mImagePosition);
                Picasso.with(getActivity())
                        .load(new File(data.getString(COLUMN_IMAGE)))
                        .resize(500, 500)
                        .centerCrop()
                        .into(mImageView);
            } else {
                Log.v(LOG_TAG, "not getting image position"); //TODO: Add UI element for this case.
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
