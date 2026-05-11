package org.firstinspires.ftc.teamcode.robot;

/**
 * Central repository for all robot constants.
 *
 * This file must contain only fixed values:
 * - hardware device names
 * - port numbers
 * - tuning values
 * - motion limits
 * - servo positions
 * - geometry measurements
 *
 * This file should NOT contain:
 * - hardware objects
 * - control flow
 * - subsystem logic
 * - command behavior
 *
 * Goal:
 * Keep robot configuration in one place so tuning and maintenance are easy.
 * If a value changes on the robot, this is the first place to update it.
 */
public final class Constants {
    private Constants() {}

    public static final class Drive {
        // ========================= EDIT HERE FIRST =========================
        // Most teams only need to change values in this section.

        // Use the same motor names as Mark 2 for compatibility with existing configs
        public static final String FRONT_LEFT = "frontLeft";
        public static final String BACK_LEFT = "backLeft";
        public static final String FRONT_RIGHT = "frontRight";
        public static final String BACK_RIGHT = "backRight";

        // Optional IMU name for heading-aware drive; only used when enabled.
        public static final String IMU_NAME = "imu";

        // Teleop scaling (0..1)
        public static final double TELEOP_POWER_SCALE = 1.0;
        public static final double ROTATION_POWER_SCALE = 1.0;

        // Deadband for joystick input
        public static final double JOYSTICK_DEADBAND = 0.05;

        // Input sign conventions. Keep these values to match Mark 2 behavior.
        public static final double FORWARD_INPUT_SIGN = -1.0; // left_stick_y is inverted in FTC
        public static final double STRAFE_INPUT_SIGN = 1.0;
        public static final double TURN_INPUT_SIGN = -1.0;

        // Field-centric behavior; matches Mark 2 drive transform when enabled.
        public static final boolean FIELD_CENTRIC_ENABLED = true;
        public static final double HEADING_OFFSET_RADIANS = 0.0;

        // Whether to set motors to BRAKE when power is zero
        public static final boolean ZERO_POWER_BRAKE = true;

        // Motor direction inversion to match physical mounting.
        public static final boolean FRONT_LEFT_REVERSED = false;
        public static final boolean BACK_LEFT_REVERSED = false;
        public static final boolean FRONT_RIGHT_REVERSED = true;
        public static final boolean BACK_RIGHT_REVERSED = true;

        // If true, attempt to read yaw from IMU for heading-aware driving.
        public static final boolean USE_IMU_FOR_HEADING = true;
    }
}