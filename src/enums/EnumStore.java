package enums;

public class EnumStore {
    private static EnumStore instance;

    private static Alignment alignment = Alignment.values()[0];
    private static LineType lineType = LineType.values()[0];
    private static ObjectType objectType = ObjectType.values()[0];

    private EnumStore() {}

    public static EnumStore getInstance() {
        if (instance == null) {
            instance = new EnumStore();
        }
        return instance;
    }

    // ------------------------------------- \\
    // Getters                               \\
    // ------------------------------------- \\

    public Alignment getAlignment() { return alignment; }
    public LineType getLineType() { return lineType; }
    public ObjectType getObjectType() { return objectType; }

    public void moveEnum(Enums enumType) {
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
                ObjectType[] allObjectTypes = ObjectType.values(); // Gets all enum object
                objectType = allObjectTypes[(objectType.ordinal() + 1) % allObjectTypes.length]; // Assigns next enum object
                break;
        }
    }
}
