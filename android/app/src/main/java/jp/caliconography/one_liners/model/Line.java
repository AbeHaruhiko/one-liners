package jp.caliconography.one_liners.model;

import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by abeharuhiko on 2014/10/17.
 */
public class Line {
    private final float mStartX;
    private final float mStartY;
    private final float mEndX;
    private final float mEndY;
    private final Paint mPaint;
    private final Matrix mMatrix;
    private float mTranslateX = 0;
    private float mTranslateY = 0;

    public Line(float startX, float startY, float endX, float endY, Paint path, Matrix matrix) {
        this.mStartX = startX;
        this.mStartY = startY;
        this.mEndX = endX;
        this.mEndY = endY;
        this.mPaint = path;
        this.mMatrix = matrix;
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

    public Paint getPaint() {
        return mPaint;
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public float getmTranslateX() {
        return mTranslateX;
    }

    public void setmTranslateX(float mTranslateX) {
        this.mTranslateX = mTranslateX;
    }

    public float addTranslateX(float value) {
        return this.mTranslateX += value;
    }

    public float getTranslateY() {
        return mTranslateY;
    }

    public void setTranslateY(float translateY) {
        this.mTranslateY = translateY;
    }

    public float addTranslateY(float value) {
        return this.mTranslateY += value;
    }
}