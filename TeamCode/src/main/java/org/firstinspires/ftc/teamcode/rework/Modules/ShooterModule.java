package org.firstinspires.ftc.teamcode.rework.Modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.rework.ModuleTools.TelemetryProvider;
import org.firstinspires.ftc.teamcode.rework.Robot;

import java.util.ArrayList;

public class ShooterModule implements Module, TelemetryProvider {
    Robot robot;
    boolean isOn;

    // States
    double flyWheelSpeed;
    double shooterFlapAngle; // Radians, relative to horizon.
    int queuedFires; // Number of rings queued up to shoot.

    // Motors
    private DcMotor leftFlyWheel;
    private DcMotor rightFlyWheel;
    private Servo shooterFlap;
    private Servo indexerServo;

    public ShooterModule(Robot robot, boolean isOn) {
        robot.telemetryDump.registerProvider(this);
        this.robot = robot;
        this.isOn = isOn;
    }

    @Override
    public void init() {
        // TODO
//        leftFlyWheel = robot.getDcMotor("leftFlyWheel");
//        rightFlyWheel = robot.getDcMotor("rightFlyWheel");
//        shooterFlap = robot.getServo("shooterFlap");
//        indexerServo = robot.getServo("indexerServo");
    }

    @Override
    public void update() {
        // TODO
        // Ensure flywheel is up to speed, index and shoot if commanded to shoot.
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    @Override
    public ArrayList<String> getTelemetryData() {
        // TODO
        ArrayList<String> data = new ArrayList<>();
//        data.add("Flywheel speed: " + flyWheelSpeed);
//        data.add("Will shoot: " + willShoot);

        return data;
    }

    @Override
    public void fileDump() {
        // TODO
    }
}
