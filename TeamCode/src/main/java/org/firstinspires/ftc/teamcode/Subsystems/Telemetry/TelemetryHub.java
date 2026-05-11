package org.firstinspires.ftc.teamcode.Subsystems.Telemetry;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BotConfig;
import org.firstinspires.ftc.teamcode.Subsystems.Summaries.NumericMetric;
import org.firstinspires.ftc.teamcode.Subsystems.Summaries.RobotSummary;
import org.firstinspires.ftc.teamcode.Subsystems.Summaries.SummaryPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TelemetryHub {
    private static TelemetryHub activeHub;

    public static void bindGlobal(TelemetryHub hub) {
        activeHub = hub;
    }

    public static void unbindGlobal() {
        activeHub = null;
    }

    public static void postInfo(String title, String message) {
        post(new TelemetryPopup(title, message, PopupSeverity.INFO, true));
    }

    public static void postWarning(String title, String message) {
        post(new TelemetryPopup(title, message, PopupSeverity.WARNING, true));
    }

    public static void postError(String title, String message) {
        post(new TelemetryPopup(title, message, PopupSeverity.ERROR, true));
    }

    public static void post(String title, String message, PopupSeverity severity) {
        post(new TelemetryPopup(title, message, severity, true));
    }

    private static void post(TelemetryPopup popup) {
        if (activeHub != null) {
            activeHub.enqueuePopup(popup);
        }
    }

    private final Telemetry driverTelemetry;
    private final Telemetry dashboardTelemetry;
    private final PopupQueue popupQueue = new PopupQueue();
    private final BootAnimator bootAnimator = new BootAnimator();
    private final TouchpadNavigator touchpadNavigator = new TouchpadNavigator();
    private final List<BootCheck> bootChecks = new ArrayList<>();

    private TelemetryState state = TelemetryState.BOOT;
    private boolean bootComplete;
    private boolean menuOpen;
    private int bootTick;
    private int summaryPageIndex;
    private int menuSelectionIndex;

    public TelemetryHub(Telemetry driverTelemetry, Telemetry dashboardTelemetry) {
        this.driverTelemetry = driverTelemetry;
        this.dashboardTelemetry = dashboardTelemetry;
        this.driverTelemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
    }

    public void beginBoot() {
        bootChecks.clear();
        popupQueue.clear();
        bootTick = 0;
        bootComplete = false;
        menuOpen = false;
        state = TelemetryState.BOOT;
        summaryPageIndex = BotConfig.Telemetry.defaultSummaryPage;
        menuSelectionIndex = summaryPageIndex;
    }

    public void addBootCheck(String label, boolean ok, String detail) {
        bootChecks.add(new BootCheck(label, ok, detail));
    }

    public void finishBoot() {
        bootComplete = true;
        state = popupQueue.isEmpty() ? TelemetryState.SUMMARY : TelemetryState.POPUP;
    }

    public boolean isBootComplete() {
        return bootComplete;
    }

    public TelemetryState getState() {
        return state;
    }

    public int getSummaryPageIndex() {
        return summaryPageIndex;
    }

    public void setSummaryPageIndex(int summaryPageIndex) {
        this.summaryPageIndex = summaryPageIndex;
        this.menuSelectionIndex = summaryPageIndex;
    }

    public int getPopupCount() {
        return popupQueue.size();
    }

    public void enqueuePopup(TelemetryPopup popup) {
        popupQueue.enqueue(popup);
        if (bootComplete) {
            state = TelemetryState.POPUP;
        }
    }

    public void update(Gamepad gamepad, RobotSummary summary) {
        RobotSummary safeSummary = summary == null ? new RobotSummary() : summary;

        if (!bootComplete) {
            renderBoot();
            publishDashboard(safeSummary);
            bootTick++;
            return;
        }

        if (!popupQueue.isEmpty()) {
            state = TelemetryState.POPUP;
            handlePopupInput(gamepad);
            renderPopup();
            publishDashboard(safeSummary);
            return;
        }

        TouchpadAction action = touchpadNavigator.poll(gamepad, menuOpen);
        handleSummaryInput(action, safeSummary);

        if (menuOpen) {
            state = TelemetryState.MENU;
            renderMenu(safeSummary);
        } else {
            state = TelemetryState.SUMMARY;
            renderSummary(safeSummary);
        }

        publishDashboard(safeSummary);
    }

    private void handleSummaryInput(TouchpadAction action, RobotSummary summary) {
        int pageCount = summary.getPageCount();
        if (pageCount <= 0) {
            summaryPageIndex = 0;
            menuSelectionIndex = 0;
            if (action == TouchpadAction.MENU_TOGGLE) {
                menuOpen = !menuOpen;
            }
            return;
        }

        switch (action) {
            case MENU_TOGGLE:
                menuOpen = !menuOpen;
                if (menuOpen) {
                    menuSelectionIndex = summaryPageIndex;
                }
                break;
            case UP:
                if (menuOpen) {
                    menuSelectionIndex = summary.clampIndex(menuSelectionIndex - 1);
                }
                break;
            case DOWN:
                if (menuOpen) {
                    menuSelectionIndex = summary.clampIndex(menuSelectionIndex + 1);
                }
                break;
            case SELECT:
                if (menuOpen) {
                    summaryPageIndex = summary.clampIndex(menuSelectionIndex);
                    menuOpen = false;
                }
                break;
            case BACK:
                if (menuOpen) {
                    menuOpen = false;
                }
                break;
            case NONE:
            default:
                break;
        }

        summaryPageIndex = summary.clampIndex(summaryPageIndex);
        menuSelectionIndex = summary.clampIndex(menuSelectionIndex);
    }

    private void handlePopupInput(Gamepad gamepad) {
        TouchpadAction action = touchpadNavigator.poll(gamepad, true);
        if (action == TouchpadAction.SELECT || action == TouchpadAction.BACK || action == TouchpadAction.MENU_TOGGLE) {
            popupQueue.dismiss();
            if (popupQueue.isEmpty()) {
                state = menuOpen ? TelemetryState.MENU : TelemetryState.SUMMARY;
            }
        }
    }

    private void renderBoot() {
        driverTelemetry.clearAll();
        driverTelemetry.addLine("=== MARK 3 BOOT ===");
        if (BotConfig.Telemetry.bootAnimationEnabled) {
            driverTelemetry.addLine(bootAnimator.frame(bootTick));
        } else {
            driverTelemetry.addLine("[boot animation disabled]");
        }
        driverTelemetry.addLine("--- Startup Checks ---");

        if (bootChecks.isEmpty()) {
            driverTelemetry.addLine("Waiting for boot checks...");
        } else {
            for (BootCheck check : bootChecks) {
                String status = check.isOk() ? "[OK]" : "[WARN]";
                String detail = check.getDetail() == null || check.getDetail().isEmpty() ? "" : " - " + check.getDetail();
                driverTelemetry.addLine(status + " " + check.getLabel() + detail);
            }
        }

        driverTelemetry.addLine("---");
        driverTelemetry.addLine("Init phase: hold touchpad for menu once OpMode starts.");
        driverTelemetry.update();
    }

    private void renderSummary(RobotSummary summary) {
        driverTelemetry.clearAll();
        SummaryPage page = summary.getPage(summaryPageIndex);

        driverTelemetry.addLine("=== SUMMARY ===");
        if (page == null) {
            driverTelemetry.addLine("No summary pages configured.");
        } else {
            driverTelemetry.addLine(String.format(Locale.US, "Page %d/%d: %s", summaryPageIndex + 1, Math.max(summary.getPageCount(), 1), page.getTitle()));
            for (String line : page.getLines()) {
                driverTelemetry.addLine(line);
            }
        }

        driverTelemetry.addLine("--- Controls ---");
        driverTelemetry.addLine("Touchpad click: menu");
        driverTelemetry.addLine("Touchpad right: select/OK");
        driverTelemetry.addLine("Touchpad left: back/no");
        driverTelemetry.addLine("Touchpad top: up");
        driverTelemetry.addLine("Touchpad bottom: down");
        if (popupQueue.isEmpty()) {
            driverTelemetry.addLine("No active popups.");
        } else {
            driverTelemetry.addLine("Active popups in queue: " + popupQueue.size());
        }
        driverTelemetry.update();
    }

    private void renderMenu(RobotSummary summary) {
        driverTelemetry.clearAll();
        driverTelemetry.addLine("=== MENU ===");

        if (summary.isEmpty()) {
            driverTelemetry.addLine("No pages available.");
        } else {
            for (int i = 0; i < summary.getPageCount(); i++) {
                SummaryPage page = summary.getPage(i);
                String cursor = i == menuSelectionIndex ? ">" : " ";
                driverTelemetry.addLine(String.format(Locale.US, "%s %d) %s", cursor, i + 1, page.getTitle()));
            }
        }

        driverTelemetry.addLine("--- Controls ---");
        driverTelemetry.addLine("Top / Bottom: move");
        driverTelemetry.addLine("Right: select");
        driverTelemetry.addLine("Left: back/close");
        driverTelemetry.addLine("Touchpad click: close menu");
        driverTelemetry.update();
    }

    private void renderPopup() {
        driverTelemetry.clearAll();
        TelemetryPopup popup = popupQueue.peek();

        driverTelemetry.addLine("=== POPUP ===");
        if (popup == null) {
            driverTelemetry.addLine("No popup available.");
        } else {
            driverTelemetry.addLine("[" + popup.getSeverity().name() + "] " + popup.getTitle());
            driverTelemetry.addLine(popup.getMessage());
            driverTelemetry.addLine("---");
            driverTelemetry.addLine("Right / Left / Touchpad click to dismiss");
            driverTelemetry.addLine("Queued popups remaining: " + popupQueue.size());
        }
        driverTelemetry.update();
    }

    private void publishDashboard(RobotSummary summary) {
        dashboardTelemetry.clearAll();

        dashboardTelemetry.addData("UI/State", state.ordinal());
        dashboardTelemetry.addData("UI/BootComplete", bootComplete ? 1 : 0);
        dashboardTelemetry.addData("UI/MenuOpen", menuOpen ? 1 : 0);
        dashboardTelemetry.addData("UI/PopupCount", popupQueue.size());
        dashboardTelemetry.addData("UI/SummaryPage", summaryPageIndex);

        for (SummaryPage page : summary.getPages()) {
            for (NumericMetric metric : page.getMetrics()) {
                dashboardTelemetry.addData(page.getTitle() + "/" + metric.getLabel(), metric.getValue());
            }
        }

        dashboardTelemetry.update();
    }
}

