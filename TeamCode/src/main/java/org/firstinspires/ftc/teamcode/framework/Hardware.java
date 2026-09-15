package org.firstinspires.ftc.teamcode.framework;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Contains a wrapper for FTC hardware calls.
 * <p>
 * Used by subsystems to standardize direct calls to and from hardware.
 */
public interface Hardware {
    /**
     * Called once at beginning to register hardware with FTC SDK.
     */
    default void init(HardwareMap hardwareMap) {
    }

    /**
     * Should freeze or stop robot motion. Can be called at any time.
     */
    default void stop() {
    }
}
