package controllers;

import models.Point;
import rasters.RasterBuffer;
import utilities.HelperFunctions;

public class HandController {
    private static HandController instance;

    private HandController() {}

    public static HandController getInstance() {
        if (instance == null) {
            instance = new HandController();
        }
        return instance;
    }

    // -----------------------------------------

    private Point startPoint = null; // Stores the position of object from last "tick" to get relative change when moving
    private String closestObject = ""; // Keeps track of the moved object

    public void clearPoint() {
        startPoint = null;
        closestObject = "";
    }

    public void pick(int pickX, int pickY) {
        closestObject = HelperFunctions.getInstance().getClosestObject(pickX, pickY, false); // Gets the closest object to the cursor (within range)
        if (closestObject.length() < 2) { // If the closest object is invalid
            clearPoint(); // Clear the starting point to prevent dragging some object that is out of range
            return;
        }

        startPoint = new Point(pickX, pickY); // Otherwise set the start point as the current cursor position
    }

    public void move(int moveX, int moveY) {
        if (startPoint != null && closestObject.length() >= 2) { // If there is a valid object being moved AND a valid startPoint
            int xDiff = moveX - startPoint.getX(); // Gets the change of X axis relative to the last "tick"
            int yDiff = moveY - startPoint.getY(); // Gets the change of Y axis relative to the last "tick"
            startPoint = new Point(startPoint.getX() + xDiff, startPoint.getY() + yDiff); // "Reset" the point, so the difference is relative to the last change

            int objectId = Integer.parseInt(closestObject.substring(1)); // Extract the moved object id (object = "C15" -> objectId = 15)

            switch (RasterBuffer.getInstance().getActionTypeFromBufferId(closestObject)) { // Based on object type, call given object's move function
                case Line:
                    LineController.getInstance().changePointsOf(objectId, xDiff, yDiff);
                    break;
                case Polygon:
                    PolygonController.getInstance().changePointsOf(objectId, xDiff, yDiff);
                    break;
                case Rectangle:
                    RectangleController.getInstance().changePointsOf(objectId, xDiff, yDiff);
                    break;
                case Circle:
                    CircleController.getInstance().changePointsOf(objectId, xDiff, yDiff);
                    break;
            }
        }
    }
}
