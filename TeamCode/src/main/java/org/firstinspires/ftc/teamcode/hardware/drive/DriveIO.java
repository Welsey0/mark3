package org.firstinspires.ftc.teamcode.hardware.drive;

/**
 * Hardware abstraction for drivetrain output.
 *
 * Subsystems depend on this interface instead of concrete FTC hardware classes,
 * which makes the design easier to test and migrate later.
 */
public interface DriveIO {
	void setMotorPowers(double frontLeft, double frontRight, double backLeft, double backRight);

	void stop();
}
