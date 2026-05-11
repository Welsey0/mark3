package org.firstinspires.ftc.teamcode.framework.command;

public class CommandScheduler {
	// Singleton instance
	private static final CommandScheduler INSTANCE = new CommandScheduler();

	public static CommandScheduler getInstance() { return INSTANCE; }

	// Currently scheduled commands
	private final java.util.Set<Command> scheduled = new java.util.LinkedHashSet<>();

	// Map each subsystem to the command that currently requires it
	private final java.util.Map<Subsystem, Command> requirements = new java.util.HashMap<>();

	// Default commands for subsystems (run when nothing else requires the subsystem)
	private final java.util.Map<Subsystem, Command> defaultCommands = new java.util.HashMap<>();

	private CommandScheduler() { }

	/**
	 * Schedule a command to run. If the command requires subsystems currently
	 * owned by other commands, those commands will be cancelled.
	 */
	public synchronized void schedule(Command cmd) {
		if (cmd == null) return;

		// Cancel conflicting commands
		for (Subsystem s : cmd.getRequirements()) {
			Command occupying = requirements.get(s);
			if (occupying != null && occupying != cmd) {
				occupying.end(true);
				scheduled.remove(occupying);
				// free any requirements held by the occupying command
				for (Subsystem r : occupying.getRequirements()) {
					if (requirements.get(r) == occupying) {
						requirements.remove(r);
					}
				}
			}
			requirements.put(s, cmd);
		}

		if (!scheduled.contains(cmd)) {
			cmd.initialize();
			scheduled.add(cmd);
		}
	}

	/**
	 * Run the scheduler. Call this periodically from your OpMode main loop.
	 */
	public synchronized void run() {
		// copy to avoid concurrent modification
		java.util.List<Command> copy = new java.util.ArrayList<>(scheduled);
		for (Command c : copy) {
			c.execute();
			if (c.isFinished()) {
				c.end(false);
				scheduled.remove(c);
				// release requirements and schedule defaults
				for (Subsystem s : c.getRequirements()) {
					if (requirements.get(s) == c) {
						requirements.remove(s);
						Command def = defaultCommands.get(s);
						if (def != null && !scheduled.contains(def)) {
							schedule(def);
						}
					}
				}
			}
		}
	}

	/**
	 * Register a default command for a subsystem. A default command will be
	 * scheduled automatically when the subsystem becomes free.
	 */
	public synchronized void setDefaultCommand(Subsystem subsystem, Command command) {
		if (subsystem == null) return;
		if (command == null) {
			defaultCommands.remove(subsystem);
		} else {
			defaultCommands.put(subsystem, command);
		}
	}

	/**
	 * Cancel all running commands and clear requirements.
	 */
	public synchronized void cancelAll() {
		for (Command c : new java.util.ArrayList<>(scheduled)) {
			c.end(true);
		}
		scheduled.clear();
		requirements.clear();
	}
}

