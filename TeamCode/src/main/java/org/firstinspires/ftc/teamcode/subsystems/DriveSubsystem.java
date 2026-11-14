package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.framework.TelemetrySource;
import org.firstinspires.ftc.teamcode.hardware.movement.DriveHardware;
import org.firstinspires.ftc.teamcode.robot.Constants;

/**
 * High-level drivetrain behavior.
 * <p>
 * This subsystem converts desired translation/rotation commands into mecanum
 * wheel powers and delegates low-level outputs to DriveHardware.
 */
public class DriveSubsystem implements Subsystem, TelemetrySource {

	private final DriveHardware hardware;

	public DriveSubsystem(DriveHardware hardware) {
		if (hardware == null) {
			throw new RobotInitializationException("DriveSubsystem", "DriveHardware is null");
		}
		this.hardware = hardware;
	}
	private boolean complete;
	private String error;
	@Override
	public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Autonomous state", error != null ? "ERROR" : complete ? "COMPLETE" : "RUNNING");
		if (error != null) telemetry.addData("Autonomous error", error);
		telemetry.addData("m1", m1);
		telemetry.addData("m2", m2);
		telemetry.addData("m3", m3);
		telemetry.addData("m4", m4);
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
	double m1;
	double m2;
	double m3;
	double m4;
	public void driveWithHeading(double dx, double dy, double rx, double headingRadians) {
		double[] powers = calculateWheelPowers(dx, dy, rx, headingRadians);
		m1 = powers[0];
		m2 = powers[1];
		m3 = powers[2];
		m4 = powers[3];
		if (hardware != null) {
			hardware.setMotorPowers(powers[0], powers[1], powers[2], powers[3]);
		}
	}

	/**
	 * Drive with the configured slow-mode scaling applied by the drivetrain.
	 * The opmode supplies the already-remapped action state.
	 */
	public void driveWithHeading(double dx, double dy, double rx,
	                             double headingRadians, boolean slowMode) {
		double scale = slowMode ? Constants.Drive.SLOW_MODE_SCALE : 1.0;
		driveWithHeading(dx * scale, dy * scale, rx * scale, headingRadians);
	}

	/**
	 * Math helper for mecanum power computation.
	 * <p>
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

		double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);
		double frontLeft = (y + x + rx) / denominator;
		double frontRight = (y - x - rx) / denominator;
		double backLeft = (y - x + rx) / denominator;
		double backRight = (y + x - rx) / denominator;

		return new double[]{frontLeft, frontRight, backLeft, backRight};
	}

	/**
	 * Convenience override for robot-centric control.
	 */
	public void driveWithoutHeading(double dx, double dy, double rx) {
		driveWithHeading(dx, dy, rx, 0.0);
	}

	public void stop() {
		if (hardware != null) hardware.stop();
	}
}
