package org.firstinspires.ftc.teamcode.Skystone;

import android.os.SystemClock;
import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Skystone.Accessories.MathFunctions;
import org.firstinspires.ftc.teamcode.Skystone.Auto.Actions.Action;
import org.firstinspires.ftc.teamcode.Skystone.Auto.Actions.Enums.ActionState;
import org.firstinspires.ftc.teamcode.Skystone.Modules.DriveModule;
import org.firstinspires.ftc.teamcode.Skystone.Modules.FoundationMoverModule;
import org.firstinspires.ftc.teamcode.Skystone.Modules.IntakeModule;
import org.firstinspires.ftc.teamcode.Skystone.Modules.OdometryModule;
import org.firstinspires.ftc.teamcode.Skystone.Modules.OuttakeModule;
import org.firstinspires.ftc.teamcode.Skystone.Modules.PathModule;
import org.firstinspires.ftc.teamcode.Skystone.MotionProfiler.CatmullRomSplineUtils;
import org.firstinspires.ftc.teamcode.Skystone.MotionProfiler.Point;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

import static org.firstinspires.ftc.teamcode.Skystone.Accessories.DataDump.dump;
import static org.firstinspires.ftc.teamcode.Skystone.Accessories.MathFunctions.angleWrap;

@Deprecated
public class Robot {
    public HardwareCollection hardwareCollection;
    public OdometryModule odometryModule;
    public DriveModule driveModule;
    public IntakeModule intakeModule;
    public OuttakeModule outtakeModule;
    public PathModule pathModule;
    public FoundationMoverModule foundationMoverModule;

    //Drive Motors
    private DcMotor fLeft;
    private DcMotor fRight;
    private DcMotor bLeft;
    private DcMotor bRight;

    // Intake Motors
    private DcMotor intakeLeft;
    private DcMotor intakeRight;

    // Outtake Motors
    private DcMotor outtakeSpool;
    private DcMotor outtakeSpool2;

    // Outtake Servos
    private Servo outtakeExtender;
    private Servo backClamp;
    private Servo frontClamp;
    private Servo intakePusher;

    // Foundation Servos
    private Servo leftFoundation;
    private Servo rightFoundation;

    private DistanceSensor intakeStoneDistance;

    // Outtake Slide Positions
    public final double OUTTAKE_SLIDE_EXTENDED = 0.09;
    public final double OUTTAKE_SLIDE_RETRACTED = 0.85;
//    public final double OUTTAKE_SLIDE_PARTIAL_EXTEND = 0.27; // First peg .27, second peg .121

    // Clamp positions
    public final double FRONTCLAMP_ACTIVATECAPSTONE = 0;
    public final double FRONTCLAMP_CLAMPED = .52;
    public final double FRONTCLAMP_RELEASED = .08;
    public final double BACKCLAMP_CLAMPED = .22;
    public final double BACKCLAMP_RELEASED = .635;

    // Outtake Pusher Positions
    public final double PUSHER_PUSHED = 0.91;
    public final double PUSHER_RETRACTED = .475;

    // Foundation Mover Positions
    public final double LEFTFOUNDATION_EXTENDED = .65;
    public final double LEFTFOUNDATION_RETRACTED = .86;

    public final double RIGHTFOUNDATION_EXTENDED = .94;
    public final double RIGHTFOUNDATION_RETRACTED = .72;

    // Timer delays for outtake actions. All in ms
    public final long DELAY_SLIDE_ON_EXTEND = 0;

    public final long DELAY_RELEASE_CLAMP_ON_RETRACT = 0;
    public final long DELAY_PUSHER_ON_RETRACT = 0;
    public final long DELAY_SLIDE_ON_RETRACT = 300;

    public final long DELAY_PUSHER_ON_CLAMP = 0;
    public final long DELAY_RETRACT_PUSHER_ON_CLAMP = 450;
    public final long DELAY_CLAMP_ON_CLAMP = 700;

    // Constants for spool encoder positions
    public final int[] spoolHeights = {150, 400, 763, 1075, 1420, 1764, 2097, 2446, 2778, 3135, 3500, 3825};

    //robots position
    private Point robotPos = new Point();
    private double anglePos;

    //imu
    private BNO055IMU imu;
    private Orientation angles;
    private Position position;

    //Inherited classes from Op Mode
    public Telemetry telemetry;
    private HardwareMap hardwareMap;
    public LinearOpMode linearOpMode;

    //dimensions
    private final double encoderPerRevolution = 806.4;
    private double xMovement;
    private double yMovement;
    private double turnMovement;

    public boolean isDebug = false;
    private boolean isAutoStopIntake;

    private StringBuilder odometryAllData = new StringBuilder();
    private StringBuilder odometryPoints = new StringBuilder();
    private StringBuilder splinePoints = new StringBuilder();
    private StringBuilder waypoints = new StringBuilder();

