package jp.caliconography.one_liners.model.parseobject;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by abeharuhiko on 2014/10/28.
 */
@ParseClassName("Review")
public class Review extends ParseObject {

    public static final String KEY_PHOTO = "photo";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    private static final String KEY_SHARE_SCOPE = "shareScope";

    public void setPhotoFile(ParseFile file) {
        put(KEY_PHOTO, file);
    }

    public ParseFile getPhotoFile() {
        return getParseFile(KEY_PHOTO);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public void setAuthor(String author) {
        put(KEY_AUTHOR, author);
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
