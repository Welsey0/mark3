package org.firstinspires.ftc.teamcode.framework;

/**
 * Coordinates multiple subsystems without owning their hardware.
 * Supersystems contain robot-mode orchestration; mechanism behavior belongs
 * in the subsystem they call.
 */
public interface Supersystem {
    default void init() {
    }

    default void periodic() {
    }

    default void stop() {
    }
}
