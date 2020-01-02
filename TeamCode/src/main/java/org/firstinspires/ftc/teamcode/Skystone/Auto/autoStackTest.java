package org.firstinspires.ftc.teamcode.Skystone.Auto;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Skystone.MotionProfiler.Point;
import org.firstinspires.ftc.teamcode.Skystone.Robot;
import org.firstinspires.ftc.teamcode.Skystone.Vision;

import java.util.HashMap;

@Autonomous(name = "autoStackTest", group = "LinearOpmode")
public class autoStackTest extends AutoBase {
    @Override
    public void runOpMode() {
        long startTime;
        initLogic();

        waitForStart();
        startTime = SystemClock.elapsedRealtime();

        // Positions assuming center Skystone
        double firstSkystoneY = -2;
        double secondSkyStoneY = -16;
        double secondSkyStoneX = 41;
        double thirdStoneY = -25;
        double thirdStoneX = 35.5;
        double anglelock = 33;

        Vision.Location skystoneLocation = Vision.Location.UNKNOWN;
        try {
            skystoneLocation = vision.runDetection(true, true);
        } catch (Exception e) {

        }

        telemetry.addLine("Detection Result: " + skystoneLocation.toString());
        telemetry.update();

        sleep(250);

        position2D.startOdometry();

        // Change Skystone positions if detected left or right
        if (skystoneLocation == Vision.Location.LEFT) {
            firstSkystoneY = -4;
            secondSkyStoneY = -25;
            secondSkyStoneX = 43;
            anglelock = 30;
            thirdStoneX = 45;
            thirdStoneY = -27.5;
        } else if (skystoneLocation == Vision.Location.RIGHT) {
            firstSkystoneY = 4.5;
            secondSkyStoneY = -8;
            secondSkyStoneX = 41;
            anglelock = 33;
            thirdStoneX = 33;
            thirdStoneY = -21;
        }

        double[][] toFirstStone = {
                {0, 0, 10, 0},
                {10, firstSkystoneY, 10, 0},
                {48, firstSkystoneY, 10, 0}};
        HashMap<Point, Robot.Actions> toFirstStoneActions = new HashMap<Point, Robot.Actions>() {{
            put(new Point(0, 0), Robot.Actions.START_INTAKE);
        }};

        double[][] toFoundation = {
                toFirstStone[toFirstStone.length - 1],
                {34, firstSkystoneY+5, 0, 10},
                {28, 17, -10, 20},
                {24, 20, -10, 20},
                {24, 30, -10, 20},
                {23, 43, -10, 20},
                {22, 55, 0, 20},
                {22, 67, 0, 20},
                {22, 68, 0, 20},
                {21, 74, 0, 20},
                {31, 85, 0, 10}};
        HashMap<Point, Robot.Actions> toFoundationActions = new HashMap<Point, Robot.Actions>() {{
            put(new Point(24, 20), Robot.Actions.RAISE_OUTTAKE_LEVEL1);
            put(new Point(24, 45), Robot.Actions.STOP_INTAKE);
        }};

        double[][] toSecondStone = {
                {toFoundation[toFoundation.length - 1][0], toFoundation[toFoundation.length - 1][1], -10, 0},
                {9, 63, 10, 0},
                {5, 60, 10, 0},
                {20, 61, -10, 0},
                {20, 29, 0, -10},
                {18, secondSkyStoneY + 5, 0, 10},
                {secondSkyStoneX, secondSkyStoneY, 30, 0},
                {secondSkyStoneX-5, secondSkyStoneY-5, 30, 0}};
        HashMap<Point, Robot.Actions> toSecondStoneActions = new HashMap<Point, Robot.Actions>() {{
            put(new Point(22, 65), Robot.Actions.LOWER_OUTTAKE);
            put(new Point(10, 70), Robot.Actions.RELEASE_FOUNDATION);
            put(new Point(28, -10), Robot.Actions.START_INTAKE);
        }};

        double[][] toDepositSecondStone = {
                toFirstStone[toFirstStone.length - 1],
                {34, firstSkystoneY+5, 0, 10},
                {28, 17, -10, 20},
                {24, 20, -10, 20},
                {24, 30, -10, 20},
                {23, 43, -10, 20},
                {22, 55, 0, 20},
                {22, 67, 0, 20},
                {22, 68, 0, 20},
                {21, 74, 0, 20},
                {31, 85, 0, 10}};
        HashMap<Point, Robot.Actions> toDepositSecondStoneActions = new HashMap<Point, Robot.Actions>() {{
            put(new Point(24, 20), Robot.Actions.RAISE_OUTTAKE_LEVEL2);
            put(new Point(24, 45), Robot.Actions.STOP_INTAKE);
        }};

        final double[][] toThirdStone = {
                toDepositSecondStone[toDepositSecondStone.length - 1],
                {22, 63, 5, 10},
                {21, 60, 0, -10},
                {21, 30, 0, -10},
                {21, 10, 0, -10},
                {32, 6, 0, -10},
                {thirdStoneX, thirdStoneY, 10, 0}};
        HashMap<Point, Robot.Actions> toThirdStoneActions = new HashMap<Point, Robot.Actions>() {{
            put(new Point(22, 58), Robot.Actions.RETRACT_OUTTAKE);
            put(new Point(28, 30), Robot.Actions.START_INTAKE);
        }};

        double[][] toDepositThirdStone = {
                toThirdStone[toThirdStone.length - 1],
                {27, -15, 0, -10},
                {22, 29, 0, 20},
                {19, 61, 0, 10},
                {19, 65, 0, 10}};
        HashMap<Point, Robot.Actions> toParkAfterThirdStoneActions = new HashMap<Point, Robot.Actions>() {{
            put(new Point(23, 10), Robot.Actions.EXTEND_OUTTAKE);
            put(new Point(toThirdStone[toThirdStone.length - 1][0] - 15, toThirdStone[toThirdStone.length - 1][1] + 20), Robot.Actions.STOP_INTAKE);
            put(new Point(21, 20), Robot.Actions.START_INTAKE);
        }};

        double[][] toPark = {
                {toDepositThirdStone[toDepositThirdStone.length - 1][0], toDepositThirdStone[toDepositThirdStone.length - 1][1], 0, -10},
                {20, 55, 0, -10},
                {19, 34, 0, -10}};
        HashMap<Point, Robot.Actions> toParkActions = new HashMap<Point, Robot.Actions>() {{
            put(new Point(25, 65), Robot.Actions.RETRACT_OUTTAKE);
        }};

        double[][] toParkDitch = {
                {toThirdStone[toThirdStone.length - 1][0], toThirdStone[toThirdStone.length - 1][1], -10, 10},
                {26, toThirdStone[toThirdStone.length - 1][1] + 20, -10, 10},
                {26, 30, -10, 10}};
        HashMap<Point, Robot.Actions> toParkDitchActions = new HashMap<Point, Robot.Actions>() {{
            put(new Point(25, 65), Robot.Actions.RETRACT_OUTTAKE);
        }};

        intake(true);
        robot.splineMove(toFirstStone, 0.6, 1, 0.55, 35, 0, 0, 20,
                toFirstStoneActions);
        //to first stone is 1
        robot.dumpPoints("" + startTime, "1");

        robot.splineMove(toFoundation, 1, 1, 0.4, 20, Math.toRadians(180), Math.toRadians(180), 25,
                toFoundationActions, true, 9000);
        // to foundation is 2
        robot.dumpPoints("" + startTime, "2");

        robot.splineMove(toSecondStone, 1, 1, 0.6, 25, 0, Math.toRadians(297), anglelock,
                toSecondStoneActions);
        //to second stone is 3 trip
        robot.dumpPoints("" + startTime, "3");

        robot.splineMove(toDepositSecondStone, 1, 1, 0.4, 20, Math.toRadians(180), Math.toRadians(180), 25,
                toDepositSecondStoneActions);
        // to foundation is 2
        robot.dumpPoints("" + startTime, "4");

        sleep(10000000);
    }
}