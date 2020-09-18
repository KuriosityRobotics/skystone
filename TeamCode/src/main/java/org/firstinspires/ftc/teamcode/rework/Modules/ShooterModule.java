package org.firstinspires.ftc.teamcode.rework.Modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.rework.ModuleTools.TelemetryProvider;
import org.firstinspires.ftc.teamcode.rework.Modules.Module;
import org.firstinspires.ftc.teamcode.rework.Robot;
import org.firstinspires.ftc.teamcode.rework.RobotTools.Shooter.AimBot;
import org.firstinspires.ftc.teamcode.rework.RobotTools.Shooter.TowerGoal;

import java.util.ArrayList;

public class ShooterModule implements Module, TelemetryProvider {
    Robot robot;
    boolean isOn;

    // States
    double flyWheelSpeed;
    boolean isShooterEngaged;

    // Motors
    private DcMotor flyWheel;
    private Servo shooterFlipServo;

    public ShooterModule(Robot robot, boolean isOn) {
        robot.telemetryDump.registerProvider(this);
        this.robot = robot;
        this.isOn = isOn;
    }

    @Override
    public void init() {
        flyWheel = robot.getDcMotor("flyWheel");
        shooterFlipServo = robot.getServo("shooterFlipServo");
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
        data.add("Flywheel speed: " + flyWheelSpeed);
        data.add("Is engaged: " + isShooterEngaged);

        return data;
    }

    @Override
    public void fileDump() {
        // TODO
    }
}
