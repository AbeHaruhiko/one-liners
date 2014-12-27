package jp.caliconography.one_liners.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by abeharuhiko on 2014/08/14.
 */
public class Utils {

    public static File getScreenShotFile(View view, Context context) {
        return saveImageToCacheDir(getScreenBitmap(view, context), context);
    }

    private static Bitmap getScreenBitmap(View view, Context context) {
        return getViewBitmap(view, context);
    }

    private static Bitmap getViewBitmap(View view, Context context) {
        view.setDrawingCacheEnabled(true);
        Bitmap cache = view.getDrawingCache();
        if (cache == null) {
            return null;
        }
//        Bitmap bitmap = Bitmap.createBitmap(cache);
        Bitmap bitmap = scaleDownBitmap(cache, 300, context);
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static Bitmap getShrinkedBitmap(InputStream inputStream, int maxHeight) {

        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inMutable = true;       // このbitmapをオフスクリーンバッファにしたい。= これを引数にcanvasを生成したい。mutableでないとnew Canvas(bitmap)で例外

        // 画像サイズ情報を取得する
        imageOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, imageOptions);
        float imageScaleWidth = (float) imageOptions.outWidth / maxHeight;
        float imageScaleHeight = (float) imageOptions.outHeight / maxHeight;

        // もしも、縮小できるサイズならば、縮小して読み込む
        if (imageScaleWidth > 2 && imageScaleHeight > 2) {
            imageOptions.inJustDecodeBounds = false;

            // 縦横、小さい方に縮小するスケールを合わせる
            int imageScale = (int) Math.floor((imageScaleWidth > imageScaleHeight ? imageScaleHeight : imageScaleWidth));

            // inSampleSizeには2のべき上が入るべきなので、imageScaleに最も近く、かつそれ以下の2のべき上の数を探す
            for (int i = 2; i <= imageScale; i *= 2) {
                imageOptions.inSampleSize = i;
            }

//                inputStream = getActivity().getContentResolver().openInputStream(result);
            return BitmapFactory.decodeStream(inputStream, null, imageOptions);
        } else {
            return BitmapFactory.decodeStream(inputStream);
        }
    }

    public static Bitmap scaleDownBitmap(Bitmap bitmap, int newHeight, Context context) {

        if (newHeight >= bitmap.getHeight()) {
            return bitmap;
        }

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * bitmap.getWidth() / ((double) bitmap.getHeight()));

        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);

        return bitmap;
    }

    public static File saveImageToCacheDir(Bitmap bmp, Context context) {
        String fileName = String.valueOf(System.currentTimeMillis()) + ".png";

        File file = new File(context.getExternalCacheDir(), fileName);
        FileOutputStream outputStream;

        try {

            outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, outputStream);

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromByteArray(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            return cm.getActiveNetworkInfo().isConnected();
        }
        return false;
    }

    public static boolean isOffline(Context context) {
        return !isOnline(context);
    }
}
