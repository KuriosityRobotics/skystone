package org.firstinspires.ftc.teamcode.rework.Modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.rework.EnhancedHardware.EnhancedServo;
import org.firstinspires.ftc.teamcode.rework.ModuleTools.TelemetryProvider;
import org.firstinspires.ftc.teamcode.rework.Robot;

import java.util.ArrayList;

public class ShooterModule implements Module, TelemetryProvider {
    Robot robot;
    boolean isOn;

    // States
    public double flyWheelTargetSpeed;
    public double shooterFlapAngle; // Radians, relative to horizon.
    public int queuedFires; // Number of rings queued up to shoot.

    // Motors
    private DcMotorEx leftFlyWheel;
//    private DcMotorEx rightFlyWheel;
    private EnhancedServo shooterFlap;
    private Servo indexerServo;

    public ShooterModule(Robot robot, boolean isOn) {
        robot.telemetryDump.registerProvider(this);
        this.robot = robot;
        this.isOn = isOn;
    }

    @Override
    public void init() {
        // TODO
        leftFlyWheel = (DcMotorEx) robot.getDcMotor("leftFlyWheel");
//        rightFlyWheel = (DcMotorEx) robot.getDcMotor("rightFlyWheel");
        shooterFlap = new EnhancedServo(robot.getServo("shooterFlap"), 0, 180); // In degrees
//        indexerServo = robot.getServo("indexerServo");

        leftFlyWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rightFlyWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFlyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rightFlyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void update() {
        // TODO
        // Ensure flywheel is up to speed, index and shoot if commanded to shoot.
        leftFlyWheel.setVelocity(flyWheelTargetSpeed);
        shooterFlap.setAngle(shooterFlapAngle);
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
