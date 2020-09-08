package org.firstinspires.ftc.teamcode.rework.ActionTools;

import org.firstinspires.ftc.teamcode.rework.AutoTools.Point;

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
