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

    public Color lineColor = Color.white;
    public Color fillColor = Color.green;
    public int thickness = 1;
}
