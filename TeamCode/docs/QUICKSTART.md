# Quickstart — Run the example TeleOp

This quick guide gets you from code to running the `Mecanum TeleOp (Mark3)` example.

Prereqs
- Robot Controller and Driver Station apps installed
- Robot configuration with four motors named exactly:
  - `frontLeft` `backLeft` `frontRight` `backRight`

Steps
1. Build and deploy the Robot Controller APK from Android Studio (or use OnBotJava).
2. Upload the app to your Robot Controller device and open the Driver Station.
3. On the Driver Station, choose the OpMode `Mecanum TeleOp (Mark3)`.
4. Press INIT and then PLAY. Use the left stick to translate (forward/back, strafe) and the right stick X to rotate.

Notes
- This example uses robot-centric control; it does not perform field-centric transformations.
- Hardware initialization is performed in `MecanumTeleOp.init()` using `DriveHardware`.
- The command scheduler runs the `TeleopDriveCommand` as the default command for the drivetrain.

If something doesn't work
- Verify motor names in the RC configuration match the expected names.
- Check motor directions and invert if motors spin the wrong way.
- If you see no motion, attach logging/telemetry in `TeleopDriveCommand.execute()` to confirm joystick values.

