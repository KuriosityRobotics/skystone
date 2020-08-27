package org.firstinspires.ftc.teamcode.rework.AutoTools;

import org.firstinspires.ftc.teamcode.rework.AutoTools.Point;
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
        assertTrue(angleWrap(Math.PI * 7.4) == 1.4 * Math.PI);
    }

    @Test
    public void testLinePointDistance() {
        assertTrue(linePointDistance(new Point(1, 1), new Point(2, 2), new Point(4, 3)) == 0.4472135954999579);
    }

    @Test
    public void testClosestPointOnLineToPoint() {
        Point point = closestPointOnLineToPoint(new Point(64, 23), new Point(41, 64), new Point(98,12));
        assertTrue(point.x == 73.96673945909627);
        assertTrue(point.y == 33.92507979170165);
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
