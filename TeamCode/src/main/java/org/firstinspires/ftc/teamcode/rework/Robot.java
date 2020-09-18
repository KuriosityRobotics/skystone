package org.firstinspires.ftc.teamcode.rework;

import android.os.SystemClock;
import android.util.Log;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.rework.Modules.DrivetrainModule;
import org.firstinspires.ftc.teamcode.rework.Modules.Module;
import org.firstinspires.ftc.teamcode.rework.Modules.OdometryModule;
import org.firstinspires.ftc.teamcode.rework.Modules.VelocityModule;
import org.firstinspires.ftc.teamcode.rework.RobotTools.FileDump;
import org.firstinspires.ftc.teamcode.rework.RobotTools.ModuleExecutor;
import org.firstinspires.ftc.teamcode.rework.RobotTools.Shooter.AimBot;
import org.firstinspires.ftc.teamcode.rework.RobotTools.TelemetryDump;

public class Robot {
    // All modules in the robot (remember to update initModules() and updateModules() when adding)
    public DrivetrainModule drivetrainModule;
    public OdometryModule odometryModule;
    public VelocityModule velocityModule;

    // Vuforia
    public VuforiaLocalizer vuforia;
    private final String VUFORIA_KEY = "AbSCRq//////AAAAGYEdTZut2U7TuZCfZGlOu7ZgOzsOlUVdiuQjgLBC9B3dNvrPE1x/REDktOALxt5jBEJJBAX4gM9ofcwMjCzaJKoZQBBlXXxrOscekzvrWkhqs/g+AtWJLkpCOOWKDLSixgH0bF7HByYv4h3fXECqRNGUUCHELf4Uoqea6tCtiGJvee+5K+5yqNfGduJBHcA1juE3kxGMdkqkbfSjfrNgWuolkjXR5z39tRChoOUN24HethAX8LiECiLhlKrJeC4BpdRCRazgJXGLvvI74Tmih9nhCz6zyVurHAHttlrXV17nYLyt6qQB1LtVEuSCkpfLJS8lZWS9ztfC1UEfrQ8m5zA6cYGQXjDMeRumdq9ugMkS";
    WebcamName webcamName;

    // Shooter aiming system
    AimBot aimBot;

    public long currentTimeMilli;

    public HardwareMap hardwareMap;
    private Telemetry telemetry;
    private LinearOpMode linearOpMode;

    public TelemetryDump telemetryDump;
    public FileDump fileDump;

    // New thread that updates modules
    ModuleExecutor moduleExecutor;

    // Array that all modules will be loaded into for easier access
    private Module[] modules;

    // REV Hubs
    private LynxModule revHub1;
    private LynxModule revHub2;

    public final boolean WILL_FILE_DUMP = false;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode linearOpMode) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.linearOpMode = linearOpMode;

        this.telemetryDump = new TelemetryDump(telemetry);
        fileDump = new FileDump();

        initVuforia();

        initHubs();
        initModules();

        aimBot = new AimBot(odometryModule, vuforia);
    }

    public void update() {
        refreshData2();

        currentTimeMilli = SystemClock.elapsedRealtime();

        for(Module module : modules) {
            if(module.isOn()) {
                try {
                    module.update();
                } catch (Exception e){
                    Log.d("Module", "Module couldn't update");
                }
                if(WILL_FILE_DUMP) {
                    module.fileDump();
                }
            }
        }
    }

    public void initModules() {
        // Add individual modules into the array here
        this.drivetrainModule = new DrivetrainModule(this,true);
        this.odometryModule = new OdometryModule(this,true);
        this.velocityModule = new VelocityModule(this,true);

        this.modules = new Module[] {
                this.drivetrainModule, this.odometryModule, this.velocityModule
        };

        // Initialize modules
        for(Module module : modules) {
            module.init();
        }

        // Start the thread for executing modules.
        moduleExecutor = new ModuleExecutor(this, telemetry);
    }

    /**
     * Initializes vuforia for the robot
     */
    public void initVuforia() { // TODO: Ensure this works with new webcam setup
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        try {
            webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

            parameters.vuforiaLicenseKey = VUFORIA_KEY;

            parameters.cameraName = webcamName;
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

            //  Instantiate the Vuforia engine
            this.vuforia = ClassFactory.getInstance().createVuforia(parameters);

            vuforia.enableConvertFrameToBitmap();
        } catch (Exception e) {
            throw new Error("Vuforia intialization failed. Exception: " + e);
        }
    }

    /**
     * Starts running the loop that updates modules
     */
    public void startModules() {
        moduleExecutor.start();
    }

    private void initHubs() {
        try {
            revHub1 = hardwareMap.get(LynxModule.class, "Expansion Hub 3");
            revHub1.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            revHub2 = hardwareMap.get(LynxModule.class, "Expansion Hub 2");
            revHub2.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        } catch (Exception e) {
            throw new Error("One or more of the REV hubs could not be found. More info: " + e);
        }
    }

    public void refreshData1() {
        revHub1.getBulkData();
    }

    public void refreshData2() {
        revHub2.getBulkData();
    }

    public DcMotor getDcMotor(String name) {
        try {
            return hardwareMap.dcMotor.get(name);
        } catch (IllegalArgumentException exception) {
            throw new Error("Motor with name " + name + " could not be found. Exception: " + exception);
        }
    }

    public Servo getServo(String name) {
        try {
            return hardwareMap.servo.get(name);
        } catch (IllegalArgumentException exception) {
            throw new Error("Servo with name " + name + " could not be found. Exception: " + exception);
        }
    }

    public boolean isOpModeActive() {
        return linearOpMode.opModeIsActive();
    }

    public boolean isStopRequested(){
        return linearOpMode.isStopRequested();
    }
}
