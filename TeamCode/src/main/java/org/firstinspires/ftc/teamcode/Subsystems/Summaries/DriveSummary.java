package org.firstinspires.ftc.teamcode.Subsystems.Summaries;

import org.firstinspires.ftc.teamcode.BotConfig;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Motorcode;

import java.util.Locale;

public final class DriveSummary {
	private DriveSummary() {
	}

	public static SummaryPage build(double headingRadians, double speedScale, Motorcode command) {
		SummaryPage page = new SummaryPage("Drive");

		page.addLine("Field-centric: " + (BotConfig.Drive.fieldCentricEnabled ? "ON" : "OFF"));
		page.addLine(String.format(Locale.US, "Speed scale: %.2f", speedScale));
		page.addLine(String.format(Locale.US, "Heading: %.3f rad", headingRadians));
		page.addLine("Motorcode: " + command.toCodeString());

		page.addMetric("FieldCentric", BotConfig.Drive.fieldCentricEnabled ? 1 : 0);
		page.addMetric("Speed", speedScale);
		page.addMetric("HeadingRad", headingRadians);
		page.addMetric("FrontLeft", command.frontLeft);
		page.addMetric("FrontRight", command.frontRight);
		page.addMetric("BackLeft", command.backLeft);
		page.addMetric("BackRight", command.backRight);
		return page;
	}
}

