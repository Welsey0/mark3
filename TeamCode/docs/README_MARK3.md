# Mark 3 WPILib-lite framework (TeamCode)

This folder contains a minimal, well-documented WPILib-inspired framework for FTC ("Mark 3"). The goal:

- Provide a small, testable command/subsystem architecture that is easy to understand
- Keep FTC SDK usage isolated to a small set of hardware wrapper classes
- Use clear naming and comments so team members can extend and maintain the system

Included components (minimal starter):

- framework/command: Command, Subsystem, CommandScheduler
- framework/opmode: BaseOpMode, BaseLinearOpMode
- framework/util: small math helpers
- hardware/drive: `DriveHardware` — wraps motors using Mark 2 motor names
- subsystems/DriveSubsystem: mecanum mixing and forwarding to `DriveHardware`
- commands/drive/TeleopDriveCommand: reads `Gamepad` and drives the robot
- opmode/MecanumTeleOp: example TeleOp that initializes the hardware and runs the scheduler

Configuration and tuning

All tunable values and device names are stored in `robot/Constants.java` so you can
adjust deadbands, joystick scales, and device identifiers without changing behavior code.
Notable entries:
- `Constants.Drive.FRONT_LEFT`, `BACK_LEFT`, `FRONT_RIGHT`, `BACK_RIGHT` - motor names
- `Constants.Drive.JOYSTICK_DEADBAND` - joystick deadband used by drive
- `Constants.Drive.TELEOP_POWER_SCALE` - global teleop power scale
- `Constants.Drive.ROTATION_POWER_SCALE` - turn scale
- `Constants.Drive.ZERO_POWER_BRAKE` - whether motors use BRAKE or FLOAT at zero power
- `Constants.Drive.FIELD_CENTRIC_ENABLED` - toggles heading-aware Mark 2 style transform

How to use this example:

1. Open Android Studio and import the project (or use OnBotJava if you prefer).
2. Make sure the robot configuration contains motors named `frontLeft`, `backLeft`, `frontRight`, `backRight`.
3. Deploy the Robot Controller app and run the `Mecanum TeleOp (Mark3)` OpMode on the Driver Station.
4. Use the left stick to translate and right stick X to rotate.

This is a starting point — expand subsystem behavior, add sensors, and build commands incrementally.


---

For more technical design notes, see `ARCHITECTURE.md` in this folder.

For day-to-day edits, start with `HOW_TO_CHANGE_THINGS.md`.

