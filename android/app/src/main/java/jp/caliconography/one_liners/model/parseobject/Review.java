package jp.caliconography.one_liners.model.parseobject;

import android.widget.RelativeLayout;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by abeharuhiko on 2014/10/28.
 */
@ParseClassName("Review")
public class Review extends ParseObject {

    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_ORIGINAL_PHOTO = "original_photo";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    //    private static final String KEY_SHARE_SCOPE = "shareScope";
    public static final String KEY_PHOTO_WIDTH = "photoWidth";
    private static final String KEY_PHOTO_HEIGHT = "photoHeight";
    private static final String KEY_PAINT_CONFIGS = "paintConfigs";
    private static final String KEY_THUMBNAIL_URL = "thumnbnail_url";
    private static final String KEY_AFFILIATE_URL = "affiliate_url";
    private static final String KEY_QUOTE_MARK_POSITION = "quote_mark_position";

    public void setOriginalPhotoFile(ParseFile file) {
        put(KEY_ORIGINAL_PHOTO, file);
    }

    public ParseFile getOriginalPhotoFile() {
        return getParseFile(KEY_ORIGINAL_PHOTO);
    }

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

    public void setThumnnailUrl(String url) {
        put(KEY_THUMBNAIL_URL, url);
    }

    public String getThumbnailUrl() {
        return getString(KEY_THUMBNAIL_URL);
    }

    public void setAffiliateUrl(String url) {
        put(KEY_AFFILIATE_URL, url);
    }

    public String getAffiliateUrl() {
        return getString(KEY_AFFILIATE_URL);
    }

    public void setQuoteMarkPosition(QuoteMarkPosition position) {
        put(KEY_QUOTE_MARK_POSITION, position.getPositionInt());
    }

    public QuoteMarkPosition getQuoteMarkPosition() {
        return QuoteMarkPosition.valueOf(getInt(KEY_QUOTE_MARK_POSITION));
    }

    public void setPaintConfigs(ArrayList<ParseShapeConfig> paintConfigs) {
        put(KEY_PAINT_CONFIGS, paintConfigs);
    }

    public ArrayList<ParseShapeConfig> getPaintConfigs() {
        return (ArrayList<ParseShapeConfig>) get(KEY_PAINT_CONFIGS);
    }

    public boolean isEmpty() {
        return (getTitle() == null && getAuthor() == null && getPhotoFile() == null);
    }
//    public void setShareScope(ShareScope scope) {
//        put(KEY_SHARE_SCOPE, scope);
//    }
//
//    public enum ShareScope {
//        PUBLIC,
//        PRIVATE;
//    }

    public enum QuoteMarkPosition {
        LEFT(RelativeLayout.ALIGN_LEFT),
        RIGHT(RelativeLayout.ALIGN_RIGHT);

        private int position;

        QuoteMarkPosition(final int position) {
            this.position = position;
        }

        public int getPositionInt() {
            return position;
        }

        public static QuoteMarkPosition valueOf(int positionInt) {
            for (QuoteMarkPosition position : values()) {
                if (position.getPositionInt() == positionInt) {
                    return position;
                }
            }
            // getIntで0が返ってきた場合（＝このReviewが未保存の場合）デフォルト値
            return RIGHT;
        }
    }

}
