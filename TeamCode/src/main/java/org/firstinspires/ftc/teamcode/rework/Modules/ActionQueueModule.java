package org.firstinspires.ftc.teamcode.rework.Modules;

import android.util.Log;

import org.firstinspires.ftc.teamcode.rework.ActionTools.Action;
import org.firstinspires.ftc.teamcode.rework.ModuleTools.Module;
import org.firstinspires.ftc.teamcode.rework.ModuleTools.TelemetryProvider;
import org.firstinspires.ftc.teamcode.rework.Robot;

import java.util.ArrayList;
import java.util.Objects;

public class ActionQueueModule implements Module, TelemetryProvider {

    private boolean isOn;
    private Robot robot;
    private ArrayList<Action> executingActions;

    public ActionQueueModule(Robot robot, boolean isOn) {
        executingActions = new ArrayList<>();
        robot.telemetryDump.registerProvider(this);
        this.robot = robot;
        this.isOn = isOn;
    }

    @Override
    public void init() {

    }

    @Override
    public synchronized void update() {
        for (int i = 0; i < robot.stateModules.length; i++){
            // if it can set state and there exists a state to set
            // set the state and remove action
            if (robot.stateModules[i].canSetState() && robot.actionQueueModule.existsFirstAction(i)){
                robot.stateModules[i].setState(Objects.requireNonNull(robot.actionQueueModule.getFirstAction(i)).stateNumber);
                robot.actionQueueModule.removeFirstAction(i);
            }
        }
    }

    public synchronized void registerAction(Action action){
        if (getLastAction(action.moduleNumber).stateNumber == action.stateNumber){
            return;
        }
        executingActions.add(action);
    }

    private void removeFirstAction(int moduleNumber){
        for (int i = 0; i < executingActions.size(); i++){
            if (executingActions.get(i).moduleNumber == moduleNumber){
                executingActions.remove(i);
                return;
            }
        }
    }

    private Action getFirstAction(int moduleNumber){

        for (int i = 0; i < executingActions.size(); i++){
            if (executingActions.get(i).moduleNumber == moduleNumber){
                return executingActions.get(i);
            }
        }

        return null;
    }

    private Action getLastAction(int moduleNumber){

        Action retVal = new Action(-1, -1);

        for (int i = 0; i < executingActions.size(); i++){
            if (executingActions.get(i).moduleNumber == moduleNumber){
                retVal = executingActions.get(i);
            }
        }

        return retVal;
    }

    private boolean existsFirstAction(int moduleNumber){
        for (int i = 0; i < executingActions.size(); i++){
            if (executingActions.get(i).moduleNumber == moduleNumber){
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<String> getTelemetryData() {
        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < this.executingActions.size(); i++) {
            data.add("module "  + this.executingActions.get(i).moduleNumber + " state "  + this.executingActions.get(i).stateNumber);
            Log.i("actions", "module "  + this.executingActions.get(i).moduleNumber + " state "  + this.executingActions.get(i).stateNumber);
        }

        return data;
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    @Override
    public void fileDump() {

    }
}
