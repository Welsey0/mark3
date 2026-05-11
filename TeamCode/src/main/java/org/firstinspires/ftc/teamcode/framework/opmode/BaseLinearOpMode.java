package org.firstinspires.ftc.teamcode.framework.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.framework.command.CommandScheduler;

/**
 * Base class for linear FTC OpModes using the custom framework.
 *
 * Use this when you want a linear autonomous-style structure.
 * It should provide a clean way to:
 * - initialize the robot
 * - run command sequences
 * - stop safely
 *
 * Like BaseOpMode, this should only bridge FTC lifecycle to framework code.
 */
public abstract class BaseLinearOpMode extends LinearOpMode {

	protected final CommandScheduler scheduler = CommandScheduler.getInstance();

	@Override
	public final void runOpMode() throws InterruptedException {
		scheduler.cancelAll();
		onInitialize();
		onInitialized();

		waitForStart();
		if (isStopRequested()) {
			scheduler.cancelAll();
			return;
		}

		onStartMode();
		while (opModeIsActive() && !isStopRequested()) {
			scheduler.run();
			onLoopMode();
			idle();
		}

		try {
			onStopMode();
		} finally {
			scheduler.cancelAll();
		}
	}

	protected abstract void onInitialize() throws InterruptedException;

	protected void onInitialized() { }

	protected void onStartMode() { }

	protected void onLoopMode() { }

	protected void onStopMode() { }
}