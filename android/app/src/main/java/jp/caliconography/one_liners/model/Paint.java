package jp.caliconography.one_liners.model;

/**
 * Created by abe on 2014/10/26.
 */
public class Paint extends android.graphics.Paint {

    private PaintConfig.StrokeWidth mUnScaledStrokeWidth;

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
