package controllers;

import enums.ActionType;
import models.Point;
import models.Rectangle;
import models.canvases.RectangleCanvas;
import rasters.RasterBuffer;
import utilities.HelperFunctions;
import utilities.Renderer;

public class RectangleController {
    private static RectangleController instance;

    private RectangleController() {}

    public static RectangleController getInstance() {
        if (instance == null) {
            instance = new RectangleController();
        }
        return instance;
    }

    // -----------------------------------------

    private Point point;

    private int id = 0;
    private int currentId = 0;

    public void createPoint(int x, int y) {
        point = new Point(x, y);
        id += 1;
        currentId = id;
    }

    public void clearPoint() { point = null; }

    public void createRectangle(int x, int y) {
        if (point == null) {
            createPoint(x, y); // Create a point for new rectangle
            Rectangle rectangle = new Rectangle(point, point, currentId);

            RectangleCanvas.getInstance().addRectangle(rectangle); // Add currently drawn rectangle to the canvas
        } else {
            RectangleCanvas.getInstance().getRectangleById(currentId).updateProperties(); // Update rectangle's properties (color, line type...)

            Rectangle oldRectangle = RectangleCanvas.getInstance().getRectangleById(currentId); // Get old rectagnle
            Renderer.getInstance().renderLines(oldRectangle.getLines(), true, RasterBuffer.getInstance().buildBufferId(ActionType.Rectangle, currentId)); // Remove old rectangle

            Point point2 = new Point(x, y); // Create a second corner point on click cords
            RectangleCanvas.getInstance().editRectangleById(point, point2, currentId); // Change rectangle's corner points
            Rectangle newRectangle = RectangleCanvas.getInstance().getRectangleById(currentId); // Get new rectangle
            Renderer.getInstance().renderLines(newRectangle.getLines(), false, RasterBuffer.getInstance().buildBufferId(ActionType.Rectangle, currentId)); // Add new rectangle

            Renderer.getInstance().rerender();
        }
    }

    public void edit(String object, int editX, int editY) {
        currentId = Integer.parseInt(object.substring(1)); // Get object id from object identifier

        Rectangle editedRectangle = RectangleCanvas.getInstance().getRectangleById(currentId); // Get edited rectangle

        // Find the furthest corner point from clicked pixel
        Point furthestPoint = null;
        float furthestPointDistance = 0;
        for (Point rectanglePoint : editedRectangle.getPoints()) {
            if (HelperFunctions.getInstance().getDistance(rectanglePoint, editX, editY) > furthestPointDistance) {
                furthestPoint = rectanglePoint;
                furthestPointDistance = HelperFunctions.getInstance().getDistance(rectanglePoint, editX, editY);
            }
        }
        point = furthestPoint; // Set the furthest point as the starting point for a rectangle
    }

    public void changePointsOf(int id, int xDiff, int yDiff) {
        Rectangle oldRectangle = RectangleCanvas.getInstance().getRectangleById(id); // Get the old rectangle
        Renderer.getInstance().renderLines(oldRectangle.getLines(), true, "R" + id); // Unrender the old rectangle

        oldRectangle.alterPoints(xDiff, yDiff); // Move the old rectangle
        Renderer.getInstance().renderLines(oldRectangle.getLines(), false, "R" + id); // Render the new rectangle
    }
}
