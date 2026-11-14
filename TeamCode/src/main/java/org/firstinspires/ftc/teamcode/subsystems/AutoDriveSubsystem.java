package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.framework.TelemetrySource;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;

/**
 * Owns only autonomous drive/path following. Step selection and routine
 * orchestration belong in the opmode or an autonomous supersystem.
 */

// Built on TheGodObject by casperwz

public final class AutoDriveSubsystem implements Subsystem, TelemetrySource {
    private static final double FEED_FORWARD = 0.025;
    private final DriveSubsystem drive;
    private final PositionSubsystem position;
    private final PIDController xController = new PIDController(100, 0.02, 0.001);
    private final PIDController yController = new PIDController(100, 0.02, 0.001);
    private final PIDController headingController = new PIDController(100, 0.02, 0.001);
    private final ElapsedTime timer = new ElapsedTime();
    private Path path;
    private MotionProfile profile;
    private double targetHeading;
    private boolean followingPath;
    private boolean complete;
    private String error;

    public AutoDriveSubsystem(DriveSubsystem drive, PositionSubsystem position) {
        if (drive == null || position == null) {
            throw new RobotInitializationException(
                    "AutoDriveSubsystem", "drive and position subsystems are required");
        }
        this.drive = drive;
        this.position = position;
    }

    public void followPath(Path path, MotionProfile profile, double targetHeadingRadians) {
        if (path == null || profile == null) {
            fail("path and profile are required");
            return;
        }
        this.path = path;
        this.profile = profile;
        this.targetHeading = targetHeadingRadians;
        xController.reset();
        yController.reset();
        headingController.reset();
        timer.reset();
        followingPath = true;
        complete = false;
        error = null;
    }

    @Override
    public void periodic() {
        if (error != null || complete) {
            return;
        }
        if (followingPath) {
            updatePath();
        }
    }

    double fb;
    double lr;
    double r;
    private void updatePath() {
        MotionProfile.State target = profile.calculate(path.length(), timer.seconds());
        double x = position.getX();
        double y = position.getY();
        double heading = position.getHeading();
        Point endpoint = path.positionAt(1.0);
        if (target.velocity == 0 && Math.hypot(endpoint.x - x, endpoint.y - y) < 50.0) {
            drive.stop();
            followingPath = false;
            complete = true;
            return;
        }
        double parameter = path.parameterAt(target.position);
        Point targetPoint = path.positionAt(parameter);
        Point derivative = path.derivativeAt(parameter);
        double angle = Math.atan2(derivative.y, derivative.x);
        double headingError = wrap(targetHeading - heading);
        fb = -(Math.cos(angle) * target.velocity * FEED_FORWARD
                + xController.calculate(x, targetPoint.x));
        lr = -(Math.sin(angle) * target.velocity * FEED_FORWARD
                + yController.calculate(y, targetPoint.y));
        r = -headingController.calculate(heading, heading + headingError);

        drive.driveWithHeading(fb, lr, r, heading);
    }

    public boolean isFinished() {
        return complete || error != null;
    }

    public boolean hasFailed() {
        return error != null;
    }

    public String getError() {
        return error;
    }

    public void fail(String message) {
        error = message == null ? "unknown autonomous error" : message;
        followingPath = false;
        drive.stop();
    }

    @Override
    public void stop() {
        followingPath = false;
        drive.stop();
    }

    @Override
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Autonomous state", error != null ? "ERROR" : complete ? "COMPLETE" : "RUNNING");
        if (error != null) telemetry.addData("Autonomous error", error);
        telemetry.addData("fb",fb);
        telemetry.addData("lr",lr);
        telemetry.addData("r",r);
    }

    private static double wrap(double angle) {
        while (angle > Math.PI) angle -= 2.0 * Math.PI;
        while (angle <= -Math.PI) angle += 2.0 * Math.PI;
        return angle;
    }

    public static final class Point {
        public final double x;
        public final double y;
        public Point(double x, double y) { this.x = x; this.y = y; }
    }

    public static final class Path {
        private final Point start, control1, control2, end;
        private final double[] arcTable = new double[101];
        private final double length;

        public Path(Point start, Point control1, Point control2, Point end) {
            this.start = start;
            this.control1 = control1;
            this.control2 = control2;
            this.end = end;
            length = buildArcTable();
        }

        public double length() { return length; }

        public Point positionAt(double t) {
            double u = 1.0 - t;
            return new Point(
                    u * u * u * start.x + 3 * u * u * t * control1.x
                            + 3 * u * t * t * control2.x + t * t * t * end.x,
                    u * u * u * start.y + 3 * u * u * t * control1.y
                            + 3 * u * t * t * control2.y + t * t * t * end.y);
        }

        public Point derivativeAt(double t) {
            double u = 1.0 - t;
            return new Point(
                    3 * u * u * (control1.x - start.x)
                            + 6 * u * t * (control2.x - control1.x)
                            + 3 * t * t * (end.x - control2.x),
                    3 * u * u * (control1.y - start.y)
                            + 6 * u * t * (control2.y - control1.y)
                            + 3 * t * t * (end.y - control2.y));
        }

        public double parameterAt(double distance) {
            if (distance <= 0) return 0;
            if (distance >= length) return 1;
            int low = 0, high = 100;
            while (low < high) {
                int middle = (low + high) / 2;
                if (arcTable[middle] < distance) low = middle + 1;
                else high = middle;
            }
            int before = low - 1;
            double fraction = (distance - arcTable[before])
                    / (arcTable[low] - arcTable[before]);
            return (before + fraction) / 100.0;
        }

        private double buildArcTable() {
            Point previous = positionAt(0);
            double total = 0;
            for (int i = 1; i <= 100; i++) {
                Point current = positionAt(i / 100.0);
                total += Math.hypot(current.x - previous.x, current.y - previous.y);
                arcTable[i] = total;
                previous = current;
            }
            return total;
        }
    }

    public static final class MotionProfile {
        private final double maxVelocity;
        private final double maxAcceleration;

        public MotionProfile(double maxVelocity, double maxAcceleration) {
            if (maxVelocity < 0 || maxAcceleration <= 0) {
                throw new IllegalArgumentException("invalid motion profile limits");
            }
            this.maxVelocity = maxVelocity;
            this.maxAcceleration = maxAcceleration;
        }

        public State calculate(double distance, double time) {
            double accelerationTime = maxVelocity / maxAcceleration;
            double accelerationDistance = 0.5 * maxAcceleration
                    * accelerationTime * accelerationTime;
            double actualVelocity = maxVelocity;
            if (2 * accelerationDistance > distance) {
                accelerationDistance = distance / 2;
                accelerationTime = Math.sqrt(2 * accelerationDistance / maxAcceleration);
                actualVelocity = maxAcceleration * accelerationTime;
            }
            double cruiseTime = actualVelocity == 0 ? 0
                    : (distance - 2 * accelerationDistance) / actualVelocity;
            double totalTime = 2 * accelerationTime + cruiseTime;
            if (time >= totalTime) return new State(distance, 0);
            if (time < accelerationTime) {
                return new State(0.5 * maxAcceleration * time * time,
                        maxAcceleration * time);
            }
            if (time < accelerationTime + cruiseTime) {
                return new State(accelerationDistance
                        + actualVelocity * (time - accelerationTime), actualVelocity);
            }
            double remaining = totalTime - time;
            return new State(distance - 0.5 * maxAcceleration * remaining * remaining,
                    maxAcceleration * remaining);
        }

        public static final class State {
            public final double position;
            public final double velocity;
            private State(double position, double velocity) {
                this.position = position;
                this.velocity = velocity;
            }
        }
    }
}
