package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.framework.Supersystem;
import org.firstinspires.ftc.teamcode.hardware.movement.DriveHardware;
import org.firstinspires.ftc.teamcode.hardware.movement.ShooterHardware;
import org.firstinspires.ftc.teamcode.hardware.movement.TurretHardware;
import org.firstinspires.ftc.teamcode.hardware.sensors.IMUHardware;
import org.firstinspires.ftc.teamcode.hardware.sensors.TwoPodOdometryHardware;
import org.firstinspires.ftc.teamcode.subsystems.AutoDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PositionSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TelemetrySubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.supersystems.AutonomousSupersystem;

import java.util.ArrayList;
import java.util.List;

public final class RobotContext {
    private final List<Subsystem> subsystems;
    private final TelemetrySubsystem telemetrySubsystem;
    private final DriveSubsystem drive;
    private final PositionSubsystem position;
    private final ShooterSubsystem shooter;
    private final AutoDriveSubsystem autonomous;
    private final TurretSubsystem turret;
    private final List<Supersystem> supersystems;

    private RobotContext(Builder builder) {
        this.subsystems = new ArrayList<>(builder.subsystems);
        this.telemetrySubsystem = builder.telemetrySubsystem;
        this.drive = builder.drive;
        this.position = builder.position;
        this.shooter = builder.shooter;
        this.autonomous = builder.autonomous;
        this.turret = builder.turret;
        this.supersystems = new ArrayList<>(builder.supersystems);
        this.subsystems.add(telemetrySubsystem);
    }

    public static Builder builder(HardwareMap hardwareMap, Telemetry telemetry) {
        return new Builder(hardwareMap, telemetry);
    }

    public void init() {
        for (Subsystem subsystem : subsystems) {
            subsystem.init();
        }
        for (Supersystem supersystem : supersystems) {
            supersystem.init();
        }
    }

    public void periodic() {
        for (Subsystem subsystem : subsystems) {
            if (subsystem != telemetrySubsystem) {
                subsystem.periodic();
            }
        }
        for (Supersystem supersystem : supersystems) {
            supersystem.periodic();
        }
        telemetrySubsystem.periodic();
    }

    public void stop() {
        for (int i = subsystems.size() - 1; i >= 0; i--) {
            subsystems.get(i).stop();
        }
        for (int i = supersystems.size() - 1; i >= 0; i--) {
            supersystems.get(i).stop();
        }
    }

    public DriveSubsystem drive() { return drive; }
    public PositionSubsystem position() { return position; }
    public ShooterSubsystem shooter() { return shooter; }
    public TurretSubsystem turret() { return turret; }
    public AutoDriveSubsystem autonomous() { return autonomous; }
    public List<Supersystem> supersystems() {
        return new ArrayList<>(supersystems);
    }

    public static final class Builder {
        private final HardwareMap hardwareMap;
        private final TelemetrySubsystem telemetrySubsystem;
        private final List<Subsystem> subsystems = new ArrayList<>();
        private DriveSubsystem drive;
        private PositionSubsystem position;
        private ShooterSubsystem shooter;
        private TurretSubsystem turret;
        private AutoDriveSubsystem autonomous;
        private final List<Supersystem> supersystems = new ArrayList<>();

        private Builder(HardwareMap hardwareMap, Telemetry telemetry) {
            if (hardwareMap == null) {
                throw new RobotInitializationException("RobotContext", "HardwareMap is null");
            }
            this.hardwareMap = hardwareMap;
            telemetrySubsystem = new TelemetrySubsystem(telemetry);
        }

        public Builder withDrive() {
            requireUnset(drive, "DriveSubsystem");
            DriveHardware hardware = new DriveHardware();
            initialize("DriveSubsystem", hardware);
            drive = new DriveSubsystem(hardware);
            subsystems.add(drive);
            return this;
        }

