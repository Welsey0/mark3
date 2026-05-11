package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.framework.command.Subsystem;
import org.firstinspires.ftc.teamcode.hardware.drive.DriveIO;
import org.firstinspires.ftc.teamcode.robot.Constants;

/**
 * High-level drivetrain behavior.
 *
 * This subsystem converts desired translation/rotation commands into mecanum
 * wheel powers and delegates low-level outputs to DriveIO.
 *
 * The field-centric math intentionally mirrors the Mark 2 implementation:
 * x = dx * cos(h) + dy * sin(h)
 * y = dy * cos(h) - dx * sin(h)
 * wheel mapping: FL(y+x+r), FR(y-x-r), BL(y-x+r), BR(y+x-r)
 */
public class DriveSubsystem implements Subsystem {

	private final DriveIO io;

	public DriveSubsystem(DriveIO io) {
		this.io = io;
	}

	/**
	 * Drive with explicit heading compensation (field-centric when heading != 0).
	 *
	 * @param dx strafe command (+ left), from left stick x in Mark 2
	 * @param dy forward command (+ forward), from -left stick y in Mark 2
	 * @param rx rotation command (+ clockwise), from -right stick x in Mark 2
	 * @param headingRadians current robot heading in radians
	 *
	 * Values expected in -1..1 range. Will be clipped automatically.
	 */
	public void driveWithHeading(double dx, double dy, double rx, double headingRadians) {
		double[] powers = calculateWheelPowers(dx, dy, rx, headingRadians);

		if (io != null) {
			io.setMotorPowers(powers[0], powers[1], powers[2], powers[3]);
		}
	}

	/**
	 * Pure math helper for mecanum power computation.
	 *
	 * Inputs are expected in -1..1 and should already be deadbanded/scaled by caller.
	 * Returns powers ordered as: frontLeft, frontRight, backLeft, backRight.
	 */
	public double[] calculateWheelPowers(double dx, double dy, double rx, double headingRadians) {
		double x;
		double y;
		if (Constants.Drive.FIELD_CENTRIC_ENABLED) {
			double correctedHeading = headingRadians + Constants.Drive.HEADING_OFFSET_RADIANS;
			double cos = Math.cos(correctedHeading);
			double sin = Math.sin(correctedHeading);
			x = dx * cos + dy * sin;
			y = dy * cos - dx * sin;
		} else {
			x = dx;
			y = dy;
		}

		// Match Mark 2 normalization style exactly.
		double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);
		double frontLeft = (y + x + rx) / denominator;
		double frontRight = (y - x - rx) / denominator;
		double backLeft = (y - x + rx) / denominator;
		double backRight = (y + x - rx) / denominator;

		return new double[]{frontLeft, frontRight, backLeft, backRight};
	}

	/**
	 * Convenience overload for robot-centric control.
	 */
	public void setDrivePower(double vx, double vy, double omega) {
		driveWithHeading(vx, vy, omega, 0.0);
	}

	public void stop() {
		if (io != null) io.stop();
	}
}

