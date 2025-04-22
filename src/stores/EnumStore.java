package stores;

import enums.*;

import java.awt.*;

public class EnumStore {
    private static EnumStore instance;

    public Alignment alignment = Alignment.values()[0];
    public LineType lineType = LineType.values()[0];
    public ActionType actionType = ActionType.values()[0];
    public DrawColor drawColor = DrawColor.values()[0];

    private EnumStore() {}

    public static EnumStore getInstance() {
        if (instance == null) {
            instance = new EnumStore();
        }
        return instance;
    }

    public Color getDrawColor() { // Gets the color value of current drawColor
        return getColor(drawColor);
    }

    private Color getColor(DrawColor drawColor) { // Get the color value of given drawColor
        return switch (drawColor) {
            case White -> Color.white;
            case Red -> Color.red;
            case Green -> Color.green;
            case Blue -> Color.blue;
            case Yellow -> Color.yellow;
            case Magenta -> Color.magenta;
            case Cyan -> Color.cyan;
        };
    }
}
