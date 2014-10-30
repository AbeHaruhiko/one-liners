package jp.caliconography.one_liners.event;

import android.graphics.Bitmap;

/**
 * Created by abe on 2014/10/30.
 */
public class PhotoBitmapGottenEvent {
    private final Bitmap mBitmap;

    public PhotoBitmapGottenEvent(final Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
