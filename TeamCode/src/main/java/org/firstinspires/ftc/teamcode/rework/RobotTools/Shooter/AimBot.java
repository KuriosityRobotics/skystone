package org.firstinspires.ftc.teamcode.rework.RobotTools.Shooter;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.rework.AutoTools.Point;
import org.firstinspires.ftc.teamcode.rework.Modules.OdometryModule;
import org.firstinspires.ftc.teamcode.rework.Robot;

public class AimBot {
    Robot robot;
    OdometryModule odometry;
    VuforiaLocalizer vuforia;

    // Position of goals, all in inches, from the origin of front blue corner (audience, left)
    private static final double TOP_GOAL_CENTER_HEIGHT = 33.0 + (5.0 / 2);
    private static final double MIDDLE_GOAL_CENTER_HEIGHT = 21.0 + (12.0 / 2);
    private static final double BOTTOM_GOAL_CENTER_HEIGHT = 13.0 + (8.0 / 2);
    private static final double BLUE_GOAL_CENTER_X = 23.0 + (24.0 / 2);
    private static final double RED_GOAL_CENTER_X = 23.0 + (23.5 * 3) + (24.0 / 2);
    private static final double GOAL_CENTER_Y = 6 * 24.0 - (0.5 * 2); // Full length of 6 tiles, minus .5" for edge tile's tabs.

    public AimBot(OdometryModule odometry, VuforiaLocalizer vuforia, Robot robot) {
        this.odometry = odometry;
        this.vuforia = vuforia;
        this.robot = robot;
    }

    /**
     * Aim the shooter at the target specified.
     *
     * @param target The target to aim at.
     */
    public void aimShooter(TowerGoal target) {
        // TODO: Turn robot to face target, give commands to shooter
    }

    /**
     * Calculate what heading we have to turn the robot to hit the target, incorporating vision.
     *
     * @param target The target to aim at.
     */
    private double calculateHeading(TowerGoal target) {
        // TODO: Magic to calculate where to aim the turret, incorporating odometry and a visual check"

        return 0;
    }

    /**
     * Calculate what heading we have to turn the robot to hit the target, incorporating vision.
     *
     * @param point The point to aim at.
     */
    private double calculateHeading(Point point) {
        // TODO: Magic to calculate where to aim the turret, incorporating odometry and a visual check"

        return 0;
    }

    /**
     * Calculate the speed and angle to set the shooter to in order to land the ring into the specified goal.
     *
     * @param target The target to aim at.
     * @return A double[] where the first value is the speed of the shooter and the second is the vertical angle
     *  of the shooter's flap.
     */
    private double[] calculateShooterSpeed(TowerGoal target) {
        return new double[2];
    }

    /**
     * Calculate the speed and angle to set the shooter to in order to land the ring into the specified goal.
     *
     * @param point The point to aim at.
     * @return A double[] where the first value is the speed of the shooter and the second is the vertical angle
     *  of the shooter's flap.
     */
    private double[] calculateShooterSpeed(Point point) {
        return new double[2];
    }
}
