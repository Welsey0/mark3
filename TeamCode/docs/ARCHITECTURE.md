# Mark 3 Architecture (WPILib-lite) — Overview and Rationale

This document explains the small WPILib-inspired architecture implemented in TeamCode and how to think about extending it.

Core principles

- Separation of concerns: hardware access, subsystem behavior, and command logic are kept in separate layers.
- Simplicity: features are intentionally minimal so team members can understand and modify them quickly.
- Compatibility: motor names and basic behavior are compatible with the project's Mark 2 code to make migration easier.

Top-level layout

- robot/
  - `Constants.java` — centralized configuration (device names, tunings) — no hardware here.
  - `RobotContainer.java` — (starter) wiring for subsystems and commands.
  - `RobotState.java` — optional shared state (small and focused).

- framework/
  - `command/` — `Command`, `Subsystem`, `CommandScheduler` (core runtime)
  - `util/` — small math helpers (deadband, clamp)
  - `opmode/` — (not yet fully implemented) helpers that bridge FTC `OpMode` lifecycle to the framework

- hardware/
  - hardware wrappers that are *the only* classes that talk to `hardwareMap` and FTC SDK devices.
  - e.g. `hardware/drive/DriveHardware.java` — initializes motors and exposes `setMotorPowers(...)`.

- subsystems/
  - Own mechanism logic and state; call hardware wrappers to actuate motors.
  - `DriveSubsystem` exposes `setDrivePower(vx, vy, omega)` and does the mecanum mixing.

- commands/
  - Small action objects that use subsystems and implement lifecycle methods.
  - `TeleopDriveCommand` reads a `Gamepad` and calls `DriveSubsystem.setDrivePower(...)`.

Design notes and best practices

- Keep hardwareMap usage inside `hardware/` only. Subsystems should accept hardware wrapper objects (or interfaces) in their constructors.
- Commands should be stateless procedural objects that use subsystem APIs and declare their subsystem requirements.
- The `CommandScheduler` is intentionally simple: it supports scheduling, cancellation, default commands, and periodic execution.
  - Do not add complex features (parallel groups, timeouts) until the team understands the basic flow.
- Use `Constants` for any value that may change between builds or robots.

Extending the framework

1. Add more hardware wrappers (arm, intake, sensors) under `hardware/`.
2. Implement corresponding subsystems that accept the hardware wrappers.
3. Implement commands that use subsystems; keep commands small and testable.
4. Add `RobotContainer` wiring and OpModes that build and schedule these commands.

Testing tips

- Start with a single subsystem + command and a small OpMode that runs the scheduler.
- Test on bench before putting the robot on the field: ensure motor directions and zero-power behavior are correct.
- Prefer small incremental changes.

Migration from Mark 2

- This implementation intentionally uses the same motor names as Mark 2 (`frontLeft`, `backLeft`, `frontRight`, `backRight`) so that existing robot configuration files continue to work.
- Use Mark 2 modules as reference for sensor names and behaviors, but keep Mark 3's subsystem and hardware wrappers small and well-documented.


If you'd like, I can:
- Add `BaseOpMode`/`BaseLinearOpMode` bridge classes that automatically set up the RobotContainer and run the scheduler.
- Implement a `DriveIO` interface so you can add simulation or test doubles.
- Wire in a simple odometry/localizer based on Mark 2's `Locator` module.

Tell me which extension you'd like next and I will implement it in small, reviewable steps.

