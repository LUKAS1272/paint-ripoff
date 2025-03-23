package stores;

import controllers.LineController;
import controllers.PolygonController;
import enums.*;
import enums.storeEnums.Enum;

public class EnumStore {
    private static EnumStore instance;

    public Alignment alignment = Alignment.values()[0];
    public LineType lineType = LineType.values()[0];
    public ActionType actionType = ActionType.values()[0];

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
        }
    }
}
