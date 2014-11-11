package jp.caliconography.one_liners.model.parseobject;

/**
 * Created by abeharuhiko on 2014/10/17.
 */
public class LineConfig extends ShapeConfig {

    private static final String KEY_START_X = "startX";
    private static final String KEY_START_Y = "startY";
    private static final String KEY_END_X = "endX";
    private static final String KEY_END_Y = "endY";

    public void setStartX(float startX) {
        put(KEY_START_X, startX);
    }

    public float getStartX() {
        return getInt(KEY_START_X);
    }

    public void setStartY(float startY) {
        put(KEY_START_Y, startY);
    }

    public float getStartY() {
        return getInt(KEY_START_Y);
    }

    public void setEndX(float endX) {
        put(KEY_END_X, endX);
    }

    public float getEndX() {
        return getInt(KEY_END_X);
    }

    public void setEndY(float endY) {
        put(KEY_END_Y, endY);
    }

    public float getEndY() {
        return getInt(KEY_END_Y);
    }
}
