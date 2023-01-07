package Desperatedrosseln.Logic;

public enum DIRECTION {
    UP(90),
    DOWN(270),
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
}

