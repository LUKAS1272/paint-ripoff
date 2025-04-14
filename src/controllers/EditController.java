package controllers;

import rasters.RasterBuffer;
import utilities.HelperFunctions;

public class EditController {
    private static EditController instance;

    private EditController() {}

    public static EditController getInstance() {
        if (instance == null) {
            instance = new EditController();
        }
        return instance;
    }

    // -----------------------------------------

    public void clear() {
        closestObject = "";

        LineController.getInstance().clearPoint();
        PolygonController.getInstance().clearPoint();
        RectangleController.getInstance().clearPoint();
        CircleController.getInstance().clearPoint();
    }

    private String closestObject = "";

    public void edit(int pointX, int pointY) {
        closestObject = HelperFunctions.getInstance().getClosestObject(pointX, pointY, true);

        switch (RasterBuffer.getInstance().getActionTypeFromBufferId(closestObject)) {
            case Line:
                LineController.getInstance().edit(closestObject);
                break;
            case Polygon:
                PolygonController.getInstance().edit(closestObject);
                break;
            case Rectangle:
                RectangleController.getInstance().edit(closestObject, pointX, pointY);
                break;
            case Circle:
                CircleController.getInstance().edit(closestObject, pointX, pointY);
                break;
        }
    }

    public void editAction(int x, int y) {
        if (closestObject.length() < 2) { return; }

        switch (RasterBuffer.getInstance().getActionTypeFromBufferId(closestObject)) {
            case Line:
                LineController.getInstance().MoveLineDrag(x, y);
                break;
            case Polygon:
                PolygonController.getInstance().updatePolygon(x, y);
                break;
            case Rectangle:
                RectangleController.getInstance().createRectangle(x, y);
                break;
            case Circle:
                CircleController.getInstance().createCircle(x, y);
                break;
        }
    }
}
