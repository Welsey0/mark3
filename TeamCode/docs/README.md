# Mark 3 Minimal Docs

This documentation set describes the minimized, WPILib-inspired FTC robot codebase currently in `TeamCode`.

## What remains in the active codebase

The minimized active path is:

`MecanumTeleOp` -> `DriveHardware` -> `DriveSubsystem` -> `TeleopDriveCommand` -> `CommandScheduler`

Supporting config lives in `Constants`.

## Files

- `SYSTEM_OVERVIEW.md` - how the architecture fits together
- `BUILDING_AND_RUNNING.md` - how to build, deploy, and run it
- `EXTENDING_THE_SYSTEM.md` - how to add new mechanisms or features
- `FILE_MAP.md` - current files and what each one owns

## Design goals

- Keep HAL at the boundary
- Keep commands and subsystems separate
- Keep the active path small enough to understand quickly
- Preserve useful WPILib-style ideas without reimplementing WPILib

## Who this is for

- drivers and testers who need to know what the robot is doing
- programmers who want to change behavior safely
- future maintainers who need a readable map of the codebase

If you are changing code for the first time, start with `FILE_MAP.md` and `EXTENDING_THE_SYSTEM.md`.

