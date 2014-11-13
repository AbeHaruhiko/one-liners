package jp.caliconography.one_liners.model.parseobject;

import com.parse.ParseClassName;

import org.json.JSONException;

import jp.caliconography.one_liners.model.LineConfig;
import jp.caliconography.one_liners.model.ShapeConfig;

/**
 * Created by abeharuhiko on 2014/10/17.
 */
@ParseClassName("ParseLineConfig")
public class ParseLineConfig extends ParseShapeConfig {

    private static final String KEY_START_X = "startX";
    private static final String KEY_START_Y = "startY";
    private static final String KEY_END_X = "endX";
    private static final String KEY_END_Y = "endY";

    @Override
    public void setConfig(ShapeConfig config) throws JSONException {
        super.setConfig(config);
        LineConfig lineConfig = (LineConfig) config;
        setStartX(lineConfig.getStartX());
        setStartY(lineConfig.getStartY());
        setEndX(lineConfig.getEndX());
        setEndY(lineConfig.getEndY());
    }

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
