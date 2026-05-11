package org.firstinspires.ftc.teamcode.hardware.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.robot.Constants;

/**
 * Hardware wrapper for the drivetrain.
 *
 * Owns the raw FTC devices for driving and exposes a minimal API for
 * setting motor powers. This keeps direct HardwareMap calls in one place.
 */
public class DriveHardware implements DriveIO {
	public DcMotor frontLeft;
	public DcMotor frontRight;
	public DcMotor backLeft;
	public DcMotor backRight;
	private IMU imu;

	public void init(HardwareMap hardwareMap) {
		frontLeft = hardwareMap.dcMotor.get(Constants.Drive.FRONT_LEFT);
		backLeft = hardwareMap.dcMotor.get(Constants.Drive.BACK_LEFT);
		frontRight = hardwareMap.dcMotor.get(Constants.Drive.FRONT_RIGHT);
		backRight = hardwareMap.dcMotor.get(Constants.Drive.BACK_RIGHT);

		// Set directions from configuration (defaults match Mark 2)
		frontLeft.setDirection(Constants.Drive.FRONT_LEFT_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
		backLeft.setDirection(Constants.Drive.BACK_LEFT_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
		frontRight.setDirection(Constants.Drive.FRONT_RIGHT_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
		backRight.setDirection(Constants.Drive.BACK_RIGHT_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

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

		// IMU is optional for heading-aware drive. Keep runtime robust if missing.
		if (Constants.Drive.USE_IMU_FOR_HEADING) {
			try {
				imu = hardwareMap.get(IMU.class, Constants.Drive.IMU_NAME);
			} catch (RuntimeException ignored) {
				imu = null;
			}
		}
	}

	/**
	 * Set motor powers in the order: frontLeft, frontRight, backLeft, backRight.
	 * Values should be in [-1, 1].
	 */
	@Override
	public void setMotorPowers(double fl, double fr, double bl, double br) {
		frontLeft.setPower(fl);
		frontRight.setPower(fr);
		backLeft.setPower(bl);
		backRight.setPower(br);
	}

	@Override
	public void stop() {
		setMotorPowers(0,0,0,0);
	}

	/**
	 * Return heading in radians if IMU is available; otherwise returns 0.
	 */
	public double getHeadingRadians() {
		if (imu == null) return 0.0;
		return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
	}
}