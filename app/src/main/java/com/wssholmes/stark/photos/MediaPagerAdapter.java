package com.wssholmes.stark.photos;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by stark on 31/10/16.
 */

public class MediaPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_TOTAL = 2;
    private static final String[] TAB_TITLES = new String[] {"IMAGES", "VIDEOS"};

    public MediaPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ImageGalleryFragment();
            case 1:
                return new VideoGalleryFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_TOTAL;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}
