package jp.caliconography.one_liners.model;

import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by abeharuhiko on 2014/10/17.
 */
public class Path {
    private final float mStartX;
    private final float mStartY;
    private final float mEndX;
    private final float mEndY;
    private final Paint mPaint;
    private final Matrix mMatrix;

    public Path(float startX, float startY, float endX, float endY, Paint path, Matrix matrix) {
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

    public Matrix getmMatrix() {
        return mMatrix;
    }
}
