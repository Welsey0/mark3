package org.firstinspires.ftc.teamcode.Subsystems.Telemetry;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;

public class TouchpadNavigator {
    private boolean lastTouchpadPressed;

    public TouchpadAction poll(Gamepad gamepad, boolean menuOpen) {
        if (gamepad == null) {
            return TouchpadAction.NONE;
        }

        boolean pressed = readBoolean(gamepad, "touchpad", false);
        boolean risingEdge = pressed && !lastTouchpadPressed;
        lastTouchpadPressed = pressed;

        if (!risingEdge) {
            return TouchpadAction.NONE;
        }

        if (!menuOpen) {
            return TouchpadAction.MENU_TOGGLE;
        }

        double x = normalize(readFloat(gamepad, "touchpad_finger_1_x", Float.NaN));
        double y = normalize(readFloat(gamepad, "touchpad_finger_1_y", Float.NaN));

        if (Double.isNaN(x) || Double.isNaN(y)) {
            return TouchpadAction.MENU_TOGGLE;
        }

        if (x >= 0.75) {
            return TouchpadAction.SELECT;
        }
        if (x <= 0.25) {
            return TouchpadAction.BACK;
        }
        if (y <= 0.25) {
            return TouchpadAction.UP;
        }
        if (y >= 0.75) {
            return TouchpadAction.DOWN;
        }

        return TouchpadAction.MENU_TOGGLE;
    }

    private static boolean readBoolean(Gamepad gamepad, String fieldName, boolean fallback) {
        try {
            Field field = gamepad.getClass().getField(fieldName);
            return field.getBoolean(gamepad);
        } catch (ReflectiveOperationException ignored) {
            return fallback;
        }
    }

    private static float readFloat(Gamepad gamepad, String fieldName, float fallback) {
        try {
            Field field = gamepad.getClass().getField(fieldName);
            return field.getFloat(gamepad);
        } catch (ReflectiveOperationException ignored) {
            return fallback;
        }
    }

    private static double normalize(double raw) {
        if (Double.isNaN(raw)) {
            return Double.NaN;
        }

        if (raw >= 0.0 && raw <= 1.0) {
            return raw;
        }

        if (raw >= -1.0 && raw <= 1.0) {
            return (raw + 1.0) / 2.0;
        }

        if (raw > 1.0 && raw <= 255.0) {
            return raw / 255.0;
        }

        return Double.NaN;
    }
}