    /**
     * robot constructor, does the hardwareMaps
     *
     * @param hardwareMap
     * @param telemetry
     * @param linearOpMode
     */
    public Robot(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode linearOpMode) {
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.linearOpMode = linearOpMode;

        //config names need to match configs on the phone

        //Map drive motors
        fLeft = getDcMotor("fLeft");
        if (fLeft != null) {
            fLeft.setDirection(DcMotor.Direction.FORWARD);
        }

        fRight = getDcMotor("fRight");
        if (fRight != null) {
            fRight.setDirection(DcMotor.Direction.REVERSE);
        }

        bLeft = getDcMotor("bLeft");
        if (bLeft != null) {
            bLeft.setDirection(DcMotor.Direction.FORWARD);
        }

        bRight = getDcMotor("bRight");
        if (bRight != null) {
            bRight.setDirection(DcMotor.Direction.REVERSE);
        }

        intakeLeft = getDcMotor("intakeLeft");
        if (intakeLeft != null) {
            intakeLeft.setDirection(DcMotor.Direction.REVERSE);
        }

        intakeRight = getDcMotor("intakeRight");
        if (intakeRight != null) {
            intakeRight.setDirection(DcMotor.Direction.FORWARD);
        }

        outtakeSpool = getDcMotor("outtakeSpool");
        if (outtakeSpool != null) {
            outtakeSpool.setDirection(DcMotor.Direction.REVERSE);
        }

        outtakeSpool2 = getDcMotor("outtakeSpool2");
        if (outtakeSpool2 != null) {
            outtakeSpool2.setDirection(DcMotor.Direction.REVERSE);
        }

        outtakeExtender = getServo("outtakeExtender");
        backClamp = getServo("backClamp");
        frontClamp = getServo("frontClamp");
        intakePusher = getServo("intakePusher");

        leftFoundation = getServo("leftFoundation");
        rightFoundation = getServo("rightFoundation");

        intakeStoneDistance = hardwareMap.get(DistanceSensor.class, "intakeStoneDistance");

    }

    public void update(){
        // put all sensors on rev 2
        hardwareCollection.refreshData2();
        hardwareCollection.updateTime();

        // update all modules
        odometryModule.update(this, hardwareCollection);
        pathModule.update(this);
        driveModule.update(this, hardwareCollection);
        intakeModule.update(this, hardwareCollection);
        outtakeModule.update(this, hardwareCollection);
        foundationMoverModule.update(this, hardwareCollection);

        // TODO does this actually work? delete when answer found
        if (linearOpMode.isStopRequested() && isDebug){
            dump(this, hardwareCollection.currTime + "_ALL_DATA");
        }
    }

