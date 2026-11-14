package org.firstinspires.ftc.teamcode.subsystems;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.hardware.movement.ShooterHardware;

public class ShooterSubsystem implements Subsystem {
    private final ShooterHardware hardware;

    public ShooterSubsystem(ShooterHardware hardware) {
        if (hardware == null) {
            throw new RobotInitializationException("ShooterSubsystem", "ShooterHardware is null");
        }
        this.hardware = hardware;
    }
    public void setPower(double top, double bottom) {
        if (hardware != null) {
            hardware.setPowers(top, bottom);
        }
    }

    @Override
    public void addTelemetry(Telemetry telemetry) {

    }

    public void stop() {
        if (hardware != null) hardware.stop();
    }
}
