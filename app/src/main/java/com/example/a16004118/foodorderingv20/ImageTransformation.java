package com.example.a16004118.foodorderingv20;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;

public class ImageTransformation {

    private static final String TAG = "ImageTransformation";

    public static Transformation getTransformation(final ImageView imageView) {
        return new Transformation() {

            @Override
            public Bitmap transform(final Bitmap source) {

                int targetWidth = imageView.getMeasuredWidthAndState();

                Log.e(TAG, "targetWidth: " + targetWidth);

                double aspectRatio = 0.66;

                Bitmap result = null;

                if (targetWidth > 0){
                    int targetHeight = (int) (targetWidth * aspectRatio);
                    result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                }else{
                    //实在没办法了 就这样凑活着吧
                    int targetHeight = (int) (313 * aspectRatio);
                    result = Bitmap.createScaledBitmap(source, 313, targetHeight, false);
                }

                Log.e(TAG, "result: " + result);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }

                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };
    }

}
