package org.firstinspires.ftc.teamcode.rework.RobotTools;

import com.qualcomm.robotcore.hardware.Servo;

public class OptimizedServo implements Hardware{
    Servo thisServo;
    double targetPosition;
    double oldTargetPosition;

    // we can make a motion profile of it later


    public OptimizedServo(Servo thisServo) {
        this.thisServo = thisServo;
        targetPosition = thisServo.getPosition();
        oldTargetPosition = targetPosition;
    }

    public void setTargetPosition(double newPosition){
        // maybe do an if its different then set but setting var doesnt take much time
        targetPosition = newPosition;
    }

    public void updateToTarget(){
        thisServo.setPosition(targetPosition);
    }

    @Override
    public double getTargetPosition() {
        return targetPosition;
    }
}
