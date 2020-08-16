package org.firstinspires.ftc.teamcode.rework.Robot.Modules.Odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.rework.Robot.Auto.PathPlanning.Point;
import org.firstinspires.ftc.teamcode.rework.Robot.Modules.Module;
import org.firstinspires.ftc.teamcode.rework.Robot.Robot;

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

    private final double INCHES_PER_ENCODER_TICK = 0.0007284406721;
    private final double LR_ENCODER_DIST_FROM_CENTER = 6.89512052;
    private final double M_ENCODER_DIST_FROM_CENTER = 4.5;

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

        calculateNewPosition(leftPodDelta, rightPodDelta, mecanumPodDelta);

        leftPodOldPosition = leftPodNewPosition;
        rightPodOldPosition = rightPodNewPosition;
        mecanumPodOldPosition = mecanumPodNewPosition;
    }

    public void calculateNewPosition(double dLeftPod, double dRightPod, double dMecanumPod) {
        // convert all inputs to inches
        double dLeftPodInches = dLeftPod * INCHES_PER_ENCODER_TICK;
        double dRightPodInches = dRightPod * INCHES_PER_ENCODER_TICK;
        double dMecanumPodInches = dMecanumPod * INCHES_PER_ENCODER_TICK;

        // so its easier to type
        double L = dLeftPodInches;
        double R = dRightPodInches;
        double M = dMecanumPodInches;
        double P = LR_ENCODER_DIST_FROM_CENTER;
        double Q = M_ENCODER_DIST_FROM_CENTER;

        // find robot relative deltas
        double dTheta = (L - R) / (2 * P);
        double dRobotX = M * sinXOverX(dTheta) + Q * Math.sin(dTheta) - L * cosXMinusOneOverX(dTheta) + P * (Math.cos(dTheta) - 1);
        double dRobotY = L * sinXOverX(dTheta) - P * Math.sin(dTheta) + M * cosXMinusOneOverX(dTheta) + Q * (Math.cos(dTheta) - 1);

        // find global deltas by rotating robot relative deltas by the old heading, then add the delta to the sum
        robotPosition.getLocation().x += dRobotX * Math.cos(robotPosition.getHeading()) + dRobotY * Math.sin(robotPosition.getHeading());
        robotPosition.getLocation().y += dRobotY * Math.cos(robotPosition.getHeading()) - dRobotX * Math.sin(robotPosition.getHeading());

        // update heading
        robotPosition.setHeading(((dLeftPod + leftPodOldPosition) - (dRightPod + rightPodOldPosition)) * INCHES_PER_ENCODER_TICK / (2 * P));
    }

    /*
    taylor series expansion to make stuff COOL
     */
    private double sinXOverX(double x){
        if (Math.abs(x) < 2){
            double retVal = 0;
            double top = 1;
            double bottom = 1;
            for (int i = 0; i < 9; i++){
                retVal += top/bottom;
                top *= -x*x;
                bottom *= (2 * i + 2)*(2 * i + 3);
            }
            return retVal;
        } else {
            return Math.sin(x)/x;
        }
    }

    /*
    taylor series expansion to make stuff COOL
     */
    private double cosXMinusOneOverX(double x){
        if (Math.abs(x) < 2){
            double retVal = 0;
            double top = -x;
            double bottom = 2;
            for (int i = 0; i < 9; i++){
                retVal += top/bottom;
                top *= -x*x;
                bottom *= (2 * i + 3)*(2 * i + 4);
            }
            return retVal;
        } else {
            return (Math.cos(x)-1)/x;
        }
    }
}