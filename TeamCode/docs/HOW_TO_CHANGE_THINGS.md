# How To Change Things (Mark 3 Lite)

This guide is intentionally short. If you are a new programmer, start here.

## 1) What to edit first

Most day-to-day changes happen in only three places:

1. `robot/Constants.java`
   - hardware names
   - deadbands
   - speed scales
   - field-centric on/off
2. `commands/drive/TeleopDriveCommand.java`
   - joystick mapping and driver feel
3. `opmode/MecanumTeleOp.java`
   - which robot modes are run, telemetry text

If you can solve your problem in one of these files, do not touch the framework.

## 2) Mental model (simple)

`OpMode -> Command -> Subsystem -> Hardware`

- OpMode composes the robot and runs scheduler loops.
- Command reads input and asks subsystem to do work.
- Subsystem does mechanism math/logic.
- Hardware talks to FTC SDK devices.

## 3) Common tasks

### Change drive speed
- Edit `Constants.Drive.TELEOP_POWER_SCALE`.
- If turning is too fast/slow, edit `Constants.Drive.ROTATION_POWER_SCALE`.

### Swap motor names
- Edit `Constants.Drive.FRONT_LEFT`, `BACK_LEFT`, `FRONT_RIGHT`, `BACK_RIGHT`.

### Reverse a motor
- Edit one of:
  - `Constants.Drive.FRONT_LEFT_REVERSED`
  - `Constants.Drive.BACK_LEFT_REVERSED`
  - `Constants.Drive.FRONT_RIGHT_REVERSED`
  - `Constants.Drive.BACK_RIGHT_REVERSED`

### Switch robot-centric / field-centric
- Set `Constants.Drive.FIELD_CENTRIC_ENABLED`.
- If field-centric is on, enable heading source with `Constants.Drive.USE_IMU_FOR_HEADING`.

## 4) What not to change unless needed

- `framework/command/CommandScheduler.java`
- `framework/opmode/BaseOpMode.java`

These are infrastructure files. Keep them stable.

## 5) Add a new mechanism safely

1. Add hardware wrapper in `hardware/<mechanism>/...`
2. Add subsystem in `subsystems/...`
3. Add command(s) in `commands/<mechanism>/...`
4. Wire in `RobotContainer.configureTeleop(...)`

Keep each step small and test after each step.

## 6) One-team rule

If a change is hard to explain in 2-3 sentences, split it into smaller commits.
That keeps the codebase understandable for everyone.

