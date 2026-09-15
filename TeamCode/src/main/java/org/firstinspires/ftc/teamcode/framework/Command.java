package org.firstinspires.ftc.teamcode.framework;

/**
 * Template for a single executable robot action.
 * <p>
 */
public interface Command {

	/**
	 * Called once when the command is initially scheduled.
	 */
	default void initialize() { }

	/**
	 * Called repeatedly while the command is scheduled.
	 */
	default void execute() { }

	/**
	 * Return true when the command has finished and should be ended.
	 */
	default boolean isFinished() { return false; }

	/**
	 * Called once when the command ends or is interrupted.
	 * @param interrupted true if the command was canceled/interrupted
	 */
	default void end(boolean interrupted) { }

	/**
	 * The set of subsystems required by this command. The scheduler will
	 * prevent two commands that require the same subsystem from running
	 * concurrently.
	 */
	default java.util.Set<Subsystem> getRequirements() {
		return java.util.Collections.emptySet();
	}
}