package org.firstinspires.ftc.teamcode.FTC2021.Modules;

import org.firstinspires.ftc.teamcode.FTC2021.HardwareCollection;
import org.firstinspires.ftc.teamcode.FTC2021.Robot;

public class OuttakeModule {
    public OuttakeStateModule outtakeStateModule;
    public LinkageModule linkageModule;
    public ClawModule clawModule;
    public SpoolModule spoolModule;

    public OuttakeModule(){
        outtakeStateModule = new OuttakeStateModule();
        linkageModule = new LinkageModule();
        clawModule = new ClawModule();
        spoolModule = new SpoolModule();
    }

    public void update(Robot robot, HardwareCollection hardwareCollection){
        outtakeStateModule.update(robot,hardwareCollection);
        linkageModule.update(robot, hardwareCollection);
        clawModule.update(robot, hardwareCollection);
        spoolModule.update(robot, hardwareCollection);
    }
}