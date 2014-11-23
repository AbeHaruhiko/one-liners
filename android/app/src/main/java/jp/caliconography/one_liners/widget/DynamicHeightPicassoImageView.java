package jp.caliconography.one_liners.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

/**
 * Created by abeharuhiko on 2014/11/06.
 */
public class DynamicHeightPicassoImageView extends ImageView implements Target {

    private RequestCreator mRequest;
    private double mHeightRatio;
//    private String mImageUrl;

    public DynamicHeightPicassoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //    public DynamicHeightPicassoImageView(Context context, String imageUrl) {
//        super(context);
//        this.mImageUrl = imageUrl;
//    }
//
    public DynamicHeightPicassoImageView(Context context) {
        super(context);
    }

    //    public String getImageUrl() {
//        return mImageUrl;
//    }
//
//    public void setImageUrl(String url) {
//        this.mImageUrl = url;
//    }
//
    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int mode = MeasureSpec.getMode(widthMeasureSpec);
//        if (mode != MeasureSpec.EXACTLY) {
//            throw new IllegalStateException("layout_width must be match_parent");
//        }
//
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        // Honor aspect ratio for height but no larger than 2x width.
//        int height = Math.min((int) (width / mHeightRatio), width * 2);
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        //Load the image
//        Picasso.with(getContext()).setIndicatorsEnabled(true);  //only for debug tests
//        Picasso.with(getContext())
//                .load(mImageUrl)
//                .error(R.drawable.ic_error_loadingsmall)
//                .into(this);
//
//
//
//    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("layout_width must be match_parent");
        }
        if (mHeightRatio > 0.0) {
            // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);

//            //Load the image
//            if (mRequest != null) {
//                mRequest.resize(width, height).centerCrop().into((ImageView) this);
//                mRequest = null;
//            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void loadImage(String url) {
        //Load the image
        Picasso.with(getContext()).setIndicatorsEnabled(true);  //only for debug tests
        mRequest = Picasso.with(getContext()).load(url);

        //Load the image
        if (mRequest != null) {
            mRequest.into((Target) this);
            mRequest = null;
        }
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        mHeightRatio = 1d * bitmap.getHeight() / bitmap.getWidth();
        requestLayout();
        this.setImageBitmap(bitmap);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
