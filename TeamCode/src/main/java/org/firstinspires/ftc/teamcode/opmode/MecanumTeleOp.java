package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.commands.drive.TeleopDriveCommand;
import org.firstinspires.ftc.teamcode.framework.command.CommandScheduler;
import org.firstinspires.ftc.teamcode.hardware.drive.DriveHardware;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

/**
 * Simple TeleOp demonstrating the WPILib-like framework and basic mecanum drive.
 *
 * - Initializes DriveHardware using motor names compatible with Mark 2
 * - Creates DriveSubsystem and a TeleopDriveCommand bound to gamepad1
 * - Registers the TeleopDriveCommand as the default command for the drivetrain
 * - Runs the CommandScheduler in loop()
 *
 * This OpMode is an example: keep it small and easy to read so you can adapt it.
 */
@TeleOp(name = "Mecanum TeleOp (Mark3)")
public class MecanumTeleOp extends OpMode {

    private DriveHardware driveHardware;
    private DriveSubsystem driveSubsystem;
    private TeleopDriveCommand teleopDriveCommand;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing drive hardware...");
        telemetry.update();

        driveHardware = new DriveHardware();
        driveHardware.init(hardwareMap);

        driveSubsystem = new DriveSubsystem(driveHardware);
        teleopDriveCommand = new TeleopDriveCommand(driveSubsystem, gamepad1);

        // Register as default command for drive
        CommandScheduler.getInstance().setDefaultCommand(driveSubsystem, teleopDriveCommand);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Run the framework scheduler - it will call our TeleopDriveCommand
        CommandScheduler.getInstance().run();

        telemetry.addData("Status", "Running");
        telemetry.update();
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().cancelAll();
    }
}

