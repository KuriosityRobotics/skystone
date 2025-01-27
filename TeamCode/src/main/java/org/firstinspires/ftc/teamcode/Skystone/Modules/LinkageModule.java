package org.firstinspires.ftc.teamcode.Skystone.Modules;

import org.firstinspires.ftc.teamcode.Skystone.HardwareCollection;
import org.firstinspires.ftc.teamcode.Skystone.Robot;

import static org.firstinspires.ftc.teamcode.Skystone.Constants.*;

@Deprecated
public class LinkageModule {

    public boolean extended;

    public StringBuilder linkageData;

    public LinkageModule(){
        extended = false;

        linkageData = new StringBuilder();
        linkageData.append("exended");
        linkageData.append("\n");
    }

    public void update(Robot robot, HardwareCollection hardwareCollection) {
        if (robot.isDebug){
            linkageData.append(extended);
            linkageData.append("\n");
        }

        if (extended){
            hardwareCollection.outtakeExtender.setPosition(OUTTAKE_SLIDE_EXTENDED);
        } else {
            hardwareCollection.outtakeExtender.setPosition(OUTTAKE_SLIDE_RETRACTED);
        }
    }

}
