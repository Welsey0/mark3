package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.framework.input.GamepadInput;
import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotContext;

/**
 * Demonstration OpMode implementing basic mecanum drive.
 */
@TeleOp(name = "Mecanum TeleOp (Mark3)")
public class MecanumTeleOp extends OpMode {
    private RobotContext robot;
    private final GamepadInput.AnalogBinding strafe = GamepadInput.axis(
            GamepadInput.StickAxis.RIGHT_X);
    private final GamepadInput.AnalogBinding forward = GamepadInput.axis(
            GamepadInput.StickAxis.LEFT_Y);
    private final GamepadInput.AnalogBinding turn = GamepadInput.axis(
            GamepadInput.StickAxis.LEFT_X);
    private final GamepadInput.AnalogBinding shooterTop = GamepadInput.trigger(
            GamepadInput.Trigger.RIGHT);
    private final GamepadInput.AnalogBinding shooterBottom = GamepadInput.trigger(
            GamepadInput.Trigger.LEFT);
    private final GamepadInput.ActionBinding slowMode = GamepadInput.hold(
            GamepadInput.Button.LEFT_BUMPER);

    @Override
    public void init() {
        robot = RobotContext.builder(hardwareMap, telemetry)
                .withDrive()
                .withPosition(true, true)
                .withShooter()
                .build();
        robot.init();
    }

    @Override
    public void loop() {
        if (robot != null) {
            double dx = applyDeadband(strafe.value(gamepad1) * Constants.Drive.STRAFE_INPUT_SIGN);
            double dy = applyDeadband(forward.value(gamepad1) * Constants.Drive.FORWARD_INPUT_SIGN);
            double rx = applyDeadband(turn.value(gamepad1) * Constants.Drive.TURN_INPUT_SIGN);
            double top = shooterTop.value(gamepad1) * Constants.Shooter.INPUT_SIGN;
            double bottom = shooterBottom.value(gamepad1) * Constants.Shooter.INPUT_SIGN;
            boolean slowModeActive = slowMode.active(gamepad1);

            robot.drive().driveWithHeading(
                    dx * Constants.Drive.TELEOP_POWER_SCALE,
                    dy * Constants.Drive.TELEOP_POWER_SCALE,
                    rx * Constants.Drive.ROTATION_POWER_SCALE,
                    robot.position().getHeading(),
                    slowModeActive
            );
            robot.shooter().setPower(
                    top * Constants.Shooter.POWER_SCALE,
                    bottom * Constants.Shooter.POWER_SCALE);
            robot.periodic();
        }
    }

    @Override
    public void stop() {
        if (robot != null) {
            robot.stop();
        }
    }

    private static double applyDeadband(double value) {
        double deadband = Constants.Drive.JOYSTICK_DEADZONE;
        if (Math.abs(value) <= deadband) return 0.0;
        return value > 0
                ? (value - deadband) / (1.0 - deadband)
                : (value + deadband) / (1.0 - deadband);
    }
}