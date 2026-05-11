package org.firstinspires.ftc.teamcode.Subsystems.Telemetry;

public class TelemetryPopup {
    private final String title;
    private final String message;
    private final PopupSeverity severity;
    private final boolean dismissible;

    public TelemetryPopup(String title, String message, PopupSeverity severity, boolean dismissible) {
        this.title = title;
        this.message = message;
        this.severity = severity;
        this.dismissible = dismissible;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public PopupSeverity getSeverity() {
        return severity;
    }

    public boolean isDismissible() {
        return dismissible;
    }
}

