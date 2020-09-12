package org.firstinspires.ftc.teamcode.rework.RobotTools;

import android.os.SystemClock;
import android.util.Log;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class FileDump {
    HashMap<String,FileData> files;
    long startTime;

    public FileDump(){
        startTime = SystemClock.elapsedRealtime();
        files = new HashMap<>();
    }

    public synchronized void addData(String fileName, String data){
        if(files.containsKey(fileName)) {
            files.get(fileName).text.append(data).append("\n");
        }else{
            files.put(fileName,new FileData("",new StringBuilder(data).append("\n")));
        }
    }

    public synchronized void writeFilesToDevice(){
        for(String key : files.keySet()){
            writeToFile(Long.toString(startTime),key,files.get(key).getEntireFile());
        }
    }

    public synchronized void setHeader(String fileName, String s){
        if(!files.containsKey(fileName)){
            files.put(fileName,new FileData(s + "\n",new StringBuilder()));
        }
        if(files.get(fileName).header.equals("")){
            files.get(fileName).header = s + "\n";
        }
    }

    public void writeToFile(String directoryName, String fileName, String data) {
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
}

class FileData{
    public StringBuilder text;
    public String header;

    public FileData(String header, StringBuilder text){
        this.header = header;
        this.text = text;
    }

    public String getEntireFile(){
        return text.insert(0,header).toString();
    }
}
