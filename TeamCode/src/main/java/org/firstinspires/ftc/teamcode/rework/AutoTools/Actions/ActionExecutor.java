package org.firstinspires.ftc.teamcode.rework.AutoTools.Actions;

import org.firstinspires.ftc.teamcode.rework.ModuleTools.TelemetryProvider;
import org.firstinspires.ftc.teamcode.rework.Robot;

import java.util.ArrayList;

public class ActionExecutor implements TelemetryProvider {
    private Robot robot;
    private ArrayList<Action> executingActions;

    public ActionExecutor(Robot robot) {
        robot.telemetryDump.registerProvider(this);

        executingActions = new ArrayList<Action>();

        this.robot = robot;
    }

    /**
     * Update execution on all actions registered as currently executing.
     */
    public void updateExecution() {
        for (Action action : executingActions) {
            switch(action) {
                case SLOW_MODE:
                    drivetrainSlowMode(true);

                    executingActions.remove(Action.SLOW_MODE);
                    break;
                case FULL_SPEED:
                    drivetrainSlowMode(false);

                    executingActions.remove(Action.FULL_SPEED);
                    break;
            }
        }
    }

    /**
     * Registers actions as ready to be executed. They will be executed the next time
     * updateExecution() is run.
     *
     * @param actions An ArrayList of Actions to register.
     * @see #updateExecution()
     */
    public void registerActions(ArrayList<Action> actions) {
        for (Action action : actions) {
            registerAction(action);
        }
    }

    /**
     * Register an action as ready to be executed. They will be executed the next time
     * updateExecution() is run.
     *
     * @param action
     * @see #updateExecution()
     */
    public void registerAction(Action action) {
        // Register the action
        executingActions.add(action);
    }

    /**
     * Toggle the drivetrain slowMode state.
     *
     * @param isSlowMode whether or not to have slowMode on.
     */
    private void drivetrainSlowMode(boolean isSlowMode) {
        robot.drivetrainModule.isSlowMode = isSlowMode;
    }

    @Override
    public ArrayList<String> getTelemetryData() {
        ArrayList<String> data = new ArrayList<>();
        String executingActions = "Actions being executed: ";

        for (int i = 0; i < this.executingActions.size(); i++) {
            executingActions = executingActions + this.executingActions.get(i).name();

            if (i != this.executingActions.size()) {
                executingActions = executingActions + ", ";
            }
        }

        data.add(executingActions);

        return data;
    }
}
