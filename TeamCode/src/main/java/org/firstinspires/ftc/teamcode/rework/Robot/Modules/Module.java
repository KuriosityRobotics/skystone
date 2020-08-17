package org.firstinspires.ftc.teamcode.rework.Robot.Modules;


import java.util.HashMap;

public interface Module {
    /**
     * Initializes the module. This includes setting up all motors/servos
     * */
    public void init();

    /**
     * Updates the module, executing all the tasks it should complete on every iteration,
     * utilizing the module's states. This separate method allows for each module to be updated
     * on a different thread from where the states are set.
     */
    public void update();

    /**
     * Gets the current state, such as position variables etc
     * @return
     */
    public HashMap<String, String> getState();
}
