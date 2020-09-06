package org.firstinspires.ftc.teamcode.rework;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;


public class AsyncThread extends AsyncTask<Void, Boolean, Boolean> {
    Robot robot;
    public AsyncThread(Robot robot) {
        this.robot = robot;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        long lastUpdateTime = SystemClock.elapsedRealtime();
        long currentTime;

        while (robot.isOpModeActive()) {
            //robot.update();

            currentTime = SystemClock.elapsedRealtime();
            robot.telemetryDump.addHeader("---Async---");
            robot.telemetryDump.addData("Async thread loop time: ", (currentTime - lastUpdateTime));
            lastUpdateTime = currentTime;
            robot.odometryModule.update();
            robot.odometryModule.telemetry();
            robot.drivetrainModule.update();
            robot.drivetrainModule.telemetry();

//            if(robot.isStopRequested() && robot.WILL_FILE_DUMP){
//                robot.fileDump.writeFilesToDevice();
//            }

            //robot.telemetryDump.update();
        }
        return true;
    }

    protected void onPostExecute(Boolean result) {
        if (result) {

        }
    }

}