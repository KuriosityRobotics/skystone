package org.firstinspires.ftc.teamcode.rework.AutoTools;

import org.firstinspires.ftc.teamcode.rework.RobotTools.Action;

public class Waypoint extends Point {

    public Action[] actionSet;

    public Waypoint(double x, double y){
        super(x,y);
        this.actionSet = new Action[]{};
    }

    public Waypoint(double x, double y, Action[] actionSet) {
        super(x,y);
        this.actionSet = actionSet;
    }

    public Point toPoint(){
        return new Point(x,y);
    }
}
