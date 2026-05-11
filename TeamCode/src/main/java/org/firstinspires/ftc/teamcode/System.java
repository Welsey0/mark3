package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.DriveController;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.DriveService;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Motorcode;
import org.firstinspires.ftc.teamcode.Subsystems.Hardware;
import org.firstinspires.ftc.teamcode.Subsystems.Summaries.RobotSummary;
import org.firstinspires.ftc.teamcode.Subsystems.Summaries.SystemSummary;
import org.firstinspires.ftc.teamcode.Subsystems.Telemetry.TelemetryHub;

@SuppressWarnings("unused")
public class System {
    public final HardwareMap hardwareMap;
    public final Gamepad gamepad1;
    public final Gamepad gamepad2;

    public final Hardware hardware;
    public final DriveController driveController;
    public final DriveService driveService;
    public final TelemetryHub telemetry;

    private double currentHeading;
    private double lastDriveSpeed;
    private Motorcode lastDriveCommand = Motorcode.zero();

    public System(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2, Telemetry driverTelemetry, Telemetry dashboardTelemetry) {
        this.hardwareMap = hardwareMap;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        this.hardware = new Hardware(hardwareMap);
        this.driveController = new DriveController();
        this.driveService = new DriveService(hardware);
        this.telemetry = new TelemetryHub(driverTelemetry, dashboardTelemetry);

        TelemetryHub.bindGlobal(this.telemetry);
    }

    public void beginBoot() {
        telemetry.beginBoot();
        telemetry.addBootCheck("Config", true, "Dashboard config loaded");
    }

    public void initHardware() {
        hardware.init();
        telemetry.addBootCheck("Drive motors", hardware.isDriveReady(), hardware.getDriveHealthSummary());
        telemetry.addBootCheck("Drive service", driveService != null, "Ready");
    }

    public void finishBoot() {
        telemetry.finishBoot();
    }

    public void bootLoop() {
        telemetry.update(gamepad1, buildSummary());
    }

    public void teleOpLoop() {
        driveFromGamepad();
        telemetry.update(gamepad1, buildSummary());
    }

    public void driveFromGamepad() {
        lastDriveSpeed = gamepad1.left_bumper ? BotConfig.Drive.slowSpeed : BotConfig.Drive.defaultSpeed;
        lastDriveCommand = driveController.driveFromJoy(gamepad1, currentHeading, lastDriveSpeed);
        driveService.apply(lastDriveCommand);
    }

    public void setHeading(double radians) {
        currentHeading = radians;
    }

    public double getHeading() {
        return currentHeading;
    }

    public double getLastDriveSpeed() {
        return lastDriveSpeed;
    }

    public Motorcode getLastDriveCommand() {
        return lastDriveCommand;
    }

    public RobotSummary buildSummary() {
        return SystemSummary.build(this);
    }

    public void notifyInfo(String title, String message) {
        TelemetryHub.postInfo(title, message);
    }

    public void notifyWarning(String title, String message) {
        TelemetryHub.postWarning(title, message);
    }

    public void notifyError(String title, String message) {
        TelemetryHub.postError(title, message);
    }

    public void stopAll() {
        driveService.apply(Motorcode.zero());
        TelemetryHub.unbindGlobal();
    }
}