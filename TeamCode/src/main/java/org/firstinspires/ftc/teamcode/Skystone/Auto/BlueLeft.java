package org.firstinspires.ftc.teamcode.Skystone.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Skystone.MathFunctions;
import org.firstinspires.ftc.teamcode.Skystone.MotionProfiler.PathPoints;
import org.firstinspires.ftc.teamcode.Skystone.MotionProfiler.Point;
import org.firstinspires.ftc.teamcode.Skystone.Vision;

@Autonomous(name="BlueLeft", group ="LinearOpmode")
public class BlueLeft extends AutoBase {
    @Override
    public void runOpMode() {
        initLogic();
        Vision tensorflow = new Vision(robot);


        waitForStart();
        position2D.startOdometry();

    }
}