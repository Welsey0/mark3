package org.firstinspires.ftc.teamcode.framework;

public final class RobotInitializationException extends RuntimeException {
    public RobotInitializationException(String subsystem, String message, Throwable cause) {
        super("Failed to initialize " + subsystem + ": " + message, cause);
    }

    public RobotInitializationException(String subsystem, String message) {
        super("Failed to initialize " + subsystem + ": " + message);
    }
}
