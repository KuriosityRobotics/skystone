package org.firstinspires.ftc.teamcode.rework.Modules;

import android.os.SystemClock;

import org.firstinspires.ftc.teamcode.rework.ModuleTools.StateModule;
import org.firstinspires.ftc.teamcode.rework.ModuleTools.TelemetryProvider;
import org.firstinspires.ftc.teamcode.rework.Robot;
import org.firstinspires.ftc.teamcode.rework.RobotTools.OptimizedServo;
import org.firstinspires.ftc.teamcode.rework.ActionTools.SubAction;

import java.util.ArrayList;
import java.util.HashMap;

public class ArmModule implements StateModule, TelemetryProvider {

    private Robot robot;
    private boolean isOn;

    private int state = -1; // can destroy everything, but this is to make it init at pos
    private boolean canSetState = true;
    private long stateChangeStartTime = 0;

    // state, how to do the state
    HashMap<Integer, SubAction[]> subActions = new HashMap<>();

    // hardware
    private OptimizedServo armServo;

    public ArmModule(Robot robot, boolean isOn) {
        robot.telemetryDump.registerProvider(this);
        this.robot = robot;
        this.isOn = isOn;
    }

    @Override
    public void init() {
        armServo = new OptimizedServo(robot.getServo("armServo"));
        resetSubActions();

        // reset to 0 when init
        setState(0);
        while (!canSetState){
            update();
        }
    }

    @Override
    public synchronized void update() {

        // if it is currently changing states
        // then do it
        if (!canSetState){
            long elapsedTime = SystemClock.elapsedRealtime() - stateChangeStartTime;
            SubAction[] directions = subActions.get(state);

            // execute the sub actions for changing the state
            for (int i = 0; i < directions.length; i++){
                SubAction thisSubAction = directions[i];

                // if this sub action should have been done
                if (elapsedTime > thisSubAction.time){

                    // if it is the end sub action then it is done with changing states
                    if (thisSubAction.isEndSubAction){
                        canSetState = true;
                        resetSubActions();
                        break;
                    } else if (!thisSubAction.hasDone){ // otherwise if it is a normal sub action that has not been done
                        thisSubAction.hardware.setTargetPosition(thisSubAction.targetPos);
                        thisSubAction.hasDone = true;
                    }
                }
            }
        }

        armServo.updateToTarget();
    }

    public void resetSubActions(){
        subActions.put(0, new SubAction[]{
                new SubAction(0, armServo, 0),
                new SubAction(1000)
        });

        subActions.put(1, new SubAction[]{
                new SubAction(0, armServo, 1),
                new SubAction(1000)
        });
    }

    @Override
    public void setState(int state) {

        // if the state its trying to set to is the same don't set it
        if (this.state == state){
            return;
        }

        // we need to make sure that we always do canSetState before we set a state (outside, because we need to know it couldnt before we try to set)
        this.state = state;
        stateChangeStartTime = SystemClock.elapsedRealtime();
        canSetState = false;
    }

    @Override
    public boolean canSetState() {
        return canSetState;
    }

    @Override
    public boolean isOn() {
        return true;
    }

    @Override
    public void fileDump() {

    }

    @Override
    public ArrayList<String> getTelemetryData() {
        ArrayList<String> data = new ArrayList<>();
        data.add("state: " + state);
        data.add("changing state now: " + !canSetState);
        data.add("elapsed time: " + (SystemClock.elapsedRealtime() - stateChangeStartTime));
        data.add("servo target: " + armServo.getTargetPosition());
        return data;
    }
}
