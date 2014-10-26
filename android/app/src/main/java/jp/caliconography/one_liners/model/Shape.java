package jp.caliconography.one_liners.model;

import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by abeharuhiko on 2014/10/17.
 */
abstract public class Shape {
    private Paint mPaint = null;
    private Matrix mMatrix;
    private float mTranslateX = 0;
    private float mTranslateY = 0;

    protected Shape(Paint mPaint, Matrix mMatrix, float mTranslateX, float mTranslateY) {
        this.mPaint = mPaint;
        this.mMatrix = mMatrix;
        this.mTranslateX = mTranslateX;
        this.mTranslateY = mTranslateY;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public float getTranslateX() {
        return mTranslateX;
    }

    public float getTranslateY() {
        return mTranslateY;
    }
}
