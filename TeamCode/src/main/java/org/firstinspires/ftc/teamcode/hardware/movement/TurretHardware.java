package org.firstinspires.ftc.teamcode.hardware.movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.framework.Hardware;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.robot.Constants;

public class TurretHardware implements Hardware {
    public enum Side { LEFT, RIGHT }

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private Servo leftServo;
    private Servo rightServo;
    private final Constants.Turret.Configuration configuration;
    private final String leftName;
    private final String rightName;

    public TurretHardware() {
        this(Constants.Turret.CONFIGURATION,
                Constants.Turret.LEFT_ROTATION_ACTUATOR,
                Constants.Turret.RIGHT_ROTATION_ACTUATOR);
    }

    public TurretHardware(
            Constants.Turret.Configuration configuration,
            String leftName,
            String rightName) {
        if (configuration == null
                || leftName == null || leftName.isEmpty()
                || rightName == null || rightName.isEmpty()) {
            throw new IllegalArgumentException(
                    "turret configuration and both actuator names are required");
        }

        this.configuration = configuration;
        this.leftName = leftName;
        this.rightName = rightName;
    }

    @Override
    public void init(HardwareMap hardwareMap) {
        try {
            if (configuration == Constants.Turret.Configuration.MOTOR) {
                leftMotor = hardwareMap.dcMotor.get(leftName);
                rightMotor = hardwareMap.dcMotor.get(rightName);
                leftMotor.setDirection(
                        Constants.Turret.LEFT_ROTATION_ACTUATOR_REVERSED
                                ? DcMotor.Direction.REVERSE
                                : DcMotor.Direction.FORWARD
                );
                rightMotor.setDirection(
                        Constants.Turret.RIGHT_ROTATION_ACTUATOR_REVERSED
                                ? DcMotor.Direction.REVERSE
                                : DcMotor.Direction.FORWARD
                );
                DcMotor.ZeroPowerBehavior zeroPowerBehavior =
                        Constants.Turret.ZERO_POWER_BRAKE
                                ? DcMotor.ZeroPowerBehavior.BRAKE
                                : DcMotor.ZeroPowerBehavior.FLOAT;
                leftMotor.setZeroPowerBehavior(zeroPowerBehavior);
                rightMotor.setZeroPowerBehavior(zeroPowerBehavior);
            } else {
                leftServo = hardwareMap.servo.get(leftName);
                rightServo = hardwareMap.servo.get(rightName);
            }
        } catch (RuntimeException exception) {
            throw new RobotInitializationException(
                    "TurretHardware",
                    "check both configured turret actuator names and type",
                    exception);
        }
    }

    public void setPower(Side side, double power) {
        if (configuration == Constants.Turret.Configuration.MOTOR) {
            motor(side).setPower(power * Constants.Turret.POWER_SCALE);
        } else {
            servo(side).setPosition(power);
        }
    }

    public void setPowers(double left, double right) {
        setPower(Side.LEFT, left);
        setPower(Side.RIGHT, right);
    }

    private DcMotor motor(Side side) {
        return side == Side.LEFT ? leftMotor : rightMotor;
    }

    private Servo servo(Side side) {
        return side == Side.LEFT ? leftServo : rightServo;
    }

    @Override
    public void stop() {
        if (configuration == Constants.Turret.Configuration.MOTOR) {
            setPowers(0.0, 0.0);
        } else {
            setPowers(
                    Constants.Turret.SERVO_STOP_POSITION,
                    Constants.Turret.SERVO_STOP_POSITION);
        }
    }
}
