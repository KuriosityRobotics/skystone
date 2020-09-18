package org.firstinspires.ftc.teamcode.rework.RobotTools.Shooter;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.rework.Modules.OdometryModule;

public class AimBot {
    OdometryModule odometry;
    VuforiaLocalizer vuforia;

    // Position of goals, all in inches, from the origin of front blue corner (audience, left)
    private static final double TOP_GOAL_CENTER_HEIGHT = 33.0 + (5.0 / 2);
    private static final double MIDDLE_GOAL_CENTER_HEIGHT = 21.0 + (12.0 / 2);
    private static final double BOTTOM_GOAL_CENTER_HEIGHT = 13.0 + (8.0 / 2);
    private static final double BLUE_GOAL_CENTER_X = 23.0 + (24.0 / 2);
    private static final double RED_GOAL_CENTER_X = 23.0 + (23.5 * 3) + (24.0 / 2);
    private static final double GOAL_CENTER_Y = 6 * 24.0 - (0.5 * 2); // Full length of 6 tiles, minus .5" for edge tile's tabs.

    public AimBot(OdometryModule odometry, VuforiaLocalizer vuforia) {
        this.odometry = odometry;
        this.vuforia = vuforia;
    }

    /**
     * Calculate the pitch and yaw for the turret to target in order to get into the specified goal.
     *
     * @return A double array with the pitch, then the yaw
     */
    public double[] calculateTurretPitchYaw(TowerGoal target) {
        // TODO: Magic to calculate where to aim the turret, incorporating odometry and a visual check
        return new double[2];
    }
}
