package org.firstinspires.ftc.teamcode.hardware.movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.framework.Hardware;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.robot.Constants;

public class TurretHardware implements Hardware {
    private DcMotor motor;
    private Servo servo;
    private final Constants.Turret.Configuration configuration;
    private final String actuatorName;

    public TurretHardware() {
        this(Constants.Turret.CONFIGURATION, Constants.Turret.ROTATIONACTUATOR);
    }

    public TurretHardware(
            Constants.Turret.Configuration configuration,
            String actuatorName) {
        if (configuration == null || actuatorName == null || actuatorName.isEmpty()) {
            throw new IllegalArgumentException(
                    "turret configuration and actuator name are required");
        }

        this.configuration = configuration;
        this.actuatorName = actuatorName;
    }

    @Override
    public void init(HardwareMap hardwareMap) {
        try {
            if (configuration == Constants.Turret.Configuration.MOTOR) {
                motor = hardwareMap.dcMotor.get(actuatorName);
                motor.setDirection(
                        Constants.Turret.ROTATIONACTUATOR_REVERSED
                                ? DcMotor.Direction.REVERSE
                                : DcMotor.Direction.FORWARD
                );
                motor.setZeroPowerBehavior(
                        Constants.Turret.ZERO_POWER_BRAKE
                                ? DcMotor.ZeroPowerBehavior.BRAKE
                                : DcMotor.ZeroPowerBehavior.FLOAT
                );
            } else {
                servo = hardwareMap.servo.get(actuatorName);
            }
        } catch (RuntimeException exception) {
        throw new RobotInitializationException(
                "TurretHardware",
                "check the configured turret actuator name and type",
                exception);
        }
    }

    /**
     * Set the power of the turret.
     * @param power the power to set, in the range [-1, 1]
     */
    public void setPower(double power) {
        if (configuration == Constants.Turret.Configuration.MOTOR) {
            motor.setPower(power);
        } else {
            servo.setPosition(power);
        }
    }

    @Override
    public void stop() {
        setPower(0);
    }
}