        public Builder withPosition(boolean useImu, boolean useOdometry) {
            requireUnset(position, "PositionSubsystem");
            if (!useImu && !useOdometry) {
                throw new RobotInitializationException(
                        "PositionSubsystem", "at least one position sensor must be selected");
            }
            if (useOdometry && !useImu) {
                throw new RobotInitializationException(
                        "PositionSubsystem",
                        "two-pod odometry requires the IMU for heading");
            }
            IMUHardware imu = useImu ? initialize("IMUHardware", new IMUHardware()) : null;
            TwoPodOdometryHardware odometry =
                    useOdometry ? initialize("TwoPodOdometryHardware", new TwoPodOdometryHardware()) : null;
            position = new PositionSubsystem(imu, odometry);
            subsystems.add(position);
            return this;
        }

        public Builder withShooter() {
            return withShooter(
                    Constants.Shooter.CONFIGURATION,
                    Constants.Shooter.TOP,
                    Constants.Shooter.BOTTOM);
        }

        public Builder withShooter(
                Constants.Shooter.Configuration configuration,
                String primaryMotorName,
                String secondaryMotorName) {
            requireUnset(shooter, "ShooterSubsystem");
            ShooterHardware hardware = new ShooterHardware(
                    configuration, primaryMotorName, secondaryMotorName);
            initialize("ShooterSubsystem", hardware);
            shooter = new ShooterSubsystem(hardware);
            subsystems.add(shooter);
            return this;
        }

        public Builder withTurret() {
            return withTurret(
                    Constants.Turret.CONFIGURATION,
                    Constants.Turret.ROTATIONACTUATOR);
        }

        public Builder withTurret(
                Constants.Turret.Configuration configuration,
                String actuatorName) {
            requireUnset(turret, "TurretSubsystem");
            TurretHardware hardware = new TurretHardware(configuration, actuatorName);
            initialize("TurretSubsystem", hardware);
            turret = new TurretSubsystem(hardware);
            subsystems.add(turret);
            return this;
        }

        public Builder withAutonomousSupersystem() {
            requireDependency(autonomous, "AutonomousSupersystem", "AutoDriveSubsystem");
            supersystems.add(new AutonomousSupersystem(
                    autonomous, drive, position, shooter));
            return this;
        }

        public Builder withAutonomous() {
            requireUnset(autonomous, "AutoDriveSubsystem");
            requireDependency(drive, "AutoDriveSubsystem", "DriveSubsystem");
            requireDependency(position, "AutoDriveSubsystem", "PositionSubsystem");
            autonomous = new AutoDriveSubsystem(drive, position);
            telemetrySubsystem.addSource(autonomous);
            subsystems.add(autonomous);
            return this;
        }

        public Builder withSupersystem(Supersystem supersystem) {
            if (supersystem == null) {
                throw new RobotInitializationException("RobotContext", "supersystem cannot be null");
            }
            supersystems.add(supersystem);
            return this;
        }

        public RobotContext build() {
            if (subsystems.isEmpty()) {
                throw new RobotInitializationException(
                        "RobotContext", "no subsystems were selected for this opmode");
            }
            return new RobotContext(this);
        }

        private <T> T initialize(String name, T hardware) {
            try {
                if (hardware instanceof org.firstinspires.ftc.teamcode.framework.Hardware) {
                    ((org.firstinspires.ftc.teamcode.framework.Hardware) hardware).init(hardwareMap);
                }
                return hardware;
            } catch (RobotInitializationException exception) {
                throw exception;
            } catch (RuntimeException exception) {
                throw new RobotInitializationException(name, exception.getMessage(), exception);
            }
        }

        private static void requireDependency(
                Object dependency, String subsystem, String dependencyName) {
            if (dependency == null) {
                throw new RobotInitializationException(
                        subsystem, dependencyName + " must be selected first");
            }
        }

        private static void requireUnset(Object value, String name) {
            if (value != null) {
                throw new RobotInitializationException(name, "selected more than once");
            }
        }
    }
}
