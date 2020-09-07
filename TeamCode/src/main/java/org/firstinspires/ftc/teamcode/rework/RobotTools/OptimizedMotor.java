package org.firstinspires.ftc.teamcode.rework.RobotTools;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.rework.AutoTools.PIDController;
import org.firstinspires.ftc.teamcode.rework.ModuleTools.Module;

public class OptimizedMotor implements Hardware {

    DcMotor thisMotor;
    PIDController pidController;
    double targetPosition;
    double power;

    // power mode or target mode
    boolean powerMode;

    public OptimizedMotor(DcMotor thisMotor, PIDController pidController) {
        this.thisMotor = thisMotor;
        this.pidController = pidController;
        powerMode = false;
        targetPosition = 0;
    }

    @Override
    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;

        int currentPos = thisMotor.getCurrentPosition();
        pidController.PID(targetPosition - currentPos, 0);
        power = pidController.scale;
    }

    @Override
    public void updateToTarget() {
        thisMotor.setPower(power);
    }

    @Override
    public double getTargetPosition() {
        return 0;
    }
}
