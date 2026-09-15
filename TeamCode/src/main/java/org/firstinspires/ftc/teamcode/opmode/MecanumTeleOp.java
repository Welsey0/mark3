package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.commands.drive.TeleopDriveCommand;
import org.firstinspires.ftc.teamcode.framework.CommandScheduler;
import org.firstinspires.ftc.teamcode.hardware.movement.DriveHardware;
import org.firstinspires.ftc.teamcode.hardware.sensors.IMUHardware;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

/**
 * Demonstration OpMode implementing basic mecanum drive.
 */
@TeleOp(name = "Mecanum TeleOp (Mark3)")
public class MecanumTeleOp extends OpMode {

    private DriveHardware driveHardware;
    private IMUHardware imuHardware;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing drive hardware...");
        telemetry.update();

        driveHardware = new DriveHardware();
        driveHardware.init(hardwareMap);

        imuHardware = new IMUHardware();
        imuHardware.init(hardwareMap);

        DriveSubsystem driveSubsystem = new DriveSubsystem(driveHardware);
        TeleopDriveCommand teleopDriveCommand = new TeleopDriveCommand(driveSubsystem, gamepad1, imuHardware::getHeadingRadians);
        CommandScheduler.getInstance().schedule(teleopDriveCommand);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        telemetry.addData("Drive Mode", org.firstinspires.ftc.teamcode.robot.Constants.Drive.FIELD_CENTRIC_ENABLED ? "Field-centric" : "Robot-centric");
        telemetry.addData("Slow Mode", gamepad1.right_bumper ? "ON" : "OFF");
        if (driveHardware != null) {
            telemetry.addData("Heading(rad)", imuHardware.getHeadingRadians());
        }
        telemetry.addData("Status", "Running");
        telemetry.update();
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().cancelAll();
    }
}