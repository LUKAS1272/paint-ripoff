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

    public void edit(int pointX, int pointY) { // Called on mousePressed - controller only registers, which object will be edited
        closestObject = HelperFunctions.getInstance().getClosestObject(pointX, pointY, true); // Get the closest object to the cursor

        switch (RasterBuffer.getInstance().getActionTypeFromBufferId(closestObject)) { // Based on closest object type, call given object's edit function
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

    public void editAction(int x, int y) { // Called on mouseDragged - there are some changes actually being made to the canvas
        if (closestObject.length() < 2) { return; } // Return if the closestObject is invalid (got to be at least an identifier letter and 1-digit id number)

        switch (RasterBuffer.getInstance().getActionTypeFromBufferId(closestObject)) { // Based on closest object type, call given object's edit function
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
