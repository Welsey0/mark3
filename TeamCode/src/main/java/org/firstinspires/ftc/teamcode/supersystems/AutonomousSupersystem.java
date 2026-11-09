package org.firstinspires.ftc.teamcode.supersystems;

import org.firstinspires.ftc.teamcode.framework.Supersystem;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.subsystems.AutonomousSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PositionSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

/**
 * Autonomous convenience layer. The opmode owns the step switch and decides
 * when to call these methods; this class only coordinates selected subsystems.
 */
public final class AutonomousSupersystem implements Supersystem {
    private final AutonomousSubsystem drive;
    private final DriveSubsystem drivetrain;
    private final PositionSubsystem position;
    private final ShooterSubsystem shooter;

    public AutonomousSupersystem(AutonomousSubsystem drive,
                                 DriveSubsystem drivetrain,
                                 PositionSubsystem position,
                                 ShooterSubsystem shooter) {
        if (drive == null || drivetrain == null || position == null) {
            throw new RobotInitializationException(
                    "AutonomousSupersystem",
                    "autonomous drive, drivetrain, and position are required");
        }
        this.drive = drive;
        this.drivetrain = drivetrain;
        this.position = position;
        this.shooter = shooter;
    }

    public AutonomousSubsystem drive() {
        return drive;
    }

    public DriveSubsystem drivetrain() {
        return drivetrain;
    }

    public PositionSubsystem position() {
        return position;
    }

    public ShooterSubsystem shooter() {
        return shooter;
    }

    public void stopAll() {
        if (drivetrain != null) {
            drivetrain.stop();
        }
        if (shooter != null) {
            shooter.stop();
        }
    }

    @Override
    public void stop() {
        stopAll();
    }
}
