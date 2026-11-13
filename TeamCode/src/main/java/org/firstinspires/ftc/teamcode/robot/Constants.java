package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

/**
 * Central repository for all robot constants.
 * <p>
 * Keep robot configuration in one place so tuning and maintenance are easy.
 * If a value changes on the robot, this is the place to update it.
 */
public final class Constants {
    private Constants() {}

    public static final class Drive {
        public static final String FRONT_LEFT = "FL";
        public static final String BACK_LEFT = "BL";
        public static final String FRONT_RIGHT = "FR";
        public static final String BACK_RIGHT = "BR";

        // TeleOp tuning
        public static final double TELEOP_POWER_SCALE = 1.0;
        public static final double ROTATION_POWER_SCALE = 1.0;
        public static final double SLOW_MODE_SCALE = 0.35;
        public static final double JOYSTICK_DEADZONE = 0.05;
        public static final double FORWARD_INPUT_SIGN = -1.0;
        public static final double STRAFE_INPUT_SIGN = -1.0;
        public static final double TURN_INPUT_SIGN = -1.0;
        public static final boolean FIELD_CENTRIC_ENABLED = false;
        public static final double HEADING_OFFSET_RADIANS = 0.0;
        public static final boolean ZERO_POWER_BRAKE = true;
        public static final boolean FRONT_LEFT_REVERSED = false;
        public static final boolean BACK_LEFT_REVERSED = false;
        public static final boolean FRONT_RIGHT_REVERSED = true;
        public static final boolean BACK_RIGHT_REVERSED = true;
    }

    public static final class Shooter {
        public enum Configuration {
            SINGLE_WHEEL,
            DOUBLE_WHEEL,
            TWIN_SINGLE_WHEEL
        }

        public static final Configuration CONFIGURATION = Configuration.DOUBLE_WHEEL;
        public static final String TOP = "shooterTop";
        public static final String BOTTOM = "shooterBottom";
        public static final boolean TOP_REVERSED = false;
        public static final boolean BOTTOM_REVERSED = false;
        public static final boolean ZERO_POWER_BRAKE = false;
        public static final double INPUT_SIGN = 1.0;
        public static final double POWER_SCALE = 1.0;
    }

    public static final class Turret {
        public enum Configuration {
            MOTOR,
            SERVO
        }

        public static final Configuration CONFIGURATION = Configuration.SERVO;
        public static final String ROTATIONACTUATOR = "rotActuator";
        public static final boolean ROTATIONACTUATOR_REVERSED = false;
        public static final boolean ZERO_POWER_BRAKE = false;
        public static final double POWER_SCALE = 1.0;
    }

    public static final class Sensors {
        public static final String IMU_NAME = "imu";
        public static final RevHubOrientationOnRobot HUB_ORIENTATION = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );

        public static final String ODOMETRY_PARALLEL_NAME = "odoParallel";
        public static final String ODOMETRY_PERPENDICULAR_NAME = "odoPerpendicular";
        public static final double ODOMETRY_TICKS_PER_REV = 2000.0;
        public static final double ODOMETRY_WHEEL_DIAMETER_MM = 38.1;
        // Offsets are measured from the robot center. Positive X is forward and
        // positive Y is left when viewed from above.
        public static final double ODOMETRY_PARALLEL_Y_OFFSET_MM = 0.0;
        public static final double ODOMETRY_PERPENDICULAR_X_OFFSET_MM = 0.0;
        public static final DcMotorSimple.Direction ODOMETRY_PARALLEL_DIRECTION =
                DcMotorSimple.Direction.FORWARD;
        public static final DcMotorSimple.Direction ODOMETRY_PERPENDICULAR_DIRECTION =
                DcMotorSimple.Direction.FORWARD;
    }
}