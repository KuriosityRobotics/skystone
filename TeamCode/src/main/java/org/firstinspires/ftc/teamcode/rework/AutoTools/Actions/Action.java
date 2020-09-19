package org.firstinspires.ftc.teamcode.rework.AutoTools.Actions;

public class Action {
    ActionType type;
    ActionState state;

    long beginExecutionTime;

    public Action(ActionType type) {
        this.type = type;
        state = ActionState.PENDING_START;
    }
}
