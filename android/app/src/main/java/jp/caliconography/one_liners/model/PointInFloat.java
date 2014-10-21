package jp.caliconography.one_liners.model;

/**
 * Created by abeharuhiko on 2014/10/21.
 */
public class PointInFloat {
    public float x;
    public float y;

    public PointInFloat(float x, float y) {
        this.x = x;
        this.y = y;
    }


    double getAbs() {
        return Math.sqrt(x * x + y * y);
    }

    PointInFloat difference(PointInFloat p2) {
        return new PointInFloat(x - p2.x, y - p2.y);
    }

    PointInFloat sum(PointInFloat p2) {
        return new PointInFloat(x + p2.x, y + p2.y);
    }

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }

    /**
     * 中点を求める。
     *
     * @param p1 座標1
     * @param p2 座標2
     * @return 中点座標
     */
    public static PointInFloat getMidpoint(PointInFloat p1, PointInFloat p2) {

        PointInFloat midpoint = new PointInFloat((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        return midpoint;
    }

    /**
     * 2点間の距離を求める。
     *
     * @param p1 座標1
     * @param p2 座標2
     * @return 距離
     */
    public static double getDistance(PointInFloat p1, PointInFloat p2) {
        PointInFloat dist = p1.difference(p2);

        return dist.getAbs();
    }
}
