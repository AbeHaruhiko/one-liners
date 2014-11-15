package jp.caliconography.one_liners.model;

import android.graphics.Matrix;

import org.json.JSONException;

import jp.caliconography.one_liners.model.parseobject.ParseLineConfig;

/**
 * Created by abeharuhiko on 2014/10/17.
 */
public class LineConfig extends ShapeConfig {

    private final float mStartX;
    private final float mStartY;
    private final float mEndX;
    private final float mEndY;

    public LineConfig(float mStartX, float mStartY, float mEndX, float mEndY, Paint mPaint, Matrix mMatrix, float mTranslateX, float mTranslateY) {
        super(mPaint, mMatrix, mTranslateX, mTranslateY);
        this.mStartX = mStartX;
        this.mStartY = mStartY;
        this.mEndX = mEndX;
        this.mEndY = mEndY;
    }

    public LineConfig(ParseLineConfig config) throws JSONException {
        this(config.getStartX(),
                config.getStartY(),
                config.getEndX(),
                config.getEndY(),
                config.getPaint(),
                config.getMatrix(),
                config.getTranslateX(),
                config.getTranslateY());
    }

    public float getStartX() {
        return mStartX;
    }

    public float getStartY() {
        return mStartY;
    }

    public float getEndX() {
        return mEndX;
    }

    public float getEndY() {
        return mEndY;
    }
}
