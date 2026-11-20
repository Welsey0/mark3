package org.firstinspires.ftc.teamcode.hardware.movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.framework.Hardware;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.robot.Constants;

public class SingleMotorIntakeHardware implements Hardware {
    public DcMotor intake;

    @Override
    public void init(HardwareMap hardwareMap) {
        try {
            intake = hardwareMap.dcMotor.get(Constants.Intake.MOTOR);
        } catch (RuntimeException exception) {
            throw new RobotInitializationException(
                    "SingleMotorIntakeHardware",
                    "did not find expected motors",
                    exception);
        }

        // Set directions from configuration
        intake.setDirection(Constants.Intake.MOTOR_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

        // Default motor behavior (configurable)
        if (org.firstinspires.ftc.teamcode.robot.Constants.Intake.ZERO_POWER_BRAKE) {
            intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public void setMotorPower(double one) {
        intake.setPower(one);
    }

    @Override
    public void stop() {
        setMotorPower(0);
    }
}