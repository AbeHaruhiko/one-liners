package jp.caliconography.one_liners.model;

/**
 * Created by abe on 2014/10/26.
 */
public class Paint extends android.graphics.Paint {

    private PaintConfig.StrokeWidth mUnScaledStrokeWidth;

    public Paint(boolean setDefalutValues) {
        super();
        if (setDefalutValues) {
            setAntiAlias(true);
            setDither(true);
            setColor(0x88cdcdcd);
            setStyle(android.graphics.Paint.Style.STROKE);
            setStrokeJoin(android.graphics.Paint.Join.ROUND);
            setStrokeCap(android.graphics.Paint.Cap.ROUND);
            setStrokeWidth(20);
        }
    }

    public Paint(PaintConfig.StrokeWidth unScaledStrokeWidth) {
        super();
        this.mUnScaledStrokeWidth = unScaledStrokeWidth;
    }

    public Paint(android.graphics.Paint paint) {
        super(paint);
    }

    public Paint(android.graphics.Paint paint, PaintConfig.StrokeWidth unScaledStrokeWidth) {
        super(paint);
        this.mUnScaledStrokeWidth = unScaledStrokeWidth;
    }

    public PaintConfig.StrokeWidth getUnScaledStrokeWidth() {
        return mUnScaledStrokeWidth;
    }

    public void setUnScaledStrokeWidth(PaintConfig.StrokeWidth unScaledStrokeWidth) {
        this.mUnScaledStrokeWidth = unScaledStrokeWidth;
    }
}
