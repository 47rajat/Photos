package com.wssholmes.stark.photos;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by stark on 31/10/16.
 */

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ImageGalleryViewHolder> {

    public static final String LOG_TAG = ImageGalleryAdapter.class.getSimpleName();

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
            holder.mImageView.setImageBitmap(
                    //Used to downsize the bitmap to reduce load on the UI.
                    decodeSampledBitmapFromResource(mCursor.getString(ImageGalleryFragment.COLUMN_IMAGE), 200, 200)
            );
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
        }
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    //TODO: FIND A BETTER WAY TO LOAD BITMAPS, NOT WORKING EFFICIENTLY RIGHT NOW.HINT:USE PICASSO.
    //Method for downsizing bitmaps.
    public static Bitmap decodeSampledBitmapFromResource(String filePath,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
