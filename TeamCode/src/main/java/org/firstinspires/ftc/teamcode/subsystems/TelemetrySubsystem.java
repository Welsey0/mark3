package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.framework.TelemetrySource;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;

import java.util.ArrayList;
import java.util.List;

public final class TelemetrySubsystem implements Subsystem {
    private final Telemetry telemetry;
    private final List<TelemetrySource> sources = new ArrayList<>();

    public TelemetrySubsystem(Telemetry telemetry) {
        if (telemetry == null) {
            throw new RobotInitializationException("TelemetrySubsystem", "Telemetry is null");
        }
        this.telemetry = telemetry;
    }

    public void addSource(TelemetrySource source) {
        if (source == null) {
            throw new RobotInitializationException("TelemetrySubsystem", "telemetry source is null");
        }
        sources.add(source);
    }

    @Override
    public void periodic() {
        for (TelemetrySource source : sources) {
            source.addTelemetry(telemetry);
        }
        telemetry.update();
    }

    @Override
    public void addTelemetry(Telemetry telemetry) {

    }

    public void status(String status) {
        telemetry.addData("Status", status);
    }
}
