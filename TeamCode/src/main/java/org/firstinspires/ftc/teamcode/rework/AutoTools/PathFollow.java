package org.firstinspires.ftc.teamcode.rework.AutoTools;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.rework.ModuleTools.TelemetryProvider;
import org.firstinspires.ftc.teamcode.rework.Robot;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.rework.AutoTools.MathFunctions.angleWrap;
import static org.firstinspires.ftc.teamcode.rework.AutoTools.MathFunctions.closestPointOnLineToPoint;
import static org.firstinspires.ftc.teamcode.rework.AutoTools.MathFunctions.linePointDistance;
import static org.firstinspires.ftc.teamcode.rework.AutoTools.MathFunctions.lineSegmentCircleIntersection;

public class PathFollow implements TelemetryProvider {
    Robot robot;

    private boolean isFileDump = false;

    Point clippedPoint = new Point(0, 0);
    Point targetPoint = new Point(0, 0);
    Point adjustedTargetPoint;

    // constants
    private final static double SEARCH_AHEAD_NUMBER_OF_PATHS = 1; // How many paths to search ahead (compared to the one being currently followed) while finding which to follow
    private final static double DISTANCE_THRESHOLD = 0.5;
    private final static double ANGLE_THRESHOLD = Math.toRadians(2);
    private final static double FOLLOW_RADIUS = 15;
    private final static double SLIP_FACTOR = 0;

    // states
    private boolean isTargetingLastPoint = false;
    private String description;
    private Waypoint[] path;
    private int pathIndex = 0;

    // settings
    private double direction = 0;
    private double angleLockHeading = 0;
    private boolean willAngleLock = false;

    public PathFollow(Waypoint[] path, Robot robot, String description) {
        this.path = path;
        this.robot = robot;
        this.description = description;
    }

    public void pathFollow(double direction, double moveSpeed, double turnSpeed, boolean willAngleLock, double angleLockHeading) {
        pathIndex = 0; // Reset pathIndex

        this.direction = direction;
        this.willAngleLock = willAngleLock;
        this.angleLockHeading = angleLockHeading;
        isTargetingLastPoint = false;

        while (robot.isOpModeActive()) {
            Point robotPoint = new Point(robot.odometryModule.worldX, robot.odometryModule.worldY);
            double robotHeading = robot.odometryModule.worldAngleRad;

            clippedPoint = clipToPath(path, robotPoint);
            targetPoint = findTarget(path, clippedPoint, robotHeading);
            adjustedTargetPoint = adjustTargetPoint(targetPoint);

            setMovementsToTarget(adjustedTargetPoint, moveSpeed, turnSpeed);

            if (isDone(path, robotPoint, robotHeading)) {
                robot.drivetrainModule.xMovement = 0;
                robot.drivetrainModule.yMovement = 0;
                robot.drivetrainModule.turnMovement = 0;
                return;
            }

            // Update execution of all tasks
            robot.actionExecutor.updateExecution();
        }
    }

    private void pathFileDump(ArrayList<Waypoint> path) {
        if (robot.WILL_FILE_DUMP) {
            for (int i = 0; i < path.size(); i++) {
                robot.fileDump.addData(new StringBuilder().append(description).append("_path.txt").toString(), new StringBuilder().append(path.get(i).x).append(" ").append(path.get(i).y).toString());
            }
        }
    }

    private void fileDump() {
        if (robot.WILL_FILE_DUMP) {
            robot.fileDump.addData(new StringBuilder().append(description).append("_target.txt").toString(), new StringBuilder().append(adjustedTargetPoint.x).append(" ").append(adjustedTargetPoint.y).toString());
        }
    }

    private Point clipToPath(Waypoint[] path, Point center) {
        double nearestClipDistance = Double.MAX_VALUE;
        int shortestClipDistanceIndex = pathIndex;

        // only checks the current line and the next line (no skipping)
        for (int i = pathIndex; i < Math.min(path.length - 1, pathIndex + SEARCH_AHEAD_NUMBER_OF_PATHS); i++) {
            Point thisPathStart = path[i].toPoint();
            Point thisPathEnd = path[i + 1].toPoint();

            double thisClipDist = linePointDistance(center, thisPathStart, thisPathEnd);

            // if this clip distance is record low set the clip point to the clip point set the shortestClipDistanceIndex to index so later we can update the index we are at
            if (thisClipDist < nearestClipDistance) {
                nearestClipDistance = thisClipDist;
                shortestClipDistanceIndex = i;
            }
        }

        if (shortestClipDistanceIndex != pathIndex) { // If we start following a new point
            for (int passedPath = pathIndex; passedPath < shortestClipDistanceIndex; passedPath++) { // For every path we're no longer following
                Waypoint passedPoint = path[passedPath + 1];

                // Register actions as ready to execute
                passedPoint.registerActions(robot.actionExecutor);
            }
        }

        pathIndex = shortestClipDistanceIndex;

        return closestPointOnLineToPoint(center, path[pathIndex].toPoint(), path[pathIndex + 1].toPoint());
    }

