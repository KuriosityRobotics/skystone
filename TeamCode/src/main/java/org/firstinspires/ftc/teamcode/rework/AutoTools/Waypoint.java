package org.firstinspires.ftc.teamcode.rework.AutoTools;

import org.firstinspires.ftc.teamcode.rework.AutoTools.Actions.Action;
import org.firstinspires.ftc.teamcode.rework.AutoTools.Actions.ActionExecutor;

import java.util.ArrayList;

public class Waypoint extends Point {
    ArrayList<Action> actions;

    public Waypoint(double x, double y){
        super(x,y);
    }

    public Waypoint(double x, double y, ArrayList<Action> actions) {
        super(x,y);
        this.actions = actions;
    }

    public void executeActions(ActionExecutor actionExecutor) {
        actionExecutor.registerActions(actions);
    }

    public Point toPoint(){
        return new Point(x,y);
    }
}
