package org.firstinspires.ftc.teamcode.framework;

/**
 * Represents one executable robot action.
 * <p>
 * A command should be small, reusable, and focused on one task.
 * Examples:
 * - drive forward
 * - run intake until object detected
 * - follow an autonomous sequence
 * <p>
 * Typical lifecycle:
 * - initialize()
 * - execute()
 * - isFinished()
 * - end(interrupted)
 * <p>
 * Commands should not directly own robot-wide architecture.
 * They should depend on subsystems.
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