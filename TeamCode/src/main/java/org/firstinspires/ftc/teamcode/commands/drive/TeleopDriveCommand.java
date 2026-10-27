package org.firstinspires.ftc.teamcode.commands.drive;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.framework.Command;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.subsystems.PositionSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import java.util.Collections;
import java.util.Set;
import java.util.function.DoubleSupplier;

/**
 * TeleOp drive command that reads a Gamepad and drives robot based on driver input.
 */

public class TeleopDriveCommand implements Command {
    private final DriveSubsystem drive;
    private final Gamepad gamepad;
    private final PositionSubsystem position;
    private final ShooterSubsystem shooter;

    public TeleopDriveCommand(DriveSubsystem drive, Gamepad gamepad, PositionSubsystem position, ShooterSubsystem shooter) {
        this.drive = drive;
        this.gamepad = gamepad;
        this.position = position;
        this.shooter = shooter;
    }

    @Override
    public void execute() {
        // Read joysticks using configurable signs so behavior is tunable in Constants.
        double dx = gamepad.right_stick_x * Constants.Drive.STRAFE_INPUT_SIGN;
        double dy = gamepad.left_stick_y * Constants.Drive.FORWARD_INPUT_SIGN;
        double rx = gamepad.left_stick_x * Constants.Drive.TURN_INPUT_SIGN;
        double st = gamepad.right_trigger * Constants.Shooter.INPUT_SIGN;
        double sb = gamepad.left_trigger * Constants.Shooter.INPUT_SIGN;

        // deadband scaling
        dx = applyDeadband(dx);
        dy = applyDeadband(dy);
        rx = applyDeadband(rx);

        // teleop power scaling
        double scale = gamepad.left_bumper ? Constants.Drive.SLOW_MODE_SCALE : 1.0;
        dx *= Constants.Drive.TELEOP_POWER_SCALE * scale;
        dy *= Constants.Drive.TELEOP_POWER_SCALE * scale;
        rx *= Constants.Drive.ROTATION_POWER_SCALE * scale;
        st *= Constants.Shooter.POWER_SCALE * scale;
        sb *= Constants.Shooter.POWER_SCALE * scale;


        double heading = position.getHeading();
        drive.driveWithHeading(dx, dy, rx, heading);
        shooter.setPower(st, sb);
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

