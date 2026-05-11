package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.framework.command.Subsystem;

/**
 * High-level drivetrain behavior.
 *
 * This subsystem should:
 * - manage drive state
 * - expose drive commands
 * - handle odometry or localization updates if needed
 * - own any drive-specific safety logic
 *
 * It should not know about button bindings or OpMode lifecycle.
 *
 * Minimal implementation: contains a simple API used by commands and
 * leaves actual hardware wiring to the hardware layer.
 */
public class DriveSubsystem implements Subsystem {

	public DriveSubsystem() {
		// construct internal state here (no hardware access in this minimal stub)
	}

	/**
	 * Set the drive power using any coordinate system you prefer.
	 * This is a minimal placeholder; in a real robot implementation this
	 * would forward to a hardware wrapper (DriveHardware) that controls motors.
	 *
	 * @param vx forward velocity / power
	 * @param vy strafe velocity / power
	 * @param omega rotational velocity / power
	 */
	public void setDrivePower(double vx, double vy, double omega) {
		// TODO: forward to DriveHardware when available
	}

	/**
	 * Periodic update that might be called by the scheduler if desired.
	 */
	public void periodic() {
		// update odometry or safety checks here
	}
}

