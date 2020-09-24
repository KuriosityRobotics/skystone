package org.firstinspires.ftc.teamcode.rework.AutoTools;

import org.firstinspires.ftc.teamcode.rework.util.auto.Point;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.firstinspires.ftc.teamcode.rework.util.auto.MathFunctions.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class MathFunctionsTest {
    @Test
    public void testAngleWrap() {
        assertEquals(
                (-0.6) * Math.PI,
                angleWrap(Math.PI * 7.4),
                0
        );

        assertEquals(
                0,
                angleWrap(0),
                0
        );

        assertEquals(
                -0.25 * Math.PI,
                angleWrap(1.75 * Math.PI),
                0
        );

    }

    @Test
    public void testLinePointDistance() {
        // TODO: Issue #54
//        assertEquals(
//                0,
//                lineSegmentPointDistance(new Point(0, 0), new Point(-1, 0), new Point(1, 0)),
//                0
//        );
//
//        assertEquals(
//                1,
//                lineSegmentPointDistance(new Point(1, 0), new Point(2, 0), new Point(2, 1)),
//                0
//        );
//
//        assertEquals(
//                Math.sqrt(8),
//                lineSegmentPointDistance(new Point(2, 3), new Point(3, 0), new Point(5, 2)),
//                1E-15
//        );
    }

    @Test
    public void testClosestPointOnLineToPoint() {
        assertEquals(
                new Point(0, 0),
                closestPointOnLineToPoint(new Point(0, 0), new Point(-1, -1), new Point(1, 1))
        );

        assertEquals(
                new Point(4, 1),
                closestPointOnLineToPoint(new Point(2, 3), new Point(3, 0), new Point(5, 2))
        );
    }

    @Test
    public void testTwoLineIntersectionPoint() {
        assertEquals(
                new Point(4, 1),
                twoLineIntersectionPoint(new Point(2, 3), -1, new Point(3, 0), 1)
        );

        assertEquals(
                new Point(-3, 0),
                twoLineIntersectionPoint(new Point(-3, 0), 1.0 / 2, new Point(-3, 0), -1.0 / 2)
        );

        assertEquals(
                new Point(),
                twoLineIntersectionPoint(new Point(-4, -2), 3.424342, new Point(-532, 344), 3.424342)
        ); // Parallel lines
    }

    @Test
    public void testSolveQuadratic() {
        assertArrayEquals(
                new double[]{1, -2},
                solveQuadratic(1, 1, -2),
                0
        );

        assertArrayEquals(
                new double[]{-1, -8},
                solveQuadratic(1, 9, 8),
                0
        );

        double[] test3 = new double[2];
        test3[0] = -2;
        assertArrayEquals(
                test3,
                solveQuadratic(2, 8, 8),
                0
        );
    }

    @Test
    public void testLineSegmentCircleIntersection() {
        // TODO: issue #54
//        ArrayList<Point> test1Points = new ArrayList<Point>();
//        test1Points.add(new Point(3, 0));
//        test1Points.add(new Point(-3, 0));
//        assertEquals(
//                test1Points,
//                lineSegmentCircleIntersection(new Point(0, 0), 3, new Point(-10, 0), new Point(10,0)));
//
//        ArrayList<Point> test2Points = new ArrayList<Point>();
//        test1Points.add(new Point(0,6));
//        test1Points.add(new Point(0,-6));
//        assertEquals(
//                test2Points,
//                lineSegmentCircleIntersection(new Point(0, 0), 6, new Point(0,100), new Point(0,-100)));
//
//        ArrayList<Point> test3Points = new ArrayList<>();
//        assertEquals(
//                test3Points,
//                lineSegmentCircleIntersection(new Point(324234, 12341234), 3424, new Point(-324, -2314), new Point(-234321, -13412342))
//        );
//
//        ArrayList<Point> test4Points = new ArrayList<>();
//        test3Points.add(new Point(-3, -2));
//        assertEquals(
//                test4Points,
//                lineSegmentCircleIntersection(new Point(-6, -5), 18, new Point(-4, 2), new Point(-2, -6))
//        );
//
//        ArrayList<Point> test5Points = new ArrayList<>();
//        assertEquals(
//                test5Points,
//                lineSegmentCircleIntersection(new Point(-12348,-234987), 18, new Point(-4, 2), new Point(-4, -2 - Double.MIN_VALUE))
//        );
    }
}
