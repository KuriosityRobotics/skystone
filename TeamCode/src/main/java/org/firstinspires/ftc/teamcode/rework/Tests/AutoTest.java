package org.firstinspires.ftc.teamcode.rework.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.rework.AutoTools.Waypoint;
import org.firstinspires.ftc.teamcode.rework.Robot;

import java.util.ArrayList;

@Autonomous
public class AutoTest extends LinearOpMode {

    Robot robot;

    public void runOpMode() {

        initRobot();

        ArrayList<Waypoint> path = new ArrayList<Waypoint>();
        path.add(new Waypoint(0, 0));
        path.add(new Waypoint(24, 24));
        path.add(new Waypoint(24, 48));

        ArrayList<Waypoint> path1 = new ArrayList<Waypoint>();
        path1.add(new Waypoint(24, 48));
        path1.add(new Waypoint(24, 24));
        path1.add(new Waypoint(0,0));

        waitForStart();

        robot.startModules();


        while (opModeIsActive()) {

            robot.movements.pathFollow(path, 0, 0.8, 0.8, true, 0);

            sleep(1000);
            robot.movements.pathFollow(path1, Math.PI, 0.8, 0.8, true, 0);

            robot.telemetryDump.addData("Status: ","finished");
            break;
        }
    }

    private void initRobot() {
        robot = new Robot(hardwareMap, telemetry, this);
        robot.initModules();
    }
}

