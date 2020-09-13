package org.firstinspires.ftc.teamcode.rework.Modules.Turret;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.rework.ModuleTools.TelemetryProvider;
import org.firstinspires.ftc.teamcode.rework.Modules.Module;
import org.firstinspires.ftc.teamcode.rework.Robot;

import java.util.ArrayList;

public class TurretModule implements Module, TelemetryProvider {
    Robot robot;
    boolean isOn;

    // Aim calculator
    AimBot aimBot;

    // States
    TowerGoal targetGoal;
    boolean fireOnLock = false;

    // Pitch and yaw for turret to try to aim at.
    double targetPitch = 0; // Angle in radians, 0 is horizon
    double targetYaw = 0; // Angle in radians, relative to robot, 0 is front of robot

    // Motors
    private DcMotor flyWheel;
    private Servo turretPitch;
    private Servo turretYaw;

    public TurretModule(Robot robot, boolean isOn) {
        robot.telemetryDump.registerProvider(this);
        this.robot = robot;
        this.isOn = isOn;

        this.aimBot = new AimBot(robot.odometryModule, robot.vuforia);
    }

    @Override
    public void init() {
        flyWheel = robot.getDcMotor("flyWheel");
        turretPitch = robot.getServo("turretPitch");
        turretYaw = robot.getServo("turretYaw");
    }

    @Override
    public void update() {
        // Move towards position specified, shoot if there
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    @Override
    public ArrayList<String> getTelemetryData() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Target goal: " + targetGoal);
        data.add("Calculated pitch to hit goal: " + targetPitch);
        data.add("Calculated yaw to hit goal: " + targetYaw);
        data.add("Fire on target lock: " + fireOnLock);

        return data;
    }

    @Override
    public void fileDump() {
        // TODO
    }
}
