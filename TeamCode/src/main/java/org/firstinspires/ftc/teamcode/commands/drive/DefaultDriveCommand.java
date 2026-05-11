package org.firstinspires.ftc.teamcode.commands.drive;

import org.firstinspires.ftc.teamcode.framework.command.Command;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

import java.util.Collections;
import java.util.Set;

/**
 * Default teleop drive behavior.
 *
 * This command should usually run whenever no other drive command is active.
 * Typical responsibilities:
 * - read driver input
 * - convert input to drive power
 * - apply deadbands and scaling
 *
 * Minimal implementation: calls the DriveSubsystem with placeholder values.
 */
public class DefaultDriveCommand implements Command {

	private final DriveSubsystem drive;

	public DefaultDriveCommand(DriveSubsystem drive) {
		this.drive = drive;
	}

	@Override
	public void initialize() {
		// no-op for default
	}

	@Override
	public void execute() {
		// placeholder: in TeleOp this would read gamepad input
		drive.setDrivePower(0.0, 0.0, 0.0);
	}

	@Override
	public boolean isFinished() { return false; }

	@Override
	public void end(boolean interrupted) {
		// stop motors when ending
		drive.setDrivePower(0.0, 0.0, 0.0);
	}

	@Override
	public Set<org.firstinspires.ftc.teamcode.framework.command.Subsystem> getRequirements() {
		return Collections.<org.firstinspires.ftc.teamcode.framework.command.Subsystem>singleton(drive);
	}
}

