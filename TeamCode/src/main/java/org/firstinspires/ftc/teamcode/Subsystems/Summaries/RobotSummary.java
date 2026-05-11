package org.firstinspires.ftc.teamcode.Subsystems.Summaries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RobotSummary {
    private final List<SummaryPage> pages = new ArrayList<>();

    public RobotSummary addPage(SummaryPage page) {
        if (page != null) {
            pages.add(page);
        }
        return this;
    }

    public List<SummaryPage> getPages() {
        return Collections.unmodifiableList(pages);
    }

    public boolean isEmpty() {
        return pages.isEmpty();
    }

    public int getPageCount() {
        return pages.size();
    }

    public SummaryPage getPage(int index) {
        if (pages.isEmpty()) {
            return null;
        }

        int wrapped = Math.floorMod(index, pages.size());
        return pages.get(wrapped);
    }

    public int clampIndex(int index) {
        if (pages.isEmpty()) {
            return 0;
        }
        return Math.floorMod(index, pages.size());
    }
}

