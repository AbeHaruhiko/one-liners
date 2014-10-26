package jp.caliconography.one_liners.model;

import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by abeharuhiko on 2014/10/17.
 */
public class Line extends Shape {

    private final float mStartX;
    private final float mStartY;
    private final float mEndX;
    private final float mEndY;

    public Line(float mStartX, float mStartY, float mEndX, float mEndY, Paint mPaint, Matrix mMatrix, float mTranslateX, float mTranslateY) {
        super(mPaint, mMatrix, mTranslateX, mTranslateY);
        this.mStartX = mStartX;
        this.mStartY = mStartY;
        this.mEndX = mEndX;
        this.mEndY = mEndY;
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
