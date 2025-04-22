package stores;

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

    private int thickness = minThickness; // Set the starting thickness as the minThickness

    public int getThickness() { return thickness; }

    public void increaseThickness() { // Increase thickness by 1
        thickness++;

        if (thickness > maxThickness) { // If it is above maxThickness, set it as maxThickness
            thickness = maxThickness;
        }
    }

    public void decreaseThickness() { // Decrease thickness by 1
        thickness--;

        if (thickness < minThickness) { // If it is below minThickness set it as minThickness
            thickness = minThickness;
        }
    }
}
