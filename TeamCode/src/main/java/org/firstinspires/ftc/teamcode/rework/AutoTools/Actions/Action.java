package org.firstinspires.ftc.teamcode.rework.AutoTools.Actions;

/**
 * A class to keep track of an action, along with the state of its execution.
 */
public class Action {
    ActionType type;
    ActionState state;

    long beginExecutionTime;

    public Action(ActionType type) {
        this.type = type;
        state = ActionState.PENDING_START;
    }

    @Override
    public boolean equals(Object action) {
        // Two actions are equal if they do the same thing and begin at the same time.
        return this.type == ((Action) action).type && this.beginExecutionTime == ((Action) action).beginExecutionTime;
    }
}
