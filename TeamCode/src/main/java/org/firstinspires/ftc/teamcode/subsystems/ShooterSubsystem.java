package org.firstinspires.ftc.teamcode.subsystems;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.hardware.movement.ShooterHardware;

public class ShooterSubsystem implements Subsystem {
    private final ShooterHardware hardware;

    public ShooterSubsystem(ShooterHardware hardware) {
        this.hardware = hardware;
    }
    public void setPower(double top, double bottom) {
        if (hardware != null) {
            hardware.setMotorPowers(top, bottom);
        }
    }
    public void stop() {
        if (hardware != null) hardware.stop();
    }
}
