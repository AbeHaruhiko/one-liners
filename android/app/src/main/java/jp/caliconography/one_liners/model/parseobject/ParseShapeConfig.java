package jp.caliconography.one_liners.model.parseobject;

import android.graphics.Matrix;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import jp.caliconography.one_liners.model.Paint;
import jp.caliconography.one_liners.model.PaintConfig;
import jp.caliconography.one_liners.model.ShapeConfig;

/**
 * Created by abeharuhiko on 2014/11/10.
 */
@ParseClassName("ParseShapeConfig")
abstract public class ParseShapeConfig extends ParseObject {
    private static final String KEY_TRANSLATE_X = "translateX";
    private static final String KEY_TRANSLATE_Y = "translateY";
    private static final String KEY_MATRIX = "matrix";
    private static final String KEY_PAINT = "paint";
    private static final String KEY_UNSCALED_STROKE_WIDTH = "unscaledStrokeWidth";
    private static final String KEY_COLOR = "color";

    public ParseShapeConfig setConfig(ShapeConfig config) throws JSONException {
        this.setConfig(config.getPaint(), config.getMatrix(), config.getTranslateX(), config.getTranslateY());
        return this;
    }

    void setConfig(Paint paint, Matrix matrix, float translateX, float translateY) throws JSONException {
        setPaint(paint);
        setMatrix(matrix);
        setTranslateX(translateX);
        setTranslateY(translateY);
    }

    public void setPaint(Paint paint) {
        // 線の太さと色のみ保存。（他の属性はデフォルト）
        put(KEY_UNSCALED_STROKE_WIDTH, paint.getUnScaledStrokeWidth().getWidthInt());
        put(KEY_COLOR, paint.getColor());
    }

    public Paint getPaint() throws JSONException {
        // 線の太さと色のみ取得。（他の属性はデフォルト）
        Paint paint = new Paint(true);
        paint.setUnScaledStrokeWidth(PaintConfig.StrokeWidth.valueOf(getInt(KEY_UNSCALED_STROKE_WIDTH)));
        paint.setColor(getInt(KEY_COLOR));
        return paint;
    }

    public void setMatrix(Matrix matrix) throws JSONException {
        float[] valueHolder = new float[9];
        matrix.getValues(valueHolder);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < valueHolder.length; i++) {
            jsonArray.put(valueHolder[i]);
        }
        put(KEY_MATRIX, jsonArray);
    }

    public Matrix getMatrix() throws JSONException {
        JSONArray jsonArray = getJSONArray(KEY_MATRIX);
        float[] valueHolder = new float[9];
        for (int i = 0; i < valueHolder.length; i++) {
            valueHolder[i] = new Double(jsonArray.getDouble(i)).floatValue();
        }
        Matrix matrix = new Matrix();
        matrix.setValues(valueHolder);
        return matrix;
    }

    public void setTranslateX(float translateX) {
        put(KEY_TRANSLATE_X, translateX);
    }

    public float getTranslateX() {
        return new Double(getDouble(KEY_TRANSLATE_X)).floatValue();
    }

    public void setTranslateY(float translateY) {
        put(KEY_TRANSLATE_Y, translateY);
    }

    public float getTranslateY() {
        return new Double(getDouble(KEY_TRANSLATE_Y)).floatValue();
    }
}
