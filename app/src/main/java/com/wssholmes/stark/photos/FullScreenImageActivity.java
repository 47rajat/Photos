package com.wssholmes.stark.photos;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class FullScreenImageActivity extends AppCompatActivity {
    private static final String LOG_TAG = FullScreenImageActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private int mTotalImages;
    private int mCurrentImagePosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_activity);

        if(getIntent().hasExtra(ImageGalleryAdapter.TOTAL_IMAGES_KEY)){
            mTotalImages = getIntent().getIntExtra(ImageGalleryAdapter.TOTAL_IMAGES_KEY, -1);
        }

        if(getIntent().hasExtra(ImageGalleryAdapter.CURRENT_IMAGE_POSITION_KEY)){
            mCurrentImagePosition = getIntent().getIntExtra(ImageGalleryAdapter.CURRENT_IMAGE_POSITION_KEY, -1);
        }

        if(mTotalImages > -1 && mCurrentImagePosition > -1) {
            mViewPager = (ViewPager) findViewById(R.id.fullscreen_image_viewer);
            mViewPager.setAdapter(new FullScreenImagePagerAdapter(getSupportFragmentManager(), mTotalImages));
            mViewPager.setCurrentItem(mCurrentImagePosition);
        } else{
            Log.v(LOG_TAG, "not getting total images"); //TODO: Add a UI element to improve UX.
        }

    }
}
