# Telemetry

`TelemetrySubsystem` is the single telemetry coordinator for an opmode.
Subsystems that have useful status implement `TelemetrySource` and are
registered by the `RobotContext.Builder` when they are selected. Supersystems
should expose status through their owned subsystems or add a telemetry source
when the builder explicitly registers one; they should not create a second
telemetry update loop.

The context calls selected subsystems in `periodic()`, then calls
`TelemetrySubsystem.periodic()`. That method asks every registered source to
add data and calls `telemetry.update()` exactly once. Subsystems should not
call `update()` themselves.

This keeps telemetry modular: a drive-only opmode does not report shooter or
autonomous values. It also keeps output consistent when a subsystem is not
selected. Initialization failures are not hidden in telemetry; they throw
`RobotInitializationException` with an actionable message so the driver sees
the failed subsystem and expected hardware in the SDK error screen/log.

To add subsystem telemetry:

```java
public final class ExampleSubsystem implements TelemetrySource {
    @Override
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Example", state);
    }
}
```

Register it in the builder after constructing the subsystem. Keep telemetry
reads side-effect-free and avoid performing hardware configuration from
`addTelemetry`.
