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

    private Point startPoint = null;
    private String closestObject = "";

    public void clearPoint() {
        startPoint = null;
        closestObject = "";
    }

    public void pick(int pickX, int pickY) {
        closestObject = HelperFunctions.getInstance().getClosestObject(pickX, pickY, false);
        if (closestObject.length() < 2) {
            clearPoint();
            return;
        }

        startPoint = new Point(pickX, pickY);
    }

    public void move(int moveX, int moveY) {
        if (startPoint != null && closestObject.length() >= 2) {
            int xDiff = moveX - startPoint.getX();
            int yDiff = moveY - startPoint.getY();
            startPoint = new Point(startPoint.getX() + xDiff, startPoint.getY() + yDiff); // "Reset" the point, so the difference is relative to the last change

            int objectId = Integer.parseInt(closestObject.substring(1));

            switch (RasterBuffer.getInstance().getActionTypeFromBufferId(closestObject)) {
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
