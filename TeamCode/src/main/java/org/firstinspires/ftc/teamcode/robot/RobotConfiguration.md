# Robot configuration

Each opmode selects its exact subsystem graph through `RobotContext.builder`.
There are no automatic teleop or autonomous presets:

```java
robot = RobotContext.builder(hardwareMap, telemetry)
        .withDrive()
        .withPosition(true, false) // IMU only
        .withShooter()
        .build();
```

The opmode owns its input loop and calls the selected subsystems directly:

```java
robot.drive().driveWithHeading(dx, dy, rx, robot.position().getHeading());
robot.shooter().setPower(topPower, bottomPower);
robot.periodic();
```

Only selected hardware is looked up. Builder methods validate dependencies.
A missing configured device throws `RobotInitializationException` with the
subsystem name and expected device information during `init`.

For autonomous, select only what the routine uses:

```java
robot = RobotContext.builder(hardwareMap, telemetry)
        .withDrive()
        .withPosition(false, true) // Pinpoint only
        .withAutonomous()
        .withAutonomousSupersystem()
        .build();
```

`AutonomousSubsystem` contains only path-following and drive control. The
opmode owns the step variable and chooses when to call the drive subsystem.
`AutonomousSupersystem` is an optional coordinator for shortcuts that use
multiple selected subsystems; it does not own hardware or autonomous steps.
Custom supersystems can be added with `withSupersystem(...)`.

Vision is configured explicitly because each camera may use a different
processor. Construct `CameraHardware` with webcam configurations and/or
Limelight pipeline IDs, then register it with `.withCameras(...)`. Webcam
ball detection can use `HiveBallDetectorPipeline`; a custom goal processor
must implement both `VisionProcessor` and `DetectionProvider`.

Turrets are configured as a left/right pair:

```java
robot = RobotContext.builder(hardwareMap, telemetry)
        .withTurret()
        .build();
robot.turret().setPowers(leftCommand, rightCommand);
```

Use one hardware wrapper per physical device. If two subsystems need drive
output, pass the same `DriveSubsystem` to both; do not look up the motors again.

The shooter configuration is in `Constants.Shooter.CONFIGURATION`:

- `SINGLE_WHEEL`: only `TOP` is required; bottom power is ignored.
- `DOUBLE_WHEEL`: `TOP` and `BOTTOM` are required and can receive separate power.
- `TWIN_SINGLE_WHEEL`: two independently powered shooter motors; use the two
  configured names and power arguments.

Mechanism-specific names, inversion, and zero-power behavior belong in
`Constants`. Controller bindings and routine-specific choices belong in the
opmode. See `FrameworkUsage.md` for the typed `GamepadInput` binding API.
