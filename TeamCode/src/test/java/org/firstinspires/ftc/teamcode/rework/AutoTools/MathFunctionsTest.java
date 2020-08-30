package org.firstinspires.ftc.teamcode.rework.AutoTools;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.rework.AutoTools.MathFunctions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class MathFunctionsTest {
    @Test
    public void testAngleWrap() {
        assertEquals(angleWrap(Math.PI * 7.4), (-0.6) * Math.PI, 0);
        assertEquals(angleWrap(0), 0, 0);
        assertEquals(angleWrap(1.75 * Math.PI), -0.25 * Math.PI, 0);
    }

    @Test
    public void testLinePointDistance() {
        assertEquals(linePointDistance(new Point(0,0), new Point(-1,0), new Point(1,0)), 0, 0);
        assertEquals(linePointDistance(new Point(1,0), new Point(2,0), new Point(2,1)), 1, 0);
        assertEquals(linePointDistance(new Point(2,3), new Point(3,0), new Point(5,2)), Math.sqrt(8), 1E-15);
    }

    @Test
    public void testClosestPointOnLineToPoint() {
        assertEquals(closestPointOnLineToPoint(new Point(0,0), new Point(-1,-1), new Point(1,1)), new Point(0,0));
    }

    @Test
    public void testTwoLineIntersectionPoint() {
        Point point = twoLineIntersectionPoint(new Point(-2, 6), 69, new Point(4,20), 420);
        assertTrue(point.x == 5.139601139601139);
        assertTrue(point.y == 498.63247863247864);
    }

    @Test
    public void testSolveQuadratic() {
        double[] roots = solveQuadratic(1, 1, -2);
        assertTrue(roots[0] == 1);
        assertTrue(roots[1] == -2);
    }

    @Test
    public void testLineCircleIntersection() {
        ArrayList<Point> test1Points = new ArrayList<Point>();
        test1Points.add(new Point(2.5240998703626616, 5.048199740725323));
        assertEquals(
                lineCircleIntersection(new Point(-1,-1), 7, new Point(3,6), new Point(-3,-6)),
                test1Points
        );
    }
}
