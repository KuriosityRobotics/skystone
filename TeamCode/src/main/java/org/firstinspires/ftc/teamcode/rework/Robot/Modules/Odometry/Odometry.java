package org.firstinspires.ftc.teamcode.rework.Robot.Modules.Odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.rework.Robot.Auto.PathPlanning.Point;
import org.firstinspires.ftc.teamcode.rework.Robot.Modules.Module;
import org.firstinspires.ftc.teamcode.rework.Robot.Robot;

import java.util.HashMap;

/**
 * Odometry includes all everything required to calculate the robot's position throughout
 * TeleOp or Autonomous.
 */
public class Odometry implements Module {
    RobotPosition robotPosition;

    Robot robot;

    public DcMotor yLeft;
    public DcMotor yRight;
    public DcMotor mecanum;

    // Constants (easily modified for changing hardware)
    private static final int LEFT_POD_ENCODER_PORT = 0;
    private static final int RIGHT_POD_ENCODER_PORT = 1;
    private static final int MECANUM_POD_ENCODER_PORT = 2;

    private final double MOVE_SCALE_FACTOR = 0.0007284406721;
    private final double TURN_SCALE_FACTOR = 0.00005282291078;
    private final double STRAFE_PREDICTION_FACTOR = 4.5;

    public Odometry(Robot robot) {
        this.robot = robot; // Odometry needs robot in order to be able to get data from robot
        robotPosition = new RobotPosition(new Point(),0);

        // Odometry is the only module that won't need the hardwareMap, as it doesn't move anything
    }

    public void init() {
        yLeft = robot.getDcMotor("yLeft");
        yRight = robot.getDcMotor("yRight");
        mecanum = robot.getDcMotor("mecanum");

        yLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mecanum.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        yLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        yRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mecanum.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public synchronized void update() {
        calculateRobotPosition();
    }

    @Override
    public HashMap<String, String> getState() {
        HashMap<String, String> state = new HashMap<>();
        state.put("robotPosition.x: ", String.valueOf(robotPosition.getLocation().x));
        state.put("robotPosition.y: ", String.valueOf(robotPosition.getLocation().y));
        state.put("robotPosition.heading: ", String.valueOf(robotPosition.getHeading()));
        return state;
    }

    public synchronized RobotPosition getRobotPosition() {
        return robotPosition;
    }

    double leftPodOldPosition = 0;
    double rightPodOldPosition = 0;
    double mecanumPodOldPosition = 0;
    /**
     * Calculates the robot's position.
     */
    private void calculateRobotPosition() {
        double leftPodNewPosition = yLeft.getCurrentPosition() * -1;
        double rightPodNewPosition = yRight.getCurrentPosition();
        double mecanumPodNewPosition = mecanum.getCurrentPosition();

        double leftPodDelta = leftPodNewPosition - leftPodOldPosition;
        double rightPodDelta = rightPodNewPosition - rightPodOldPosition;
        double mecanumPodDelta = mecanumPodNewPosition - mecanumPodOldPosition;

        smallAngleOdometry(leftPodDelta, rightPodDelta, mecanumPodDelta);

        leftPodOldPosition = leftPodNewPosition;
        rightPodOldPosition = rightPodNewPosition;
        mecanumPodOldPosition = mecanumPodNewPosition;
    }

    public void smallAngleOdometry(double dLeftPod, double dRightPod, double dMecanumPod) {
        double dLeftPodInches = dLeftPod * MOVE_SCALE_FACTOR;
        double dRightPodInches = dRightPod * MOVE_SCALE_FACTOR;
        double dMecanumPodInches = dMecanumPod * MOVE_SCALE_FACTOR;

        robotPosition.setHeading(((dLeftPod + leftPodOldPosition) - (dRightPod + rightPodOldPosition)) * TURN_SCALE_FACTOR);

        double dAngle = (dLeftPodInches - dRightPodInches) * TURN_SCALE_FACTOR;
        double dRobotX = (dLeftPodInches + dRightPodInches) * .5;
        double dRobotY = dMecanumPodInches + STRAFE_PREDICTION_FACTOR * dAngle;

        robotPosition.getLocation().x += dRobotX * Math.cos(robotPosition.getHeading() + dAngle * 0.5) - dRobotY * Math.sin(robotPosition.getHeading() + dAngle * 0.5);
        robotPosition.getLocation().y += dRobotX * Math.sin(robotPosition.getHeading() + dAngle * 0.5) + dRobotY * Math.cos(robotPosition.getHeading() + dAngle * 0.5);
    }

    public void circularOdometry(double dLeftPod, double dRightPod, double dMecanumPod) {
        double dLeftPodInches = dLeftPod * MOVE_SCALE_FACTOR;
        double dRightPodInches = dRightPod * MOVE_SCALE_FACTOR;
        double dMecanumPodInches = dMecanumPod * MOVE_SCALE_FACTOR;

        double dTheta = (dLeftPodInches - dRightPodInches) * TURN_SCALE_FACTOR;

        // Default changes in x' and y' directions assume no change in robot's angle
        double dYPrime = dMecanumPodInches;
        double dXPrime = dRightPodInches;

        // Calculate midAngle, the angle used to convert from x'y' coordinate system to global (x,y) system
        double midAngle = robotPosition.getHeading() + dTheta * .5;

        // Update the global angle of the robot
//        worldAngle += dTheta;
        robotPosition.setHeading((((dLeftPod + leftPodOldPosition) * MOVE_SCALE_FACTOR) - ((dRightPod + rightPodOldPosition) * MOVE_SCALE_FACTOR)) * TURN_SCALE_FACTOR);

        if (dTheta != 0.0) { // if robot turned
            // Calculate the trigonometry portion of the positions
            double curveFactor = circularOdometrySinXOverX(dTheta / 2);

            dXPrime = (dLeftPodInches + dRightPodInches) * .5 * curveFactor;
            dYPrime = dMecanumPodInches * curveFactor + .25 * 2 * Math.sin(dTheta / 2);
        }

        // Update world x and y positions of the robot by converting from robot's x'y' coordinate
        // system to the global xy coordinate system
        robotPosition.getLocation().x += dXPrime * Math.cos(midAngle) - dYPrime * Math.sin(midAngle);
        robotPosition.getLocation().y += dXPrime * Math.sin(midAngle) + dYPrime * Math.cos(midAngle);
    }

    private double circularOdometrySinXOverX(double x) {
        if (Math.abs(x) < .00005) { // If the ratio is close enough to the limit, make it the limit TODO: Adjust or eliminate this limit
            return 1;
        } else {
            return Math.sin(x) / x;
        }
    }
}