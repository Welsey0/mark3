package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.hardware.movement.TurretHardware;

public class TurretSubsystem implements Subsystem {
    private final TurretHardware hardware;

    public TurretSubsystem(TurretHardware hardware) {
        if (hardware == null) {
            throw new RobotInitializationException("TurretSubsystem", "TurretHardware is null");
        }
        this.hardware = hardware;
    }

    public void setPower(double power) {
        hardware.setPower(power);
    }

    @Override
    public void addTelemetry(Telemetry telemetry) {

    }

    @Override
    public void stop() {
        hardware.stop();
    }
}
