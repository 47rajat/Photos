package com.wssholmes.stark.photos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by stark on 31/10/16.
 */

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ImageGalleryViewHolder> {

    private static final String LOG_TAG = ImageGalleryAdapter.class.getSimpleName();
    public static final String TOTAL_IMAGES_KEY = "Total number of images";
    public static final String CURRENT_IMAGE_POSITION_KEY = "Current image position key";

    private Cursor mCursor;
    private final Context mContext;

    public ImageGalleryAdapter(Context context){
        mContext = context;
    }


    @Override
    public ImageGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_gallery_item, parent, false);
            view.setFocusable(true);
            return new ImageGalleryViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to Recycler View");
        }
    }

    @Override
    public void onBindViewHolder(ImageGalleryViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        if(mCursor != null){
            Picasso.with(mContext)
                    .load(new File(mCursor.getString(ImageGalleryFragment.COLUMN_IMAGE)))
                    .resize(300, 450)
                    .centerCrop()
                    .into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    public class ImageGalleryViewHolder extends RecyclerView.ViewHolder{

        private ImageView mImageView;

        public ImageGalleryViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.single_photo);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent((Activity)mContext, FullScreenImageActivity.class);
                    intent.putExtra(TOTAL_IMAGES_KEY, mCursor.getCount());
                    intent.putExtra(CURRENT_IMAGE_POSITION_KEY, getAdapterPosition());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
