package org.firstinspires.ftc.teamcode.rework.Robot.pathfinding.geometry;

public class Polygon {

    private Vector[] points;
    private Vector[] axes;

    /**
     * This class represents a shape, composed of multiple vectors.
     * @param points
     */
    public Polygon(Vector... points) {
        this.points = points;
        axes = new Vector[points.length];
        Vector v;
        for (int i = 0; i < points.length; ++i) {
            if (i < points.length - 1) {
                v = points[i].subtract(points[i + 1]);
                v.normal();
            } else {
                v = points[i].subtract(points[0]);
                v.normal();
            }
            axes[i] = v;
        }

    }

    /**
     * @param bounds
     * @return true if the Vector provided is outside the bounds of this polygon
     */
    public boolean outsideBounds(Vector bounds) {
        for (Vector p : points)
            if (p.x >= bounds.x || p.x <= 0 || p.y >= bounds.y || p.y <= 0)
                return true;
        return false;
    }

    /**
     *
     * @param other
     * @return true if this Polygon intersects with the Polygon provided
     */
    public boolean intersects(Polygon other) {
        Vector[] axes = getAxes();
        for (int i = 0; i < axes.length; ++i) {
            double min1 = getMin(axes[i]);
            double max1 = getMax(axes[i]);
            double min2 = other.getMin(axes[i]);
            double max2 = other.getMax(axes[i]);
            if (!(max1 >= min2 && max2 >= min1)) {
                return false;
            }
        }
        Vector[] otherAxes = other.getAxes();
        for (int i = 0; i < otherAxes.length; ++i) {
            double min1 = getMin(otherAxes[i]);
            double max1 = getMax(otherAxes[i]);
            double min2 = other.getMin(otherAxes[i]);
            double max2 = other.getMax(otherAxes[i]);
            if (!(max1 >= min2 && max2 >= min1)) {
                return false;
            }
        }
        return true;
    }

    public Vector[] getAxes() {
        return axes;
    }

    public double getMin(Vector axis) {
        double min = points[0].dotProduct(axis);
        for (int i = 1; i < points.length; ++i) {
            min = Math.min(min, points[i].dotProduct(axis));
        }
        return min;
    }

    public double getMax(Vector axis) {
        double max = points[0].dotProduct(axis);
        for (int i = 1; i < points.length; ++i) {
            max = Math.max(max, points[i].dotProduct(axis));
        }
        return max;
    }

    /**
     *
     * @param center
     * @return the closest vertex to the provided location
     */
    public Vector getClosestVertex(Vector center) {
        double d = center.squareDistance(points[0]);
        Vector min = points[0];
        for (int i = 1; i < points.length; ++i) {
            double temp = center.squareDistance(points[i]);
            if (temp < d) {
                d = temp;
                min = points[i];
            }
        }
        return min;
    }


    public Vector closestPoint(Vector p) {
        Vector closest = closestPointOnSegment(points[0], points[points.length - 1], p);
        double d2 = p.squareDistance(closest);
        for (int i = 0; i < points.length - 1; ++i) {
            Vector temp = closestPointOnSegment(points[i], points[i + 1], p);
            double tempd2 = p.squareDistance(temp);
            if (tempd2 < d2) {
                closest = temp;
                d2 = tempd2;
            }
        }
        return closest;
    }

    private static Vector closestPointOnSegment(Vector v1, Vector v2, Vector p) {
        double d2 = v1.squareDistance(v2);
        double t = p.subtract(v1).dotProduct(v2.subtract(v1)) / d2;
        if (t <= 0)
            return v1;
        if (t >= 1)
            return v2;
        return v1.add(v2.subtract(v1).multiply(t));
    }

    public Vector[] getPossibleNodes(double radius) {
        Vector[] nodes = new Vector[points.length * 2];
        int j = 0;
        for (int i = 0; i < points.length; ++i) {
            Vector p = points[i];
            Vector p1, p2;
            if (i == 0)
                p1 = points[points.length - 1];
            else
                p1 = points[i - 1];
            if (i == points.length - 1)
                p2 = points[0];
            else
                p2 = points[i + 1];
            Vector d1 = p1.subtract(p).multiply(1 / p1.distance(p));
            Vector d2 = p2.subtract(p).multiply(1 / p2.distance(p));
            double d = radius / Math.sqrt((1 - d1.dotProduct(d2)) / 2);
            Vector v = d1.add(d2);
            v = v.multiply(d / v.magnitude());
            nodes[j++] = p.add(v);
            nodes[j++] = p.subtract(v);
        }
        return nodes;
    }

    public Polygon flipPolygonX(double line) {
        Vector[] flippedPoints = new Vector[points.length];
        for (int i = 0; i < points.length; i++) {
            flippedPoints[i] = new Vector(2 * line - points[i].x, points[i].y);
        }
        return new Polygon(flippedPoints);
    }

    public Polygon flipPolygonY(double line) {
        Vector[] flippedPoints = new Vector[points.length];
        for (int i = 0; i < points.length; i++) {
            flippedPoints[i] = new Vector(points[i].x, 2 * line - points[i].y);
        }
        return new Polygon(flippedPoints);
    }
}