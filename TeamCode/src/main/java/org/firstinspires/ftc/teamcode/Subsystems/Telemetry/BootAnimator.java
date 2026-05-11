package org.firstinspires.ftc.teamcode.Subsystems.Telemetry;

public class BootAnimator {
    private static final String[] FRAMES = {
            "[|] booting Mark 3...",
            "[/] booting Mark 3...",
            "[-] booting Mark 3...",
            "[\\] booting Mark 3..."
    };

    public String frame(int tick) {
        return FRAMES[Math.floorMod(tick, FRAMES.length)];
    }
}

