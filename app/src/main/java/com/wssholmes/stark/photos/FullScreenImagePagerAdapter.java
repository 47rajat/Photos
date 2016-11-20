package com.wssholmes.stark.photos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by stark on 19/11/16.
 */

public class FullScreenImagePagerAdapter extends FragmentStatePagerAdapter {
    private static final String LOG_TAG = FullScreenImagePagerAdapter.class.getSimpleName();

    public static final String IMAGE_POSITION_KEY = "Image Position";
    private final int mTotalItems;

    public FullScreenImagePagerAdapter(FragmentManager fm, int totalitems) {
        super(fm);
        mTotalItems = totalitems;
    }

    @Override
    public Fragment getItem(int position) {
        FullScreenImageFragment fullScreenImageFragment = new FullScreenImageFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_POSITION_KEY, position);
        fullScreenImageFragment.setArguments(args);
        return fullScreenImageFragment;
    }

    @Override
    public int getCount() {
        return mTotalItems;
    }
}
