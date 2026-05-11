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
        // Use the same motor names as Mark 2 for compatibility with existing configs
        public static final String FRONT_LEFT = "frontLeft";
        public static final String BACK_LEFT = "backLeft";
        public static final String FRONT_RIGHT = "frontRight";
        public static final String BACK_RIGHT = "backRight";

        // Teleop scaling (0..1)
        public static final double TELEOP_POWER_SCALE = 1.0;

        // Deadband for joystick input
        public static final double JOYSTICK_DEADBAND = 0.05;
        // Whether to set motors to BRAKE when power is zero
        public static final boolean ZERO_POWER_BRAKE = true;
    }
}