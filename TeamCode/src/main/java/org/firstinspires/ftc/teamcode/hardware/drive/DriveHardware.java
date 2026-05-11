package org.firstinspires.ftc.teamcode.hardware.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.robot.Constants;

/**
 * Hardware wrapper for the drivetrain.
 *
 * Owns the raw FTC devices for driving and exposes a minimal API for
 * setting motor powers. This keeps direct HardwareMap calls in one place.
 */
public class DriveHardware {
	public DcMotor frontLeft;
	public DcMotor frontRight;
	public DcMotor backLeft;
	public DcMotor backRight;

	public void init(HardwareMap hardwareMap) {
		frontLeft = hardwareMap.dcMotor.get(Constants.Drive.FRONT_LEFT);
		backLeft = hardwareMap.dcMotor.get(Constants.Drive.BACK_LEFT);
		frontRight = hardwareMap.dcMotor.get(Constants.Drive.FRONT_RIGHT);
		backRight = hardwareMap.dcMotor.get(Constants.Drive.BACK_RIGHT);

		// Set directions to match the Mark2 configuration (FR/BR reversed)
		frontLeft.setDirection(DcMotor.Direction.FORWARD);
		backLeft.setDirection(DcMotor.Direction.FORWARD);
		frontRight.setDirection(DcMotor.Direction.REVERSE);
		backRight.setDirection(DcMotor.Direction.REVERSE);

		// Default motor behavior (configurable)
		if (org.firstinspires.ftc.teamcode.robot.Constants.Drive.ZERO_POWER_BRAKE) {
			frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		} else {
			frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
			backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
			frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
			backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
		}
	}

	/**
	 * Set motor powers in the order: frontLeft, frontRight, backLeft, backRight.
	 * Values should be in [-1, 1].
	 */
	public void setMotorPowers(double fl, double fr, double bl, double br) {
		frontLeft.setPower(fl);
		frontRight.setPower(fr);
		backLeft.setPower(bl);
		backRight.setPower(br);
	}

	public void stop() {
		setMotorPowers(0,0,0,0);
	}
}