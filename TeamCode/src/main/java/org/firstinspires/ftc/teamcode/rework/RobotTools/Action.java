package org.firstinspires.ftc.teamcode.rework.RobotTools;

public class Action {
    public int moduleNumber;
    public int stateNumber;
    public boolean queued;

    public Action(int moduleNumber, int stateNumber) {
        this.moduleNumber = moduleNumber;
        this.stateNumber = stateNumber;
        queued = false;
    }
}
