package jp.caliconography.one_liners.model.parseobject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by abeharuhiko on 2014/10/28.
 */
@ParseClassName("Review")
public class Review extends ParseObject {

    public static final String KEY_PHOTO = "photo";

    public Bitmap getPhoto() {
        byte[] bytes = getBytes(KEY_PHOTO);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void setPhoto(Bitmap bitmap) {
        put(KEY_PHOTO, bitmapToByte(bitmap));
    }

    public void setPhotoFile(ParseFile file) {
        put("file", file);
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
