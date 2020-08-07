package org.firstinspires.ftc.teamcode.rework.Modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ReworkDrivetrain extends Module {
    // STATES
    private double yMovement;
    private double mecanumMovement;
    private double turnMovement;
    private boolean isSlowMode;

    private DcMotor fLeft;
    private DcMotor fRight;
    private DcMotor bLeft;
    private DcMotor bRight;

    private final static double POWER_SCALE_FACTOR = 0.9;
    private final static double MECANUM_POWER_SCALE_FACTOR = 1.4;
    private final static double SLOW_POWER_SCALE_FACTOR = 0.3;

    public ReworkDrivetrain(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        init();
    }

    public void init() {
        fLeft = getDcMotor("fLeft");
        fRight = getDcMotor("fRight");
        bLeft = getDcMotor("bLeft");
        bRight = getDcMotor("bRight");

        setDrivetrainDirection(DcMotor.Direction.REVERSE);
        setDrivetrainZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void update() {
        drive();
    }

    public void setStates(double yMovement, double mecanumMovement, double turnMovement) {
        this.yMovement = yMovement;
        this.mecanumMovement = mecanumMovement;
        this.turnMovement = turnMovement;
    }

    public void slowMode(boolean slowMode) {
        this.isSlowMode = slowMode;
    }

    private void drive() {
        double fLPower = ((yMovement * MECANUM_POWER_SCALE_FACTOR) - turnMovement + mecanumMovement) * POWER_SCALE_FACTOR;
        double fRPower = ((yMovement * MECANUM_POWER_SCALE_FACTOR) + turnMovement + mecanumMovement) * POWER_SCALE_FACTOR;
        double bLPower = ((yMovement * MECANUM_POWER_SCALE_FACTOR) - turnMovement + mecanumMovement) * POWER_SCALE_FACTOR;
        double bRPower = ((yMovement * MECANUM_POWER_SCALE_FACTOR) + turnMovement - mecanumMovement) * POWER_SCALE_FACTOR;

        double maxPower = Math.abs(fLPower);
        if (Math.abs(fRPower) > maxPower) {
            maxPower = Math.abs(fRPower);
        }
        if (Math.abs(bLPower) > maxPower) {
            maxPower = Math.abs(bLPower);
        }
        if (Math.abs(bRPower) > maxPower) {
            maxPower = Math.abs(bRPower);
        }

        double scaleDown = 1.0;
        if (maxPower > 1.0) {
            scaleDown = 1.0 / maxPower;
        }

        fLPower *= scaleDown;
        fRPower *= scaleDown;
        bLPower *= scaleDown;
        bRPower *= scaleDown;

        if (isSlowMode) {
            fLPower *= SLOW_POWER_SCALE_FACTOR;
            fRPower *= SLOW_POWER_SCALE_FACTOR;
            bLPower *= SLOW_POWER_SCALE_FACTOR;
            bRPower *= SLOW_POWER_SCALE_FACTOR;
        }

        setMotorPowers(fLPower, fRPower, bLPower, bRPower);
    }

    private void setMotorPowers(double fLPower, double fRPower, double bLPower, double bRPower) {
        fLeft.setPower(fLPower);
        fRight.setPower(fRPower);
        bLeft.setPower(bLPower);
        bRight.setPower(bRPower);
    }

    private void setDrivetrainZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        fLeft.setZeroPowerBehavior(zeroPowerBehavior);
        fRight.setZeroPowerBehavior(zeroPowerBehavior);
        bLeft.setZeroPowerBehavior(zeroPowerBehavior);
        bRight.setZeroPowerBehavior(zeroPowerBehavior);
    }

    private void setDrivetrainDirection(DcMotor.Direction direction) {
        fLeft.setDirection(direction);
        fRight.setDirection(direction);
        bLeft.setDirection(direction);
        bRight.setDirection(direction);
    }
}
