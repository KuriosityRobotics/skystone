package org.firstinspires.ftc.teamcode.rework.EnhancedHardware;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * A Servo class wrapper with enhancements, including smart position setting and angle conversion.
 */
public class EnhancedServo {
    private Servo servo;

    double position;

    double lowestPositionAngle; // The angle of the servo at position 0
    double highestPositionAngle; // The angle of the servo at position 1

    double lowerAngleLimit;
    double higherAngleLimit;

    public EnhancedServo(Servo servo, double lowestPositionAngle, double highestPositionAngle) {
        this.servo = servo;

        this.lowestPositionAngle = lowestPositionAngle;
        this.highestPositionAngle = highestPositionAngle;

        this.lowerAngleLimit = lowestPositionAngle;
        this.higherAngleLimit = highestPositionAngle;

        setPosition(0);
    }

    public EnhancedServo(Servo servo, double lowestPositionAngle, double highestPositionAngle, double lowerAngleLimit, double higherAngleLimit) {
        this.servo = servo;

        this.lowestPositionAngle = lowestPositionAngle;
        this.highestPositionAngle = highestPositionAngle;

        this.lowerAngleLimit = lowerAngleLimit;
        this.higherAngleLimit = higherAngleLimit;

        setPosition(0);
    }

    public EnhancedServo(Servo servo, double lowestPositionAngle, double highestPositionAngle, double lowerAngleLimit, double higherAngleLimit, double initialPosition) {
        this.servo = servo;

        this.lowestPositionAngle = lowestPositionAngle;
        this.highestPositionAngle = highestPositionAngle;

        this.lowerAngleLimit = lowerAngleLimit;
        this.higherAngleLimit = higherAngleLimit;

        setPosition(initialPosition);
    }

    public void setAngle(double angle) {
        // Ensure the angle is within the soft limits
        if (angle > higherAngleLimit) {
            angle = higherAngleLimit;
        }
        if (angle < lowerAngleLimit) {
            angle = lowerAngleLimit;
        }

        // Scale the input angle by the highest and lowest possible angles
        double scaledPosition = (angle - lowestPositionAngle) / (highestPositionAngle - lowestPositionAngle);

        setPosition(scaledPosition);
    }

    private void setPosition(double position) {
        if (position != this.position) { // Only reset the position if it's different than the current
            servo.setPosition(position);
            this.position = position;
        }
    }
}
