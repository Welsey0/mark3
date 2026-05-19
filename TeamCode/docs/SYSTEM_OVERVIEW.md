# System Overview
Active flow: `MecanumTeleOp` -> `DriveHardware` -> `DriveSubsystem` -> `TeleopDriveCommand` -> `CommandScheduler`.
- `Constants` holds tunables.
- `DriveHardware` owns FTC devices.
- `DriveSubsystem` does mecanum math and heading handling.
- `TeleopDriveCommand` reads `gamepad1`, applies deadband/slow mode, and asks the subsystem to drive.
- `CommandScheduler` runs the command each loop.
