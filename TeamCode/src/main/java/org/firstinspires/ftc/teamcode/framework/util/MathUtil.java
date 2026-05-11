package org.firstinspires.ftc.teamcode.framework.util;

/**
 * General math helpers for robot control.
 *
 * This is for small reusable functions only:
 * - clamp
 * - deadband
 * - interpolation
 * - angle normalization
 *
 * Keep this class pure and dependency-free if possible.
 */
public final class MathUtil {
    private MathUtil() {}

    public static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    /**
     * Apply a symmetric deadband around zero.
     * If |v| <= deadband returns 0, otherwise returns scaled value in [-1,1].
     */
    public static double applyDeadband(double v, double deadband) {
        if (Math.abs(v) <= deadband) return 0.0;
        // scale so the output spans full -1..1 outside the deadband
        if (v > 0) return (v - deadband) / (1.0 - deadband);
        else return (v + deadband) / (1.0 - deadband);
    }
}
