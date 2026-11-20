package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.hardware.movement.SingleMotorIntakeHardware;
import org.firstinspires.ftc.teamcode.robot.Constants;

public class IntakeSubsystem implements Subsystem {
    public final SingleMotorIntakeHardware smIntakeHardware;
    public IntakeSubsystem(SingleMotorIntakeHardware hardware) {
        if (hardware == null) {
            throw new RobotInitializationException("DriveSubsystem", "DriveHardware is null");
        }
        this.smIntakeHardware = hardware;
    }
    @Override
    public void addTelemetry(Telemetry telemetry) {
    }
    public boolean intakeOn = false;
    public void toggleIntake() {
        intakeOn = !intakeOn;
        if (intakeOn) {
            smIntakeHardware.setMotorPower(Constants.Intake.INTAKE_MOTOR_POWER);
        } else {
            smIntakeHardware.setMotorPower(0);
        }
    }

    @Override
    public void stop() {
        smIntakeHardware.setMotorPower(0);
    }

}