    private DcMotor getDcMotor(String name) {
        try {
            return hardwareMap.dcMotor.get(name);

        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private Servo getServo(String name) {
        try {
            return hardwareMap.servo.get(name);

        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private Rev2mDistanceSensor getRev2mDistanceSensor(String name) {
        try {
            return hardwareMap.get(Rev2mDistanceSensor.class, name);

        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    /**
     * call this when you want to use the imu in a program
     */
    public void intializeIMU() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);
    }

    /**
     * resets all the encoders back to 0
     */
    public void resetEncoders() {
        fLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * make all the motors run using encoder
     * do NOT use this if you want to set your own powers (e.x deceleration)
     */
    public void changeRunModeToUsingEncoder() {
        fLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * sets drive motors mode to whatever
     *
     * @param runMode what to set all the motors to
     */
    public void setDrivetrainMotorModes(DcMotor.RunMode runMode) {
        fLeft.setMode(runMode);
        fRight.setMode(runMode);
        bLeft.setMode(runMode);
        bRight.setMode(runMode);
    }

    public void initServos() {
        foundationMovers(false);

        boolean isRetract = true;
        long outtakeExecutionTime = SystemClock.elapsedRealtime();
        long currentTime;

        frontClamp.setPosition(FRONTCLAMP_RELEASED);
        backClamp.setPosition(BACKCLAMP_CLAMPED);
        outtakeExtender.setPosition(OUTTAKE_SLIDE_RETRACTED);
        intakePusher.setPosition(PUSHER_RETRACTED);

        while (isRetract && !linearOpMode.isStopRequested()) {
            currentTime = SystemClock.elapsedRealtime();
            if (currentTime - outtakeExecutionTime >= 1500) {
                backClamp.setPosition(BACKCLAMP_CLAMPED);

                isRetract = false;
            }
        }
    }

    /**
     * sets all the motor speeds independently
     *
     * @param fLpower
     * @param fRpower
     * @param bLpower
     * @param bRpower
     */
    public void allWheelDrive(double fLpower, double fRpower, double bLpower, double bRpower) {
        fLeft.setPower(fLpower);
        fRight.setPower(fRpower);
        bLeft.setPower(bLpower);
        bRight.setPower(bRpower);
    }

    /**
     * allows to toggle intake
     *
     * @param toggle if true, intake, if false, stop intake
     */
    public void intake(boolean toggle) {
        if (toggle) {
            intakeLeft.setPower(1);
            intakeRight.setPower(1);
        } else {
            intakeLeft.setPower(-1);
            intakeRight.setPower(-1);
        }
    }

    /**
     * @param motor the motor you want to reset
     */
    public void resetMotor(DcMotor motor) {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * move straight forwards or backwards
     *
     * @param speed           speed to move, >0, <1
     * @param targetDistance, target distance, -infinity to infinity
     */
    public void finalMove(double speed, double targetDistance) {
        //move robot function
        //to move backwards make targetDistance negative
        double rotations = 0;
        if (targetDistance > 0) {
            rotations = targetDistance / 0.0168;
        } else {
            rotations = targetDistance / 0.0156;
        }
        moveRobot(speed, (int) (rotations));
        brakeRobot();
        linearOpMode.sleep(100);
    }

    /**
     * bare bones move function using encoders
     *
     * @param speed          speed to move
     * @param targetPosition target distance
     */
    public void moveRobot(double speed, int targetPosition) {
        //called by final move - bare bones move function
        this.setDrivetrainMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.setDrivetrainMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        this.setDrivetrainMotorModes(DcMotor.RunMode.RUN_TO_POSITION);
        double newSpeed = speed;
        if (targetPosition < 0) {
            newSpeed = newSpeed * -1;
            fLeft.setPower(newSpeed);
            fRight.setPower(newSpeed);
            bLeft.setPower(newSpeed);
            bRight.setPower(newSpeed);
        } else {
            fLeft.setPower(newSpeed);
            fRight.setPower(newSpeed);
            bLeft.setPower(newSpeed);
            bRight.setPower(newSpeed);
        }
        fLeft.setTargetPosition(targetPosition);
        fRight.setTargetPosition(targetPosition);
        bLeft.setTargetPosition(targetPosition);
        bRight.setTargetPosition(targetPosition);
        while (fLeft.isBusy() && fRight.isBusy() && bLeft.isBusy() && bRight.isBusy()
                && linearOpMode.opModeIsActive()) {
        }
        brakeRobot();
        telemetry.addLine("finished sleeping");
        this.setDrivetrainMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.setDrivetrainMotorModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void driveMotorsBreakZeroBehavior() {
        //sets drive motors to brake mode
        fLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * brakes all drivetrain motors
     */
    public void brakeRobot() {
        //brakes robot
        driveMotorsBreakZeroBehavior();
        fLeft.setPower(0);
        fRight.setPower(0);
        bRight.setPower(0);
        bLeft.setPower(0);
        linearOpMode.sleep(250);
    }

    /**
     * Sets power behavior of all drive motors to brake
     */
    public void setBrakeModeDriveMotors() {
        fLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Toggle foundation moveres
     */
    public void foundationMovers(boolean isExtend) {
        if (isExtend) {
            leftFoundation.setPosition(LEFTFOUNDATION_EXTENDED);
            rightFoundation.setPosition(RIGHTFOUNDATION_EXTENDED);
        } else {
            leftFoundation.setPosition(LEFTFOUNDATION_RETRACTED);
            rightFoundation.setPosition(RIGHTFOUNDATION_RETRACTED);
        }
    }

    public enum Actions {
        EXTEND_OUTTAKE, RETRACT_OUTTAKE, RELEASE_FOUNDATION, START_INTAKE, STOP_INTAKE, EXTEND_FOUNDATION, LOWER_OUTTAKE, RAISE_OUTTAKE_LEVEL1, RAISE_OUTTAKE_LEVEL2
    }

    public void printCoords(double[][] data) {
        telemetry.addLine(Arrays.deepToString(data));
        telemetry.update();
    }

    private boolean hasRaisedOuttake = false;
    private boolean isRaisingOuttake = false;

    public void splineMove(double[][] data, double moveSpeed, double turnSpeed, double slowDownSpeed, double slowDownDistance, double optimalAngle, double angleLockRadians, double angleLockInches, ArrayList<Action> actions) {
        splineMove(data, moveSpeed, turnSpeed, slowDownSpeed, slowDownDistance, optimalAngle, angleLockRadians, angleLockInches, actions, false, 0);
    }

    public void splineMove(double[][] data, double moveSpeed, double turnSpeed, double slowDownSpeed, double slowDownDistance, double optimalAngle, double angleLockRadians, double angleLockInches, ArrayList<Action> actions, boolean isTimeKill, long endTime) {
        splineMove(data, moveSpeed, turnSpeed, slowDownSpeed, slowDownDistance, optimalAngle, angleLockRadians, angleLockInches, actions, isTimeKill, endTime, false, new Point(0, 0));
    }

    public void splineMove(double[][] data, double moveSpeed, double turnSpeed, double slowDownSpeed, double slowDownDistance, double optimalAngle, double angleLockRadians, double angleLockInches, ArrayList<Action> actions, boolean isTimeKill, long endTime, boolean isMecanumPoint, Point mecanumPoint) {
        double posAngle;

        Point[] data2 = new Point[data.length];

        for (int i = 0; i < data.length && linearOpMode.opModeIsActive(); i++) {
            data2[i] = new Point(data[i][0], data[i][1]);
        }

        Point[] pathPoints = CatmullRomSplineUtils.generateSpline(data2, 100, this);

        addSplinePoints(pathPoints);
        addWaypoints(data);

        boolean isMoving = true;
        boolean isStuck = false;

        long lastPosTime = SystemClock.elapsedRealtime();
        Point lastPos = new Point(robotPos.x,robotPos.y);

        int followIndex = 1;
        double angleLockScale;
        double distanceToEnd;
        double distanceToNext = Double.MAX_VALUE;
        double desiredHeading;

        long currentTime;
        long startTime = SystemClock.elapsedRealtime();
        while (linearOpMode.opModeIsActive()) {
            currentTime = SystemClock.elapsedRealtime();

            posAngle = MathFunctions.angleWrap(anglePos + 2 * Math.PI);

            for (int p = pathPoints.length - 1; p >= 0 && linearOpMode.opModeIsActive(); p--) {
                if (Math.hypot(robotPos.x - pathPoints[p].x, robotPos.y - pathPoints[p].y) < 10) {
                    followIndex = p;
                    break;
                }
            }

            distanceToEnd = Math.hypot(robotPos.x - data2[data2.length - 1].x, robotPos.y - data2[data2.length - 1].y);
            distanceToNext = Math.hypot(robotPos.x - pathPoints[followIndex].x, robotPos.y - pathPoints[followIndex].y);
            if (followIndex > 0) {
                desiredHeading = angleWrap(Math.atan2(pathPoints[followIndex].y - pathPoints[followIndex - 1].y, pathPoints[followIndex].x - pathPoints[followIndex - 1].x) + 2 * Math.PI);
            } else {
                desiredHeading = angleWrap(Math.atan2(pathPoints[followIndex + 1].y - pathPoints[followIndex].y, pathPoints[followIndex + 1].x - pathPoints[followIndex].x) + 2 * Math.PI);
            }


            if (desiredHeading == 0) {
                desiredHeading = Math.toRadians(360);
            }
            if (angleLockRadians == 0) {
                angleLockRadians = Math.toRadians(360);
            }

            angleLockScale = Math.abs(angleLockRadians - posAngle) * Math.abs(desiredHeading - angleLockRadians) * 1.8;


            if (distanceToEnd < angleLockInches) {
                updateMovementsToPoint(pathPoints[followIndex].x, pathPoints[followIndex].y, moveSpeed, turnSpeed, optimalAngle, true);

                if (angleLockRadians - posAngle > Math.toRadians(0) && angleLockRadians - posAngle < Math.toRadians(180)) {
                    turnMovement = 1 * angleLockScale;
                } else if (angleLockRadians - posAngle < Math.toRadians(0) || angleLockRadians - posAngle > Math.toRadians(180)) {
                    turnMovement = -1 * angleLockScale;
                } else {
                    turnMovement = 0;
                }
            } else if (isMecanumPoint && Math.hypot(robotPos.x - mecanumPoint.x, robotPos.y - mecanumPoint.y) < 15) {
                updateMovementsToPoint(pathPoints[followIndex].x, pathPoints[followIndex].y, moveSpeed, turnSpeed, optimalAngle, true);
            } else {
                updateMovementsToPoint(pathPoints[followIndex].x, pathPoints[followIndex].y, moveSpeed, turnSpeed, optimalAngle, false);
            }

            if (distanceToEnd < 1) {
                isMoving = false;
            }

            if (distanceToEnd < slowDownDistance) {
                if (slowDownSpeed > moveSpeed) {
                    xMovement *= slowDownSpeed * (slowDownSpeed / moveSpeed);
                    yMovement *= slowDownSpeed * (slowDownSpeed / moveSpeed);
                    turnMovement *= slowDownSpeed * (slowDownSpeed / moveSpeed);
                } else {
                    xMovement *= slowDownSpeed;
                    yMovement *= slowDownSpeed;
                    turnMovement *= slowDownSpeed;
                }
            }

            // go through all actionpoints and see if the robot is near one
            boolean isFinishedAllActions = true;
            if (actions.size() != 0) {
                currentTime = SystemClock.elapsedRealtime();
                for (int i = 0; i < actions.size() && linearOpMode.opModeIsActive(); i++) {
                    Action action = actions.get(i);

                    Point actionPoint = action.getActionPoint();

                    if (action.getActionState() == ActionState.PROCESSING || action.getActionState() == ActionState.PENDING) {
                        isFinishedAllActions = false;
                    }

                    if (action.isExecuteOnEndOfPath()) {
                        if (!isMoving) {
                            action.executeAction(currentTime);
                        }
                    } else if ((Math.hypot(actionPoint.x - robotPos.x, actionPoint.y - robotPos.y) < 10) || (action.getActionState() == ActionState.PROCESSING)) {
                        action.executeAction(currentTime);
                    }
                }
            }

            // Test to see if the robot is stuck, every second
            if((currentTime - lastPosTime) >= 1000){
                if(followIndex != pathPoints.length-1 && (Math.hypot(lastPos.x-robotPos.x, lastPos.y - robotPos.y)) < 1.5){
                    isStuck = true;
                }else{
                    isStuck = false;
                }

                if (isStuck) {
                    brakeRobot();
                    linearOpMode.stop();
                }

                lastPos.x = robotPos.x;
                lastPos.y = robotPos.y;
                lastPosTime = currentTime;
            }

            if (distanceToEnd < 1 && Math.abs(Math.toDegrees(posAngle) - Math.toDegrees(angleLockRadians)) < 5 && isFinishedAllActions) {
                brakeRobot();
                break;
            }

            if (isTimeKill && currentTime - startTime >= endTime) {
                break;
            }


            applyMove();
        }
    }

    public void splineMoveCatMullRomPID(double[][] data, double moveSpeed, double turnSpeed, double slowDownSpeed, double slowDownDistance, double optimalAngle, double angleLockRadians, double angleLockInches, ArrayList<Action> actions, boolean isTimeKill, long endTime) {
        double P = 2;
//        double P = 1.65;
        double I = 0.1;
        double D = 2;

        double integral = 0;
        double previous_error = 0;

        double posAngle;

        Point[] data2 = new Point[data.length];

        for (int i = 0; i < data.length; i++) {
            data2[i] = new Point(data[i][0], data[i][1]);
        }

        Point[] pathPoints = CatmullRomSplineUtils.generateSpline(data2, 1, this);

//
//        addSplinePoints(pathPoints);
//        addWaypoints(data);

        boolean isMoving = true;

        long previousTime = SystemClock.elapsedRealtime();
        int followIndex = 1;
        double angleLockScale;
        double distanceToEnd;
        double distanceToNext;
        double desiredHeading;

        long currentTime = SystemClock.elapsedRealtime();
        long startTime = SystemClock.elapsedRealtime();
        while (linearOpMode.opModeIsActive()) {

            if (isTimeKill && currentTime - startTime >= endTime) {
                brakeRobot();
                isMoving = false;
            }

            posAngle = MathFunctions.angleWrap(anglePos + 2 * Math.PI);

            if (followIndex == pathPoints.length) {
                followIndex--;
            }

            distanceToEnd = Math.hypot(robotPos.x - data2[data2.length - 1].x, robotPos.y - data2[data2.length - 1].y);
            distanceToNext = Math.hypot(robotPos.x - pathPoints[followIndex].x, robotPos.y - pathPoints[followIndex].y);
            desiredHeading = angleWrap(Math.atan2(pathPoints[followIndex].y - pathPoints[followIndex - 1].y, pathPoints[followIndex].x - pathPoints[followIndex - 1].x) + 2 * Math.PI);

            currentTime = SystemClock.elapsedRealtime();

            double error = distanceToEnd; // Error = Target - Actual
            integral += (error) * 0.02; // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
            double derivative = (error - previous_error) * 0.02;
            double rcw = (P * error + I * integral + D * derivative) / 100;


            if (isMoving) {
                telemetry.addLine(" value " + rcw);
                telemetry.addLine(" Delta P " + Math.abs(error - previous_error));

                telemetry.addLine(" P " + (P * error));
                telemetry.addLine(" I " + (integral * I));
                telemetry.addLine(" D " + (D * derivative));
            }

            previous_error = error;


            if (desiredHeading == 0) {
                desiredHeading = Math.toRadians(360);
            }
            if (angleLockRadians == 0) {
                angleLockRadians = Math.toRadians(360);
            }

            angleLockScale = Math.abs(angleLockRadians - posAngle) * Math.abs(desiredHeading - angleLockRadians) * 1.8;


            if (distanceToEnd < angleLockInches) {
                updateMovementsToPoint(pathPoints[followIndex].x, pathPoints[followIndex].y, moveSpeed, turnSpeed, optimalAngle, true);

                if (angleLockRadians - posAngle > Math.toRadians(0) && angleLockRadians - posAngle < Math.toRadians(180)) {
                    turnMovement = 1 * angleLockScale;
                } else if (angleLockRadians - posAngle < Math.toRadians(0) || angleLockRadians - posAngle > Math.toRadians(180)) {
                    turnMovement = -1 * angleLockScale;
                } else {
                    turnMovement = 0;
                }
            } else if (distanceToEnd < 30) {
                updateMovementsToPoint(pathPoints[followIndex].x, pathPoints[followIndex].y, moveSpeed, turnSpeed, optimalAngle, true);
            } else {
                updateMovementsToPoint(pathPoints[followIndex].x, pathPoints[followIndex].y, moveSpeed, turnSpeed, optimalAngle, false);
            }


            if (distanceToEnd < 2) {
                rcw = 1;
                brakeRobot();
                isMoving = false;
            } else if (distanceToNext < 10) {
                followIndex++;
            }

            // go through all actionpoints and see if the robot is near one
            if (actions.size() != 0) {
                currentTime = SystemClock.elapsedRealtime();
                for (int i = 0; i < actions.size(); i++) {
                    Action action = actions.get(i);

                    Point actionPoint = action.getActionPoint();
                    if (action.isExecuteOnEndOfPath()) {
                        if (!isMoving) {
                            action.executeAction(currentTime);
                        }
                    } else if ((Math.hypot(actionPoint.x - robotPos.x, actionPoint.y - robotPos.y) < 10) || (action.getActionState() == ActionState.PROCESSING)) {
                        action.executeAction(currentTime);
                    }
                }
            }

            if (isTimeKill && currentTime - startTime >= endTime) {
                break;
            }

            if (isMoving) {
                applyMove(rcw);
            }
            telemetry.update();

        }
    }

    public void splineMoveTest(Point[] data, double moveSpeed, double turnSpeed, double slowDownSpeed, double slowDownDistance, double optimalAngle, double angleLockRadians, double angleLockInches, ArrayList<Action> actions, boolean isTimeKill, long endTime) {
        Point[] pathPoints = CatmullRomSplineUtils.generateSpline(data, 10, this);

        int pathPointIndex = 0;
        while (linearOpMode.opModeIsActive()) {
            double[][] nextPoint = new double[1][2];
            nextPoint[0][0] = pathPoints[pathPointIndex].x;
            nextPoint[0][1] = pathPoints[pathPointIndex].y;

            double distanceToNextPoint = Math.hypot(robotPos.x - nextPoint[0][0], robotPos.y - nextPoint[0][1]);

            if (distanceToNextPoint < 1) {
                pathPointIndex++;
            }

            updateMovementsToPoint(pathPoints[pathPointIndex].x, pathPoints[pathPointIndex].y, moveSpeed, turnSpeed, optimalAngle, false);
            applyMove();
        }
    }

    public void updateMovementsToPoint(double x, double y, double moveSpeed, double turnSpeed, double optimalAngle, boolean willMecanum) {
        double distanceToTarget = Math.hypot(x - robotPos.x, y - robotPos.y);
        double absoluteAngleToTarget = Math.atan2(y - robotPos.y, x - robotPos.x);

        double relativeAngleToPoint = MathFunctions.angleWrap(absoluteAngleToTarget - anglePos);
        double relativeXToPoint = Math.cos(relativeAngleToPoint) * distanceToTarget;
        double relativeYToPoint = Math.sin(relativeAngleToPoint) * distanceToTarget;
        double relativeTurnAngle = relativeAngleToPoint + optimalAngle;

        if (relativeTurnAngle > Math.PI) {
            relativeTurnAngle -= 2 * Math.PI;
        }

        double xPower = relativeXToPoint / (Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));

        double yPower = 0.4 * relativeYToPoint / (Math.abs(relativeYToPoint) + Math.abs(relativeXToPoint));

        if (willMecanum) {
            yPower = relativeYToPoint / (Math.abs(relativeYToPoint) + Math.abs(relativeXToPoint));
        }

        xMovement = xPower * moveSpeed;
        yMovement = yPower * moveSpeed;
        turnMovement = 5 * Range.clip(relativeTurnAngle / Math.toRadians(360), -1, 1) * turnSpeed;

        if (willMecanum) {
            turnMovement = Range.clip(relativeTurnAngle / Math.toRadians(360), -1, 1) * turnSpeed;
        }
    }

    public void applyMove(double scale) {

        // convert movements to motor powers
        double fLeftPower = (yMovement * 1.414 + turnMovement + xMovement);
        double fRightPower = (-yMovement * 1.414 - turnMovement + xMovement);
        double bLeftPower = (-yMovement * 1.414 + turnMovement + xMovement);
        double bRightPower = (yMovement * 1.414 - turnMovement + xMovement);

        //scale all powers to below 1
        double maxPower = Math.abs(fLeftPower);
        if (Math.abs(bLeftPower) > maxPower) {
            maxPower = Math.abs(bLeftPower);
        }
        if (Math.abs(bRightPower) > maxPower) {
            maxPower = Math.abs(bRightPower);
        }
        if (Math.abs(fRightPower) > maxPower) {
            maxPower = Math.abs(fRightPower);
        }
        double scaleDownAmount = 1.0;
        if (maxPower > 1.0) {
            scaleDownAmount = 1.0 / maxPower;
        }
        fLeftPower *= scaleDownAmount;
        fRightPower *= scaleDownAmount;
        bLeftPower *= scaleDownAmount;
        bRightPower *= scaleDownAmount;

        // apply movement with decelerationScaleFactor
        fLeft.setPower(fLeftPower * scale);
        fRight.setPower(fRightPower * scale);
        bLeft.setPower(bLeftPower * scale);
        bRight.setPower(bRightPower * scale);
    }

    public void applyMove() {

        // convert movements to motor powers
        double fLeftPower = (yMovement * 1.414 + turnMovement + xMovement);
        double fRightPower = (-yMovement * 1.414 - turnMovement + xMovement);
        double bLeftPower = (-yMovement * 1.414 + turnMovement + xMovement);
        double bRightPower = (yMovement * 1.414 - turnMovement + xMovement);

        //scale all powers to below 1
        double maxPower = Math.abs(fLeftPower);
        if (Math.abs(bLeftPower) > maxPower) {
            maxPower = Math.abs(bLeftPower);
        }
        if (Math.abs(bRightPower) > maxPower) {
            maxPower = Math.abs(bRightPower);
        }
        if (Math.abs(fRightPower) > maxPower) {
            maxPower = Math.abs(fRightPower);
        }
        double scaleDownAmount = 1.0;
        if (maxPower > 1.0) {
            scaleDownAmount = 1.0 / maxPower;
        }
        fLeftPower *= scaleDownAmount;
        fRightPower *= scaleDownAmount;
        bLeftPower *= scaleDownAmount;
        bRightPower *= scaleDownAmount;

        // apply movement with decelerationScaleFactor
        fLeft.setPower(fLeftPower);
        fRight.setPower(fRightPower);
        bLeft.setPower(bLeftPower);
        bRight.setPower(bRightPower);
    }

    public void moveToPoint(double x, double y, double moveSpeed, double turnSpeed, double optimalAngle) {

        // find the total distance from the start point to the end point
        double totalDistanceToTarget = Math.hypot(x - robotPos.x, y - robotPos.y);

        double totalTimeSeconds = totalDistanceToTarget / 20;

        // so deceleration works
        this.setDrivetrainMotorModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // start a timer
        long startTime = SystemClock.elapsedRealtime();

        // keep on running this
        while (linearOpMode.opModeIsActive() && SystemClock.elapsedRealtime() - startTime < totalTimeSeconds * 1000
                && linearOpMode.gamepad1.left_stick_y == 0 && linearOpMode.gamepad1.left_trigger == 0 && linearOpMode.gamepad1.right_trigger == 0 && linearOpMode.gamepad1.right_stick_x == 0) {
            // store your current position in variables
            double xPos = robotPos.x;
            double yPos = robotPos.y;
            double anglePos = this.anglePos;

            // find your current distance to target
            double distanceToTarget = Math.hypot(x - xPos, y - yPos);

            // only way to break the loop, if the distance to target is less than 1
            if (distanceToTarget < 1) {
                break;
            }

            // find the absolute angle to target
            double absoluteAngleToTarget = Math.atan2(y - yPos, x - xPos);
            // find the relative angle of the target to the robot
            double relativeAngleToPoint = MathFunctions.angleWrap(absoluteAngleToTarget - anglePos);
            // x distance for the robot to its target
            double relativeXToPoint = Math.cos(relativeAngleToPoint) * distanceToTarget;
            // y distance for the robot to its target
            double relativeYToPoint = Math.sin(relativeAngleToPoint) * distanceToTarget;
            // adds optimal angle
            double relativeTurnAngle = relativeAngleToPoint + optimalAngle;

            // converting the relativeX and relativeY to xPower and yPower
            double xPower = relativeXToPoint / (Math.abs(relativeXToPoint) + Math.abs(relativeYToPoint));
            double yPower = relativeYToPoint / (Math.abs(relativeYToPoint) + Math.abs(relativeXToPoint));

            // find the deceleration
            double decelerationScaleFactor = Range.clip(2 * Math.sqrt(Math.pow(totalDistanceToTarget, 2) - Math.pow(totalDistanceToTarget - distanceToTarget, 2)) / totalDistanceToTarget, -1, 1);

            // get everything into x, y, and turn movements for applyMove
            // the robot can be viewed as something that moves on a coordinate plane
            // that moves in a x and y direction but also has a heading, where it is pointing
            xMovement = xPower * moveSpeed * decelerationScaleFactor;
            yMovement = yPower * moveSpeed * decelerationScaleFactor;
            turnMovement = Range.clip(relativeTurnAngle / Math.toRadians(360),
                    -1, 1) * turnSpeed * decelerationScaleFactor;

            applyMove();
        }
        brakeRobot();
    }

    public void dumpPoints(String directoryName, String tripName) {
        if (!isDebug) {
            return;
        }
        writeToFile("" + directoryName, tripName + "_wayPoints.txt", getWayPoints());
        writeToFile("" + directoryName, tripName + "_odometry.txt", getOdometryPoints());
        writeToFile("" + directoryName, tripName + "_spline.txt", getSplinePoints());
        clearPoints();
    }

    public void clearPoints() {
        splinePoints = new StringBuilder();
        odometryPoints = new StringBuilder();
        waypoints = new StringBuilder();
    }

    public static void writeToFile(String directoryName, String fileName, String data) {
        File captureDirectory = new File(AppUtil.ROBOT_DATA_DIR, "/" + directoryName + "/");
        if (!captureDirectory.exists()) {
            boolean isFileCreated = captureDirectory.mkdirs();
            Log.d("DumpToFile", " " + isFileCreated);
        }
        Log.d("DumpToFile", " hey ");
        File file = new File(captureDirectory, fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            try {
                writer.write(data);
                writer.flush();
                Log.d("DumpToFile", data);
            } finally {
                outputStream.close();
                Log.d("DumpToFile", file.getAbsolutePath());
            }
        } catch (IOException e) {
            RobotLog.ee("TAG", e, "exception in captureFrameToFile()");
        }
    }


    /**
     * GETTERS AND SETTERS SECTION
     */

    public DcMotor getfLeft() {
        return fLeft;
    }

    public void setfLeft(DcMotor fLeft) {
        this.fLeft = fLeft;
    }

    public DcMotor getfRight() {
        return fRight;
    }

    public void setfRight(DcMotor fRight) {
        this.fRight = fRight;
    }

    public DcMotor getbLeft() {
        return bLeft;
    }

    public void setbLeft(DcMotor bLeft) {
        this.bLeft = bLeft;
    }

    public DcMotor getbRight() {
        return bRight;
    }

    public void setbRight(DcMotor bRight) {
        this.bRight = bRight;
    }

    public DcMotor getIntakeLeft() {
        return intakeLeft;
    }

    public void setIntakeLeft(DcMotor intakeLeft) {
        this.intakeLeft = intakeLeft;
    }

    public DcMotor getIntakeRight() {
        return intakeRight;
    }

    public void setIntakeRight(DcMotor intakeRight) {
        this.intakeRight = intakeRight;
    }

    public DcMotor getOuttakeSpool() {
        return outtakeSpool;
    }

    public void setOuttakeSpool(DcMotor outtakeSpool) {
        this.outtakeSpool = outtakeSpool;
    }

    public DcMotor getOuttakeSpool2() {
        return outtakeSpool2;
    }

    public Servo getOuttakeExtender() {
        return outtakeExtender;
    }

    public void setOuttakeExtender(Servo outtakeExtender) {
        this.outtakeExtender = outtakeExtender;
    }

    public Servo getBackClamp() {
        return backClamp;
    }

    public void setBackClamp(Servo backClamp) {
        this.backClamp = backClamp;
    }

    public Servo getFrontClamp() {
        return frontClamp;
    }

    public void setFrontClamp(Servo frontClamp) {
        this.frontClamp = frontClamp;
    }

    public Servo getIntakePusher() {
        return intakePusher;
    }

    public void setIntakePusher(Servo intakePusher) {
        this.intakePusher = intakePusher;
    }

    public Servo getLeftFoundation() {
        return leftFoundation;
    }

    public void setLeftFoundation(Servo leftFoundation) {
        this.leftFoundation = leftFoundation;
    }

    public Servo getRightFoundation() {
        return rightFoundation;
    }

    public void setRightFoundation(Servo rightFoundation) {
        this.rightFoundation = rightFoundation;
    }

    public Point getRobotPos() {
        return robotPos;
    }

    public void setRobotPos(Point robotPos) {
        this.robotPos = robotPos;
    }

    public double getAnglePos() {
        return anglePos;
    }

    public void setAnglePos(double anglePos) {
        this.anglePos = anglePos;
    }

    public BNO055IMU getImu() {
        return imu;
    }

    public DistanceSensor getIntakeStoneDistance() {
        return intakeStoneDistance;
    }

    public void setImu(BNO055IMU imu) {
        this.imu = imu;
    }

    public Orientation getAngles() {
        return angles;
    }

    public void setAngles(Orientation angles) {
        this.angles = angles;
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    public void setHardwareMap(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public LinearOpMode getLinearOpMode() {
        return linearOpMode;
    }

    public void setLinearOpMode(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
    }

    public double getEncoderPerRevolution() {
        return encoderPerRevolution;
    }

    public double getxMovement() {
        return xMovement;
    }

    public void setxMovement(double xMovement) {
        this.xMovement = xMovement;
    }

    public double getyMovement() {
        return yMovement;
    }

    public void setyMovement(double yMovement) {
        this.yMovement = yMovement;
    }

    public double getTurnMovement() {
        return turnMovement;
    }

    public void setTurnMovement(double turnMovement) {
        this.turnMovement = turnMovement;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getOdometryPoints() {
        odometryPoints.insert(0, "x y\n");
        return odometryPoints.toString();
    }

    public String getWayPoints() {
        waypoints.insert(0, "x y vX vY\n");
        return waypoints.toString();
    }

    public String getSplinePoints() {
        splinePoints.insert(0, "x y\n");
        return splinePoints.toString();
    }

    public void addSplinePoints(Point[] data) {
        if (!isDebug) {
            return;
        }
        for (int i = 0; i < data.length; i++) {
            addSplinePoints(data[i].x, data[i].y);
        }
    }

    public void addWaypoints(double[][] data) {
        if (!isDebug) {
            return;
        }
        for (int i = 0; i < data.length; i++) {
            addWaypoints(data[i][0], data[i][1], data[i][2], data[i][3]);
        }
    }

    public void addWaypoints(double x, double y, double vectorX, double vectorY) {
        if (!isDebug) {
            return;
        }
        waypoints.append(x);
        waypoints.append(" ");
        waypoints.append(y);
        waypoints.append(" ");
        waypoints.append(vectorX);
        waypoints.append(" ");
        waypoints.append(vectorY);
        waypoints.append("\n");
    }

    public void addSplinePoints(double x, double y) {
        if (!isDebug) {
            return;
        }
        splinePoints.append(x);
        splinePoints.append(" ");
        splinePoints.append(y);
        splinePoints.append("\n");
    }

    public void addOdometryPoints(double x, double y) {
        if (!isDebug) {
            return;
        }
        odometryPoints.append(x);
        odometryPoints.append(" ");
        odometryPoints.append(y);
        odometryPoints.append("\n");
    }

    public void addOdometryAllData(double leftEncoder, double rightEncoder, double mecanumEncoder, double x, double y, double theta) {
        if (!isDebug) {
            return;
        }
        odometryAllData.append(leftEncoder);
        odometryAllData.append(" ");
        odometryAllData.append(rightEncoder);
        odometryAllData.append(" ");
        odometryAllData.append(mecanumEncoder);
        odometryAllData.append(" ");
        odometryAllData.append(x);
        odometryAllData.append(" ");
        odometryAllData.append(y);
        odometryAllData.append(" ");
        odometryAllData.append(theta);
        odometryAllData.append("\n");
    }

    public String getOdometryAllData() {
        odometryAllData.insert(0, "l r m x y theta\n");
        return odometryAllData.toString();
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setAutoStopIntake(boolean isAutoStopIntake){
        this.isAutoStopIntake = isAutoStopIntake;
    }

    public boolean isAutoStopIntake(){
        return isAutoStopIntake;
    }
}