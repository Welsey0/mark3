package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.hardware.drive.DriveHardware;

/**
 * Central hardware owner for the robot.
 *
 * This class is responsible for:
 * - retrieving FTC devices from hardwareMap
 * - creating hardware wrappers
 * - grouping robot hardware by mechanism
 *
 * This is the main boundary between FTC SDK devices and your robot logic.
 * Keep direct FTC-specific access here and in the mechanism hardware classes.
 */
public class RobotHardware {
	public final DriveHardware drive = new DriveHardware();

	public void init(HardwareMap hardwareMap) {
		drive.init(hardwareMap);
	}
}