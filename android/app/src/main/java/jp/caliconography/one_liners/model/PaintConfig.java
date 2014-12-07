package jp.caliconography.one_liners.model;

/**
 * Created by abe on 2014/10/25.
 */
public class PaintConfig {

    private StrokeColor mColor = StrokeColor.RED;
    private StrokeWidth mStrokeWidth = StrokeWidth.MID;
    private StrokeBorderWidth mStrokeBorderWidth = StrokeBorderWidth.MID;
    private ShapeType mShapeType = ShapeType.LINE;

    public StrokeColor getColor() {
        return mColor;
    }

    public void setColor(StrokeColor color) {
        this.mColor = color;
    }

    public ShapeType getShapeType() {
        return mShapeType;
    }

    public void setShapeType(ShapeType shapeType) {
        this.mShapeType = shapeType;
    }

    public StrokeWidth getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(StrokeWidth strokeWidth) {
        this.mStrokeWidth = strokeWidth;
    }

    public StrokeBorderWidth getStrokeBorderWidth() {
        return mStrokeBorderWidth;
    }

    public void setStrokeBorderWidth(StrokeBorderWidth strokeBorderWidth) {
        this.mStrokeBorderWidth = strokeBorderWidth;
    }

    public enum StrokeColor {
        RED(android.graphics.Color.RED),
        GREEN(android.graphics.Color.GREEN),
        BLUE(android.graphics.Color.BLUE),
        BLACK(android.graphics.Color.BLACK);

        private int color;

        private StrokeColor(int color) {
            this.color = color;
        }

        public int getColorInt() {
            return this.color;
        }
    }

    public enum StrokeWidth {
        THIN(4),
        MID(8),
        FAT(16);

        private int width;

        StrokeWidth(final int width) {
            this.width = width;
        }

        public int getWidthInt() {
            return width;
        }

        public static StrokeWidth valueOf(int widthInt) {
            for (StrokeWidth strokeWidth : values()) {
                if (strokeWidth.getWidthInt() == widthInt) {
                    return strokeWidth;
                }
            }
            throw new IllegalArgumentException("StrokeWidthに不正な値が指定されました。(" + widthInt + ")");
        }
    }

    public enum StrokeBorderWidth {
        THIN(1),
        MID(2),
        FAT(4);

        private int width;

        private StrokeBorderWidth(int width) {
            this.width = width;
        }
    }

    public enum ShapeType {

        LINE(LineConfig.class),
        RECTANGLE(RectangleConfig.class);

        private Class shapeType;

        private ShapeType(Class<? extends ShapeConfig> shapeType) {
            this.shapeType = shapeType;
        }
    }
}
