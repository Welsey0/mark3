package org.firstinspires.ftc.teamcode.framework.command;

/**
 * Represents a robot subsystem.
 *
 * A subsystem owns one major robot function and its associated hardware:
 * - drivetrain
 * - arm
 * - intake
 * - vision
 *
 * Responsibilities:
 * - own hardware access through wrappers
 * - expose safe control methods
 * - maintain internal state
 * - provide periodic updates if needed
 *
 * Subsystems should be the main place where mechanism-specific behavior lives.
 */
public interface Subsystem {
}