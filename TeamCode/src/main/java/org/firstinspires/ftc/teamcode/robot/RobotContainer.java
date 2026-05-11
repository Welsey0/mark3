package org.firstinspires.ftc.teamcode.robot;

public class RobotContainer {
	// Minimal wiring: create subsystems and default commands here so OpModes
	// or tests can instantiate RobotContainer and get a working baseline.

	public final org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem driveSubsystem;
	public final org.firstinspires.ftc.teamcode.commands.drive.DefaultDriveCommand defaultDriveCommand;

	public RobotContainer() {
		// construct subsystems with null hardware for cases where RobotContainer is
		// used outside of an OpMode (tests, static contexts). Real OpModes should
		// construct hardware and subsystems explicitly.
		this.driveSubsystem = new org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem(null);

		// construct commands
		this.defaultDriveCommand = new org.firstinspires.ftc.teamcode.commands.drive.DefaultDriveCommand(driveSubsystem);

		// register default command with the scheduler
		org.firstinspires.ftc.teamcode.framework.command.CommandScheduler.getInstance().setDefaultCommand(driveSubsystem, defaultDriveCommand);
	}
}

