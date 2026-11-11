package org.firstinspires.ftc.teamcode.subsystems;

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

    public void setPower(TurretHardware.Side side, double power) {
        hardware.setPower(side, power);
    }

    public void setPowers(double left, double right) {
        hardware.setPowers(left, right);
    }

    @Override
    public void stop() {
        hardware.stop();
    }
}
