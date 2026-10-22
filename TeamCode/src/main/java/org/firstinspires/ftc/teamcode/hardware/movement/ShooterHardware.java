package org.firstinspires.ftc.teamcode.hardware.movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.framework.Hardware;
import org.firstinspires.ftc.teamcode.robot.Constants;

public class ShooterHardware implements Hardware {
    public DcMotor shooterTop;
    public DcMotor shooterBottom;

    @Override
    public void init(HardwareMap hardwareMap) {
        shooterTop = hardwareMap.dcMotor.get(Constants.Shooter.TOP);
        shooterBottom = hardwareMap.dcMotor.get(Constants.Shooter.BOTTOM);

        // Set directions from configuration
        shooterTop.setDirection(Constants.Shooter.TOP_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        shooterBottom.setDirection(Constants.Shooter.BOTTOM_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

        // Default motor behavior (configurable)
        if (org.firstinspires.ftc.teamcode.robot.Constants.Shooter.ZERO_POWER_BRAKE) {
            shooterTop.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            shooterBottom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            shooterTop.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            shooterBottom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    /**
     * Set motor powers in the order: frontLeft, frontRight, backLeft, backRight.
     * Values should be in [-1, 1].
     */
    public void setMotorPowers(double top, double bottom) {
        shooterTop.setPower(top);
        shooterBottom.setPower(bottom);
    }

    @Override
    public void stop() {
        setMotorPowers(0,0);
    }
}
