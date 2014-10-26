package jp.caliconography.one_liners.model;

/**
 * Created by abe on 2014/10/25.
 */
public class PaintConfig {

    private Color mColor = Color.RED;
    private StrokeWidth mStrokeWidth = StrokeWidth.MID;
    private StrokeBorderWidth mStrokeBorderWidth = StrokeBorderWidth.MID;
    private ShapeType mShapeType = ShapeType.LINE;

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color color) {
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

    public enum Color {
        RED(android.graphics.Color.RED),
        GREEN(android.graphics.Color.GREEN),
        BLUE(android.graphics.Color.BLUE),
        BLACK(android.graphics.Color.BLACK);

        private int color;

        private Color(int color) {
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

        private StrokeWidth(int width) {
            this.width = width;
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

        LINE(Line.class),
        RECTANGLE(Rectangle.class);

        private Class shapeType;

        private ShapeType(Class<? extends Shape> shapeType) {
            this.shapeType = shapeType;
        }
    }
}
