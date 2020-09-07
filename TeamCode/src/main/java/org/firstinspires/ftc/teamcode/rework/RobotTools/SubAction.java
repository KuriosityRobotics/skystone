package org.firstinspires.ftc.teamcode.rework.RobotTools;

public class SubAction {

    public long time;
    public Hardware hardware;
    public double targetPos;
    public boolean isEndSubAction;

    public boolean hasDone;

    public SubAction(long time, Hardware hardware, double targetPos) {
        this.time = time;
        this.hardware = hardware;
        this.targetPos = targetPos;
        hasDone = false;
        isEndSubAction = false;
    }

    public SubAction(long time) {
        this.time = time;
        isEndSubAction = true;
    }
}
