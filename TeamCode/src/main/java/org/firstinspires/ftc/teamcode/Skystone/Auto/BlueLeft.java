package org.firstinspires.ftc.teamcode.Skystone.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Skystone.MotionProfiler.PathPoints;

@Autonomous(name="BlueLeft", group ="LinearOpmode")
public class BlueLeft extends AutoBase {
    // transport two skystones and other stones if time permits

    // park in building zone (even if other team is in corner)
    @Override
    public void runOpMode() {
        initLogic();
        robot.finalTurn(90,1);
        robot.finalTurn(0,1);
        robot.finalTurn(-90,1);
        robot.moveToPoint(20,0,1,1,Math.toRadians(45));
        if(true){
            return;
        }
        robot.moveToPoint(11.5 ,0,1,1,Math.toRadians(0));

        robot.moveToPoint(48, 0, 1, 1, Math.toRadians(0));

        double[][] points = {{48.0,0.0},{10.0,10.0},{9.0,35.0},{35.0,60.0},{48.0,80,0}};

        double value = 11;
//        while(opModeIsActive() && !gamepad1.a){
//            if(gamepad1.dpad_up){
//                value+=0.0005;
//            }else if(gamepad1.dpad_down){
//                value-=0.0005;
//            }
//            telemetry.addLine("Value: "+ value);
//            telemetry.update();
//        }

        PathPoints pathPoints = new PathPoints(points,value);
        robot.moveFollowCurve(pathPoints.targetPoints);

//
//        // Determine position of SkyStone
//        int vuforiaPosition = robot.detectTensorflow();
//
//        // Go to the Skystone and intake it
//        goToSkystone(vuforiaPosition,0);
//
//        // Move to the other side of the skybridge
//        robot.moveToPoint(0, 47, 1, 0, Math.toRadians(20));
//
//        telemetry.addLine("return");
//        telemetry.update();
//        telemetry.addLine("DONEEEEE");
//        telemetry.update();
//
//        // Deposit the stone, then retract the outtake
//        depositStone();
//        retractOuttake();
//
//        // Move to the second set of three stones, in anticipation of picking up the second Skystone
//        robot.moveToPoint(0,24,1,1,Math.toRadians(0));
//
//        // Go to the Skystone and intake it
//        goToSkystone(vuforiaPosition,1);
//
//        // Move to the other side of the Skybridge
//        robot.moveToPoint(0, 47, 1, 0, Math.toRadians(20));
//
//
//        // Deposit the Skystone and retract the outtake arm
//        depositStone();
//        retractOuttake();
//
//        // Return for more Stones
//        // TODO: tensorflow for more stones
    }
}