package jp.caliconography.one_liners.model.parseobject;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONArray;

/**
 * Created by abeharuhiko on 2014/10/28.
 */
@ParseClassName("Review")
public class Review extends ParseObject {

    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    //    private static final String KEY_SHARE_SCOPE = "shareScope";
    public static final String KEY_PHOTO_WIDTH = "photoWidth";
    private static final String KEY_PHOTO_HEIGHT = "photoHeight";
    private static final String KEY_PAINT_CONFIGS = "paintConfigs";

    public void setPhotoFile(ParseFile file) {
        put(KEY_PHOTO, file);
    }

    public ParseFile getPhotoFile() {
        return getParseFile(KEY_PHOTO);
    }

    public void setPhotoFileWidth(int width) {
        put(KEY_PHOTO_WIDTH, width);
    }

    public int getPhotoFileWidth() {
        return (Integer) get(KEY_PHOTO_WIDTH);
    }

    public void setPhotoFileHeight(int height) {
        put(KEY_PHOTO_HEIGHT, height);
    }

    public int getPhotoFileHeight() {
        return (Integer) get(KEY_PHOTO_HEIGHT);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setAuthor(String author) {
        put(KEY_AUTHOR, author);
    }

    public String getAuthor() {
        return getString(KEY_AUTHOR);
    }

    public void setPaintConfigs(JSONArray paintConfigs) {
        put(KEY_PAINT_CONFIGS, paintConfigs);
    }

    public JSONArray getPaintConfigs() {
        return getJSONArray(KEY_PAINT_CONFIGS);
    }

//    public void setShareScope(ShareScope scope) {
//        put(KEY_SHARE_SCOPE, scope);
//    }
//
//    public enum ShareScope {
//        PUBLIC,
//        PRIVATE;
//    }

}
