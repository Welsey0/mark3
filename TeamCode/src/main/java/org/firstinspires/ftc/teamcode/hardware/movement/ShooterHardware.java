package org.firstinspires.ftc.teamcode.hardware.movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.framework.Hardware;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.robot.Constants;

public class ShooterHardware implements Hardware {
    private DcMotor shooterTop;
    private DcMotor shooterBottom;
    private final Constants.Shooter.Configuration configuration;
    private final String topName;
    private final String bottomName;

    public ShooterHardware() {
        this(Constants.Shooter.CONFIGURATION, Constants.Shooter.TOP, Constants.Shooter.BOTTOM);
    }

    public ShooterHardware(Constants.Shooter.Configuration configuration,
                           String topName, String bottomName) {
        if (configuration == null || topName == null || topName.isEmpty()) {
            throw new IllegalArgumentException("shooter configuration and primary motor are required");
        }
        this.configuration = configuration;
        this.topName = topName;
        this.bottomName = bottomName;
    }

    @Override
    public void init(HardwareMap hardwareMap) {
        try {
            shooterTop = hardwareMap.dcMotor.get(topName);
            if (configuration != Constants.Shooter.Configuration.SINGLE_WHEEL) {
                if (bottomName == null || bottomName.isEmpty()) {
                    throw new IllegalArgumentException("secondary shooter motor name is required");
                }
                shooterBottom = hardwareMap.dcMotor.get(bottomName);
            }
        } catch (RuntimeException exception) {
            throw new RobotInitializationException(
                    "ShooterHardware",
                    "check the configured shooter motor names and configuration",
                    exception);
        }

        // Set directions from configuration
        shooterTop.setDirection(Constants.Shooter.TOP_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        if (shooterBottom != null) {
            shooterBottom.setDirection(Constants.Shooter.BOTTOM_REVERSED
                    ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        }

        // Default motor behavior (configurable)
        if (org.firstinspires.ftc.teamcode.robot.Constants.Shooter.ZERO_POWER_BRAKE) {
            shooterTop.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            if (shooterBottom != null) shooterBottom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            shooterTop.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            if (shooterBottom != null) shooterBottom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    /**
     * Set motor powers in the order: frontLeft, frontRight, backLeft, backRight.
     * Values should be in [-1, 1].
     */
    public void setPowers(double top, double bottom) {
        shooterTop.setPower(top);
        if (shooterBottom != null) {
            shooterBottom.setPower(bottom);
        }
    }

    @Override
    public void stop() {
        setPowers(0,0);
    }
}
