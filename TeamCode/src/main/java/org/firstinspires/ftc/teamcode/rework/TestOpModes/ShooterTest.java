package org.firstinspires.ftc.teamcode.rework.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.rework.ModuleTools.TelemetryProvider;
import org.firstinspires.ftc.teamcode.rework.Robot;

import java.util.ArrayList;

@TeleOp
public class ShooterTest extends LinearOpMode implements TelemetryProvider {
    Robot robot;

    double flapAngle = 90; // In degrees
    double flyWheelSpeed = 1000; // In degrees per second

    @Override
    public void runOpMode() {
        robot = new Robot(hardwareMap, telemetry, this);

        waitForStart();

        robot.startModules();

        while (opModeIsActive()) {
            flapAngle += gamepad1.right_stick_y * 0.001;
            flyWheelSpeed += gamepad1.left_stick_y * 0.005;

            robot.shooterModule.shooterFlapAngle = flapAngle;
            robot.shooterModule.flyWheelTargetSpeed = flyWheelSpeed;
        }
    }

    @Override
    public ArrayList<String> getTelemetryData() {
        ArrayList<String> data = new ArrayList<>();

        data.add("Fly wheel speed: " + flyWheelSpeed);
        data.add("Shooter flap angle: " + flapAngle);

        return data;
    }

    public String getName() {
        return "ShooterTest";
    }
}
