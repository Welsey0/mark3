package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.framework.command.Subsystem;
import org.firstinspires.ftc.teamcode.hardware.drive.DriveHardware;
import org.firstinspires.ftc.teamcode.framework.util.MathUtil;

/**
 * High-level drivetrain behavior.
 *
 * This subsystem converts desired velocities into motor powers and
 * delegates low-level control to DriveHardware.
 */
public class DriveSubsystem implements Subsystem {

	private final DriveHardware hardware;

	public DriveSubsystem(DriveHardware hardware) {
		this.hardware = hardware;
	}

	/**
	 * Set the desired drive power in robot-centric coordinates.
	 * vx: forward (+)
	 * vy: strafe left (+)
	 * omega: clockwise rotation (+)
	 *
	 * Values expected in -1..1 range. Will be clipped automatically.
	 */
	public void setDrivePower(double vx, double vy, double omega) {
		// apply deadbands (value is configured in Constants)
		double deadband = org.firstinspires.ftc.teamcode.robot.Constants.Drive.JOYSTICK_DEADBAND;
		vx = MathUtil.applyDeadband(vx, deadband);
		vy = MathUtil.applyDeadband(vy, deadband);
		omega = MathUtil.applyDeadband(omega, deadband);

		// Mecanum wheel mixing (matches Mark2 mapping)
		double fl = vy + vx + omega; // frontLeft
		double fr = vy - vx - omega; // frontRight
		double bl = vy - vx + omega; // backLeft
		double br = vy + vx - omega; // backRight

		// normalize so no value exceeds 1
		double max = Math.max(Math.max(Math.abs(fl), Math.abs(fr)), Math.max(Math.abs(bl), Math.abs(br)));
		if (max > 1.0) {
			fl /= max;
			fr /= max;
			bl /= max;
			br /= max;
		}

		// forward to hardware
		if (hardware != null) {
			hardware.setMotorPowers(fl, fr, bl, br);
		}
	}

	public void stop() {
		if (hardware != null) hardware.stop();
	}
}

