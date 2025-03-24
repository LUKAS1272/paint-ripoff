package stores;

import java.awt.*;

public class StateStore {
    private static StateStore instance;

    public static StateStore getInstance() {
        if (instance == null) {
            instance = new StateStore();
        }
        return instance;
    }

    private StateStore() {}

    // --------------------------------------
    private int minThickness = 2;
    private int maxThickness = 10;
    // --------------------------------------

    public Color lineColor = Color.white;
    public Color fillColor = Color.green;

    private int thickness = minThickness;

    public int getThickness() { return thickness; }

    public void increaseThickness() {
        thickness++;

        if (thickness > maxThickness) {
            thickness = maxThickness;
        }
    }

    public void decreaseThickness() {
        thickness--;

        if (thickness < minThickness) {
            thickness = minThickness;
        }
    }
}
