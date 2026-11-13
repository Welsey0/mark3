package org.firstinspires.ftc.teamcode.hardware.sensors;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.framework.Hardware;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.robot.Constants;

/**
 * Two-wheel dead-wheel localizer. The IMU supplies the heading that a two-wheel
 * setup cannot determine from encoder measurements alone.
 */
public class TwoPodOdometryHardware implements Hardware {
    private static final double TWO_PI = 2.0 * Math.PI;

    public DcMotorEx parallelPod;
    public DcMotorEx perpendicularPod;

    private int lastParallelTicks;
    private int lastPerpendicularTicks;
    private double lastHeading;
    private double x;
    private double y;
    private boolean initialized;

    @Override
    public void init(HardwareMap hardwareMap) {
        try {
            parallelPod = hardwareMap.get(DcMotorEx.class, Constants.Sensors.ODOMETRY_PARALLEL_NAME);
            perpendicularPod =
                    hardwareMap.get(DcMotorEx.class, Constants.Sensors.ODOMETRY_PERPENDICULAR_NAME);

            configure(parallelPod, Constants.Sensors.ODOMETRY_PARALLEL_DIRECTION);
            configure(perpendicularPod, Constants.Sensors.ODOMETRY_PERPENDICULAR_DIRECTION);

            lastParallelTicks = parallelPod.getCurrentPosition();
            lastPerpendicularTicks = perpendicularPod.getCurrentPosition();
            initialized = false;
        } catch (RuntimeException exception) {
            parallelPod = null;
            perpendicularPod = null;
            throw new RobotInitializationException(
                    "TwoPodOdometryHardware",
                    "expected odometry pods named "
                            + Constants.Sensors.ODOMETRY_PARALLEL_NAME + " and "
                            + Constants.Sensors.ODOMETRY_PERPENDICULAR_NAME,
                    exception);
        }
    }

    public Pose2D getPosition(double heading) {
        if (parallelPod == null || perpendicularPod == null) {
            return null;
        }

        int parallelTicks = parallelPod.getCurrentPosition();
        int perpendicularTicks = perpendicularPod.getCurrentPosition();
        if (!initialized) {
            lastParallelTicks = parallelTicks;
            lastPerpendicularTicks = perpendicularTicks;
            lastHeading = heading;
            initialized = true;
            return pose(heading);
        }

        double headingDelta = normalize(heading - lastHeading);
        double parallelDistance = ticksToMillimeters(parallelTicks - lastParallelTicks);
        double perpendicularDistance =
                ticksToMillimeters(perpendicularTicks - lastPerpendicularTicks);

        double localX = parallelDistance
                + Constants.Sensors.ODOMETRY_PARALLEL_Y_OFFSET_MM * headingDelta;
        double localY = perpendicularDistance
                - Constants.Sensors.ODOMETRY_PERPENDICULAR_X_OFFSET_MM * headingDelta;
        double midpointHeading = lastHeading + headingDelta / 2.0;
        x += localX * Math.cos(midpointHeading) - localY * Math.sin(midpointHeading);
        y += localX * Math.sin(midpointHeading) + localY * Math.cos(midpointHeading);

        lastParallelTicks = parallelTicks;
        lastPerpendicularTicks = perpendicularTicks;
        lastHeading = heading;
        return pose(heading);
    }

    public void reset(double heading) {
        if (parallelPod == null || perpendicularPod == null) {
            return;
        }
        lastParallelTicks = parallelPod.getCurrentPosition();
        lastPerpendicularTicks = perpendicularPod.getCurrentPosition();
        lastHeading = heading;
        x = 0.0;
        y = 0.0;
        initialized = true;
    }

    private static void configure(DcMotorEx motor, DcMotorSimple.Direction direction) {
        motor.setDirection(direction);
        motor.setMode(com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private static double ticksToMillimeters(int ticks) {
        return ticks / Constants.Sensors.ODOMETRY_TICKS_PER_REV
                * (Math.PI * Constants.Sensors.ODOMETRY_WHEEL_DIAMETER_MM);
    }

    private Pose2D pose(double heading) {
        return new Pose2D(DistanceUnit.MM, x, y, AngleUnit.RADIANS, heading);
    }

    private static double normalize(double angle) {
        angle %= TWO_PI;
        if (angle > Math.PI) return angle - TWO_PI;
        if (angle < -Math.PI) return angle + TWO_PI;
        return angle;
    }
}
