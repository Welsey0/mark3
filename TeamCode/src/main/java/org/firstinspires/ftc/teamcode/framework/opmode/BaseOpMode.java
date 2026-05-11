package org.firstinspires.ftc.teamcode.framework.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.framework.command.CommandScheduler;

/**
 * Base class for iterative FTC OpModes using the custom framework.
 *
 * This class should connect FTC lifecycle methods to the framework:
 * - init()
 * - init_loop()
 * - start()
 * - loop()
 * - stop()
 *
 * It should create and run the robot container and scheduler.
 * It should not contain mechanism-specific behavior.
 */
public abstract class BaseOpMode extends OpMode {

	protected final CommandScheduler scheduler = CommandScheduler.getInstance();

	@Override
	public final void init() {
		scheduler.cancelAll();
		onInit();
	}

	@Override
	public final void init_loop() {
		onInitLoop();
	}

	@Override
	public final void start() {
		onStartMode();
	}

	@Override
	public final void loop() {
		scheduler.run();
		onLoopMode();
	}

	@Override
	public final void stop() {
		try {
			onStopMode();
		} finally {
			scheduler.cancelAll();
		}
	}

	protected abstract void onInit();

	protected void onInitLoop() { }

	protected void onStartMode() { }

	protected void onLoopMode() { }

	protected void onStopMode() { }
}