package org.firstinspires.ftc.teamcode.commands.drive;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.framework.command.Command;
import org.firstinspires.ftc.teamcode.framework.command.Subsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.robot.Constants;

import java.util.Collections;
import java.util.Set;

/**
 * TeleOp drive command that reads a Gamepad and drives a mecanum drivetrain.
 *
 * This command is intentionally simple and easy to read. It assumes robot-centric
 * control using left stick for translation and right stick X for rotation.
 */
public class TeleopDriveCommand implements Command {
    private final DriveSubsystem drive;
    private final Gamepad gamepad;

    public TeleopDriveCommand(DriveSubsystem drive, Gamepad gamepad) {
        this.drive = drive;
        this.gamepad = gamepad;
    }

    @Override
    public void execute() {
        // Read joysticks: FTC gamepad is +down for y, so negate to make forward +
        double vx = -gamepad.left_stick_y;    // forward
        double vy = gamepad.left_stick_x;     // strafe left
        double omega = -gamepad.right_stick_x; // rotation

        // Apply deadband scaling from Constants
        vx = org.firstinspires.ftc.teamcode.framework.util.MathUtil.applyDeadband(vx, Constants.Drive.JOYSTICK_DEADBAND);
        vy = org.firstinspires.ftc.teamcode.framework.util.MathUtil.applyDeadband(vy, Constants.Drive.JOYSTICK_DEADBAND);
        omega = org.firstinspires.ftc.teamcode.framework.util.MathUtil.applyDeadband(omega, Constants.Drive.JOYSTICK_DEADBAND);

        // Apply global teleop scale
        double scale = Constants.Drive.TELEOP_POWER_SCALE;
        drive.setDrivePower(vx * scale, vy * scale, omega * scale);
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Collections.singleton(drive);
    }
}

