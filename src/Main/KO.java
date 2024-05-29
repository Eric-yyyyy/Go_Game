package Main;

import java.awt.*;

public class KO {
    private Point lastCapturedSingleStone;

    public KO() {
        this.lastCapturedSingleStone = null;
    }

    public void reset() {
        lastCapturedSingleStone = null;
    }

    public void setLastCapturedSingleStone(Point p) {
        this.lastCapturedSingleStone = p;
    }

    public boolean isKoViolation(Point p) {
        return p.equals(lastCapturedSingleStone);
    }
}
