package jp.caliconography.one_liners.model.parseobject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ProgressCallback;

import java.io.ByteArrayOutputStream;

import jp.caliconography.one_liners.event.PhotoBitmapGottenEvent;
import jp.caliconography.one_liners.util.BusHolder;

/**
 * Created by abeharuhiko on 2014/10/28.
 */
@ParseClassName("Review")
public class Review extends ParseObject {

    public static final String KEY_PHOTO = "photo";

    public void setPhoto(Bitmap bitmap) {
        put(KEY_PHOTO, bitmapToByte(bitmap));
    }

    public Bitmap getPhoto() {
        byte[] bytes = getBytes(KEY_PHOTO);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void setPhotoFile(ParseFile file) {
        put("file", file);
    }

    public void getPhotoBitmapInBackground() {
        getParseFile("file").getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null) {
                    BusHolder.get().post(new PhotoBitmapGottenEvent(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
                } else {

                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer integer) {

            }
        });
    }

    private static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
