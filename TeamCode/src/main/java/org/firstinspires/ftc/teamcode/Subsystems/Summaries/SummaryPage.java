package org.firstinspires.ftc.teamcode.Subsystems.Summaries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SummaryPage {
    private final String title;
    private final List<String> lines = new ArrayList<>();
    private final List<NumericMetric> metrics = new ArrayList<>();

    public SummaryPage(String title) {
        this.title = title;
    }

    public SummaryPage addLine(String line) {
        if (line != null && !line.isEmpty()) {
            lines.add(line);
        }
        return this;
    }

    public SummaryPage addMetric(String label, double value) {
        metrics.add(new NumericMetric(label, value));
        return this;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public List<NumericMetric> getMetrics() {
        return Collections.unmodifiableList(metrics);
    }
}

