package org.firstinspires.ftc.teamcode.rework.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.rework.ActionTools.Waypoint;
import org.firstinspires.ftc.teamcode.rework.Robot;
import org.firstinspires.ftc.teamcode.rework.ActionTools.Action;
import org.firstinspires.ftc.teamcode.rework.RobotTools.PathFollow;

@Autonomous
public class AutoPathTest extends LinearOpMode {

    public Robot robot;

    PathFollow pf1;

    public void runOpMode() {

        robot = new Robot(hardwareMap, telemetry, this);
        robot.initModules();

        pf1 = new PathFollow( new Waypoint[]{
                        new Waypoint(0,0),
                        new Waypoint(-24,0),
                        new Waypoint(2,48)
                }, robot, "test1"
        );

        waitForStart();

        robot.startModules();

        pf1.pathFollow(0, 0.8, 0.8, true, 0);
    }
}

