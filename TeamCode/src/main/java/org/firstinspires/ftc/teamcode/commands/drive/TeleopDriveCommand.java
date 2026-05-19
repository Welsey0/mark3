package org.firstinspires.ftc.teamcode.commands.drive;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.framework.Command;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.robot.Constants;

import java.util.Collections;
import java.util.Set;
import java.util.function.DoubleSupplier;

/**
 * TeleOp drive command that reads a Gamepad and drives robot.
 * <p>
 * This command is intentionally simple and easy to read. It assumes robot-centric
 * control using left stick for translation and right stick X for rotation.
 * <p>
 * Driver input shaping for TeleOp lives in this class.
 */
public class TeleopDriveCommand implements Command {
    private final DriveSubsystem drive;
    private final Gamepad gamepad;
    private final DoubleSupplier headingRadiansSupplier;

    public TeleopDriveCommand(DriveSubsystem drive, Gamepad gamepad) {
        this(drive, gamepad, () -> 0.0);
    }

    public TeleopDriveCommand(DriveSubsystem drive, Gamepad gamepad, DoubleSupplier headingRadiansSupplier) {
        this.drive = drive;
        this.gamepad = gamepad;
        this.headingRadiansSupplier = headingRadiansSupplier;
    }

    @Override
    public void execute() {
        // Read joysticks using configurable signs so behavior is tunable in Constants.
        double dx = gamepad.left_stick_x * Constants.Drive.STRAFE_INPUT_SIGN;
        double dy = gamepad.left_stick_y * Constants.Drive.FORWARD_INPUT_SIGN;
        double rx = gamepad.right_stick_x * Constants.Drive.TURN_INPUT_SIGN;

        // Apply deadband scaling from Constants
        dx = applyDeadband(dx);
        dy = applyDeadband(dy);
        rx = applyDeadband(rx);

        // Apply global teleop scales
        double scale = gamepad.right_bumper ? Constants.Drive.SLOW_MODE_SCALE : 1.0;
        dx *= Constants.Drive.TELEOP_POWER_SCALE * scale;
        dy *= Constants.Drive.TELEOP_POWER_SCALE * scale;
        rx *= Constants.Drive.ROTATION_POWER_SCALE * scale;

        double heading = headingRadiansSupplier.getAsDouble();
        drive.driveWithHeading(dx, dy, rx, heading);
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Collections.singleton(drive);
    }

    private static double applyDeadband(double value) {
        double deadband = Constants.Drive.JOYSTICK_DEADZONE;
        if (Math.abs(value) <= deadband) return 0.0;
        return value > 0 ? (value - deadband) / (1.0 - deadband) : (value + deadband) / (1.0 - deadband);
    }
}

