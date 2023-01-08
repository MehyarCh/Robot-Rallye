package Desperatedrosseln.Logic;

public enum DIRECTION {
    TOP(90),
    BOTTOM(270),
    LEFT(180),
    RIGHT(0);

    int angle;
    DIRECTION(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public static DIRECTION valueOfDirection(int angle) {
        for (DIRECTION e : values()) {
            if (e.angle == angle) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        switch (this){
            case TOP -> {
                return "top";
            }
            case BOTTOM -> {
                return"bottom";
            }
            case LEFT -> {
                return "left";

            }
            case RIGHT -> {
                return "right";
            }
            default -> {
                return"unknown Direction";
            }
        }

    }
}

