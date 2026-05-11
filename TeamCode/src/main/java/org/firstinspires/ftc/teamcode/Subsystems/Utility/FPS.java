package org.firstinspires.ftc.teamcode.Subsystems.Utility;

@SuppressWarnings("unused")
public class FPS {
    public long fps = 0;
    public long frametime = 0;
    private long startTime = System.nanoTime();

    public Long get() {
        long newTime = System.nanoTime();
        long deltaTime = (newTime - startTime) / 1_000_000; // convert to milliseconds
        if (deltaTime > 0) {
            frametime = deltaTime;
            fps = 1000 / frametime; // frames per second
        }
        startTime = newTime;
        return fps;
    }
}
