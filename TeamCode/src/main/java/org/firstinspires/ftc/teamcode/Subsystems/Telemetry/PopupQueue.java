package org.firstinspires.ftc.teamcode.Subsystems.Telemetry;

import java.util.ArrayDeque;

public class PopupQueue {
    private final ArrayDeque<TelemetryPopup> queue = new ArrayDeque<>();

    public void enqueue(TelemetryPopup popup) {
        if (popup != null) {
            queue.addLast(popup);
        }
    }

    public TelemetryPopup peek() {
        return queue.peekFirst();
    }

    public TelemetryPopup dismiss() {
        return queue.pollFirst();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public void clear() {
        queue.clear();
    }
}