    private Point findTarget(Waypoint[] path, Point center, double heading) {
        Point followPoint = new Point();

        Point lineStartPoint = path[pathIndex].toPoint();
        double distToFirst = Math.hypot(center.x - lineStartPoint.x, center.y - lineStartPoint.y);

        // only look at lines on current index or next index
        for (int i = pathIndex; i < Math.min(path.length - 1, pathIndex + 2); i++) {
            Point start = path[i].toPoint();
            Point end = path[i + 1].toPoint();

            ArrayList<Point> intersections = lineSegmentCircleIntersection(center, FOLLOW_RADIUS, start, end);

            double nearestAngle = Double.MAX_VALUE;

            for (Point thisIntersection : intersections) {

                double angle = Math.atan2(thisIntersection.x - center.x, thisIntersection.y - center.y) + direction;
                double deltaAngle = Math.abs(MathFunctions.angleWrap(angle - heading));
                double thisDistToFirst = Math.hypot(thisIntersection.x - lineStartPoint.x, thisIntersection.y - lineStartPoint.y);

                if (deltaAngle < nearestAngle && thisDistToFirst > distToFirst) {
                    nearestAngle = deltaAngle;
                    followPoint.x = thisIntersection.x;
                    followPoint.y = thisIntersection.y;
                }
            }
        }

        if (Math.hypot(center.x - path[path.length - 1].x, center.y - path[path.length - 1].y) < FOLLOW_RADIUS * 1.5 && pathIndex == path.length - 2) {
            followPoint = path[path.length - 1].toPoint();
            isTargetingLastPoint = true;
        }

        return followPoint;
    }

    private Point adjustTargetPoint(Point targetPoint) {
        double robotSlipX = SLIP_FACTOR * robot.velocityModule.xVel;
        double robotSlipY = SLIP_FACTOR * robot.velocityModule.yVel;

        double slipX = robotSlipX * Math.cos(robot.odometryModule.worldAngleRad) + robotSlipY * Math.sin(robot.odometryModule.worldAngleRad);
        double slipY = robotSlipY * Math.cos(robot.odometryModule.worldAngleRad) - robotSlipX * Math.sin(robot.odometryModule.worldAngleRad);

        return new Point(targetPoint.x - slipX, targetPoint.y - slipY);
    }

    private void setMovementsToTarget(Point targetPoint, double moveSpeed, double turnSpeed) {
        double distanceToTarget = Math.hypot(targetPoint.x - robot.odometryModule.worldX, targetPoint.y - robot.odometryModule.worldY);
        double absoluteAngleToTarget = Math.atan2(targetPoint.x - robot.odometryModule.worldX, targetPoint.y - robot.odometryModule.worldY);

        double relativeAngleToPoint = absoluteAngleToTarget - robot.odometryModule.worldAngleRad;
        double relativeXToPoint = Math.sin(relativeAngleToPoint) * distanceToTarget;
        double relativeYToPoint = Math.cos(relativeAngleToPoint) * distanceToTarget;

        double relativeTurnAngle = angleWrap(relativeAngleToPoint + direction);
        if (willAngleLock && isTargetingLastPoint) {
            relativeTurnAngle = angleWrap(angleLockHeading - robot.odometryModule.worldAngleRad);
        }

        double xPower = relativeXToPoint / (Math.abs(relativeYToPoint) + Math.abs(relativeXToPoint));
        double yPower = relativeYToPoint / (Math.abs(relativeYToPoint) + Math.abs(relativeXToPoint));

        // lol p
        robot.drivetrainModule.xMovement = xPower * moveSpeed;
        robot.drivetrainModule.yMovement = yPower * moveSpeed;
        robot.drivetrainModule.turnMovement = Range.clip(relativeTurnAngle / Math.toRadians(30), -1, 1) * turnSpeed;

        if (isTargetingLastPoint) {
            robot.drivetrainModule.xMovement *= Range.clip(distanceToTarget / FOLLOW_RADIUS, 0.25, 1);
            robot.drivetrainModule.yMovement *= Range.clip(distanceToTarget / FOLLOW_RADIUS, 0.25, 1);
        }
    }

    private boolean isDone(Waypoint[] path, Point center, double heading) {
        Point endPoint = path[path.length - 1].toPoint();

        return (Math.hypot(center.x - endPoint.x, center.y - endPoint.y) < DISTANCE_THRESHOLD) && (!willAngleLock || Math.abs(angleWrap(angleLockHeading - heading)) < ANGLE_THRESHOLD) && pathIndex == path.length - 2;
    }

    public boolean isFileDump() {
        return isFileDump;
    }

    @Override
    public ArrayList<String> getTelemetryData() {
        ArrayList<String> data = new ArrayList<>();
        data.add("path: " + description);
        data.add("clippedX: " + String.valueOf(clippedPoint.x));
        data.add("clippedY: " + String.valueOf(clippedPoint.y));
        data.add("targetX: " + String.valueOf(targetPoint.x));
        data.add("targetY: " + String.valueOf(targetPoint.y));
        data.add("pathIndex: " + String.valueOf(pathIndex));
        return data;
    }
}