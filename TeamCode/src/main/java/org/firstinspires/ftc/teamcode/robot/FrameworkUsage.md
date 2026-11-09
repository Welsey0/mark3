# Framework usage

## Subsystems and supersystems

Use a subsystem for one mechanism or hardware-backed capability. It owns
control behavior for that mechanism and receives its hardware wrapper through
the constructor.

Use a supersystem for a robot-mode coordinator that calls multiple subsystems.
For example, `AutonomousSupersystem` provides autonomous shortcuts such as
stopping the drivetrain and shooter together. It intentionally does not own
hardware and does not contain the autonomous step variable.

The autonomous opmode owns the step state:

```java
switch (step) {
    case 0:
        autonomous.drive().followPath(path, profile, heading);
        step = 1;
        break;
    case 1:
        autonomous.shooter().setPower(1.0, 1.0);
        break;
    default:
        autonomous.drive().fail("Unknown autonomous step: " + step);
}
```

The opmode can assign any step at any time. It should stop or transition
explicitly when an operation completes, times out, or reports an error.

## Controller input bindings

Controller layout is an opmode concern, not a robot constant. Declare bindings
as fields in the opmode and pass their sampled values into subsystem methods.
Use typed factories so a trigger-only function cannot accidentally be assigned
a stick or button:

```java
private final GamepadInput.AnalogBinding driveForward =
        GamepadInput.axis(GamepadInput.StickAxis.LEFT_Y);
private final GamepadInput.AnalogBinding shooterPower =
        GamepadInput.trigger(GamepadInput.Trigger.RIGHT);
private final GamepadInput.ActionBinding slowMode =
        GamepadInput.hold(GamepadInput.Button.LEFT_BUMPER);
```

Actions support both modes:

```java
GamepadInput.ActionBinding held =
        GamepadInput.hold(GamepadInput.Button.LEFT_BUMPER);
GamepadInput.ActionBinding latched =
        GamepadInput.toggle(GamepadInput.Button.A);
GamepadInput.ActionBinding triggerAction =
        GamepadInput.hold(GamepadInput.Trigger.RIGHT, 0.7);
```

Call `active(gamepad)` once per loop. `HOLD` is true only while the control is
active. `TOGGLE` changes state on the press edge and remains true until the
next press. Trigger actions use a configurable threshold from `0.0` to `1.0`.
Call `reset()` when restarting a mode or routine.

Subsystem APIs should receive values or action state, not `Gamepad` or mapping
enums. This keeps them usable from TeleOp, autonomous tests, and other
supersystems:

```java
drive.driveWithHeading(strafe, forward, turn, heading);
// Or let DriveSubsystem apply its configured slow-mode scale:
drive.driveWithHeading(strafe, forward, turn, heading, slowModeActive);
shooter.setPower(topPower, bottomPower);
intake.setEnabled(intakeEnabled);
```

Keep the binding declaration near the opmode code that uses it. If two opmodes
need the same layout, extract a small configuration helper rather than moving
the mapping into `Constants`.

## Configuration boundary

Hardware names, inversion, physical geometry, motor behavior, controller
tuning values, and mechanism configuration belong in `Constants`. Controller
mapping, subsystem selection, and routine behavior belong in the opmode. This
keeps the same subsystem classes reusable across supported robot configurations.

## Adding a subsystem

1. Add a hardware wrapper if the mechanism needs FTC devices. Keep device names,
   directions, and zero-power behavior in `Constants`.
2. Make the wrapper fail with `RobotInitializationException` when required
   devices are missing.
3. Add a subsystem that receives the wrapper in its constructor. Reject null
   dependencies immediately.
4. Add a `RobotContext.Builder.withX()` method that initializes the wrapper once
   and adds the subsystem to the lifecycle list.
5. Add telemetry through `TelemetrySource` if useful. Never call
   `telemetry.update()` inside the subsystem.
6. Select the subsystem explicitly in each opmode that needs it.

Do not look up the same physical device in two wrappers. Share the subsystem or
pass the existing subsystem into a supersystem.

## Adding a supersystem

A supersystem coordinates multiple selected subsystems and owns no hardware.
Use it for reusable mode-level helpers, such as stopping drive and shooter
together. Keep the autonomous step variable and step switch in the autonomous
opmode. Register a custom supersystem with `withSupersystem(...)`.
