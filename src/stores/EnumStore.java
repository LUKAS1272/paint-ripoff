package stores;

import controllers.PolygonController;
import enums.*;
import enums.storeEnums.Enum;

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

    public void moveEnum(Enum enumType) {
        switch (enumType) {
            case Alignment:
                Alignment[] allAlignments = Alignment.values(); // Gets all enum object
                alignment = allAlignments[(alignment.ordinal() + 1) % allAlignments.length]; // Assigns next enum object
                break;
            case LineType:
                LineType[] allLineTypes = LineType.values(); // Gets all enum object
                lineType = allLineTypes[(lineType.ordinal() + 1) % allLineTypes.length]; // Assigns next enum object
                break;
            case ActionType:
                ActionType[] allActionTypes = ActionType.values(); // Gets all enum object
                actionType = allActionTypes[(actionType.ordinal() + 1) % allActionTypes.length]; // Assigns next enum object

                PolygonController.getInstance().clearPoint();
                break;
            case DrawColor:
                DrawColor[] allDrawColors = DrawColor.values(); // Gets all enum object
                drawColor = allDrawColors[(drawColor.ordinal() + 1) % allDrawColors.length]; // Assigns next enum object
                break;
        }
    }

    public Color getDrawColor() {
        return switch (drawColor) {
            case White -> Color.white;
            case Red -> Color.red;
            case Green -> Color.green;
            case Blue -> Color.blue;
            case Yellow -> Color.yellow;
            case Magenta -> Color.magenta;
            case Cyan -> Color.cyan;
            default -> null;
        };
    }
}
