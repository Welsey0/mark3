package org.firstinspires.ftc.teamcode.framework.input;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Typed, stateful input bindings. Bindings belong to an opmode so each
 * opmode can choose its own controls without changing Constants or subsystems.
 */
public final class GamepadInput {
    private GamepadInput() {
    }

    public enum Button {
        A, B, X, Y, LEFT_BUMPER, RIGHT_BUMPER,
        BACK, START, LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON
    }

    public enum Trigger {
        LEFT, RIGHT
    }

    public enum StickAxis {
        LEFT_X, LEFT_Y, RIGHT_X, RIGHT_Y
    }

    public enum ActionMode {
        HOLD, TOGGLE
    }

    public static AnalogBinding axis(StickAxis axis) {
        return new AnalogBinding(axis);
    }

    public static AnalogBinding trigger(Trigger trigger) {
        return new AnalogBinding(trigger);
    }

    public static ActionBinding hold(Button button) {
        return new ActionBinding(new AnalogBinding(button), ActionMode.HOLD, 0.5);
    }

    public static ActionBinding toggle(Button button) {
        return new ActionBinding(new AnalogBinding(button), ActionMode.TOGGLE, 0.5);
    }

    public static ActionBinding hold(Trigger trigger, double threshold) {
        return new ActionBinding(new AnalogBinding(trigger), ActionMode.HOLD, threshold);
    }

    public static ActionBinding toggle(Trigger trigger, double threshold) {
        return new ActionBinding(new AnalogBinding(trigger), ActionMode.TOGGLE, threshold);
    }

    public static final class AnalogBinding {
        private final StickAxis axis;
        private final Trigger trigger;
        private final Button button;

        private AnalogBinding(StickAxis axis) {
            this.axis = axis;
            trigger = null;
            button = null;
        }

        private AnalogBinding(Trigger trigger) {
            axis = null;
            this.trigger = trigger;
            button = null;
        }

        private AnalogBinding(Button button) {
            axis = null;
            trigger = null;
            this.button = button;
        }

        public double value(Gamepad gamepad) {
            if (gamepad == null) {
                throw new IllegalArgumentException("gamepad cannot be null");
            }
            if (axis != null) {
                switch (axis) {
                    case LEFT_X: return gamepad.left_stick_x;
                    case LEFT_Y: return gamepad.left_stick_y;
                    case RIGHT_X: return gamepad.right_stick_x;
                    case RIGHT_Y: return gamepad.right_stick_y;
                    default: throw new IllegalStateException("Unsupported stick axis");
                }
            }
            if (trigger != null) {
                return trigger == Trigger.LEFT ? gamepad.left_trigger : gamepad.right_trigger;
            }
            return buttonValue(gamepad, button) ? 1.0 : 0.0;
        }

        private boolean buttonValue(Gamepad gamepad, Button button) {
            switch (button) {
                case A: return gamepad.a;
                case B: return gamepad.b;
                case X: return gamepad.x;
                case Y: return gamepad.y;
                case LEFT_BUMPER: return gamepad.left_bumper;
                case RIGHT_BUMPER: return gamepad.right_bumper;
                case BACK: return gamepad.back;
                case START: return gamepad.start;
                case LEFT_STICK_BUTTON: return gamepad.left_stick_button;
                case RIGHT_STICK_BUTTON: return gamepad.right_stick_button;
                default: throw new IllegalStateException("Unsupported button");
            }
        }
    }

    public static final class ActionBinding {
        private final AnalogBinding source;
        private final ActionMode mode;
        private final double threshold;
        private boolean previousActive;
        private boolean toggled;

        private ActionBinding(AnalogBinding source, ActionMode mode, double threshold) {
            if (threshold < 0.0 || threshold > 1.0) {
                throw new IllegalArgumentException("threshold must be in [0, 1]");
            }
            this.source = source;
            this.mode = mode;
            this.threshold = threshold;
        }

        /**
         * Returns the current action state. Call once per loop.
         */
        public boolean active(Gamepad gamepad) {
            boolean currentActive = source.value(gamepad) >= threshold;
            if (mode == ActionMode.HOLD) {
                previousActive = currentActive;
                return currentActive;
            }
            if (currentActive && !previousActive) {
                toggled = !toggled;
            }
            previousActive = currentActive;
            return toggled;
        }

        public void reset() {
            previousActive = false;
            toggled = false;
        }
    }
}
