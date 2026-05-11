package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.commands.drive.TeleopDriveCommand;
import org.firstinspires.ftc.teamcode.framework.command.CommandScheduler;
import org.firstinspires.ftc.teamcode.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

public class RobotContainer {
	public final RobotHardware hardware;
	public final DriveSubsystem driveSubsystem;

	private TeleopDriveCommand teleopDriveCommand;

	/**
	 * Build a RobotContainer for real hardware use.
	 */
	public RobotContainer(RobotHardware hardware) {
		this.hardware = hardware;
		this.driveSubsystem = new DriveSubsystem(hardware.drive);
	}

	/**
	 * Test-friendly constructor with no hardware outputs.
	 */
	public RobotContainer() {
		this.hardware = null;
		this.driveSubsystem = new DriveSubsystem(null);
	}

	/**
	 * Configure TeleOp bindings and register defaults.
	 */
	public void configureTeleop(Gamepad gamepad1) {
		if (hardware != null && Constants.Drive.USE_IMU_FOR_HEADING) {
			teleopDriveCommand = new TeleopDriveCommand(driveSubsystem, gamepad1, hardware.drive::getHeadingRadians);
		} else {
			teleopDriveCommand = new TeleopDriveCommand(driveSubsystem, gamepad1);
		}
		CommandScheduler.getInstance().setDefaultCommand(driveSubsystem, teleopDriveCommand);
	}
}

