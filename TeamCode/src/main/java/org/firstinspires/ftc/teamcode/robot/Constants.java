package org.firstinspires.ftc.teamcode.robot;

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

        // Scaling of drive speed in Teleop
        public static final double TELEOP_POWER_SCALE = 1.0;
        public static final double ROTATION_POWER_SCALE = 1.0;
        public static final double SLOW_MODE_SCALE = 0.35;

        // Deadzone for joystick input
        public static final double JOYSTICK_DEADZONE = 0.05;

        // Input sign conventions for joystick input. Should probably never change.
        public static final double FORWARD_INPUT_SIGN = -1.0; // left_stick_y is inverted in FTC
        public static final double STRAFE_INPUT_SIGN = 1.0;
        public static final double TURN_INPUT_SIGN = -1.0;

        // Field-centric drive
        public static final boolean FIELD_CENTRIC_ENABLED = false;
        public static final double HEADING_OFFSET_RADIANS = 0.0;

        // Whether to set motors to BRAKE when power is zero
        public static final boolean ZERO_POWER_BRAKE = true;

        // Motor direction inversion to match physical mounting.
        public static final boolean FRONT_LEFT_REVERSED = false;
        public static final boolean BACK_LEFT_REVERSED = false;
        public static final boolean FRONT_RIGHT_REVERSED = true;
        public static final boolean BACK_RIGHT_REVERSED = true;

        // If true, attempt to read yaw from IMU for field-centric drive.
        public static final boolean USE_IMU_FOR_HEADING = true;
    }

    public static final class Sensors {
        public static final String IMU_NAME = "imu";
    }
}