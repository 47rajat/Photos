package com.wssholmes.stark.photos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by stark on 31/10/16.
 */

public class VideoGalleryFragment extends android.support.v4.app.Fragment {
    public static final String LOG_TAG = VideoGalleryFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_gallery_layout,container,false);

        return rootView;
    }
}
