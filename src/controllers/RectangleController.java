package controllers;

import enums.ActionType;
import models.Point;
import models.Rectangle;
import models.canvases.RectangleCanvas;
import rasters.RasterBuffer;
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

    public void clearPoint() {
        point = null;
    }

    public void createRectangle(int x, int y) {
        if (point == null) {
            createPoint(x, y); // Create a point for new rectangle
            Rectangle rectangle = new Rectangle(point, point, currentId);

            RectangleCanvas.getInstance().addRectangle(rectangle); // Add currently drawn rectangle to the canvas
        } else {
            RectangleCanvas.getInstance().getRectangleById(currentId).updateProperties();
            point = new Point(x, y);

            Rectangle oldRectangle = RectangleCanvas.getInstance().getRectangleById(currentId);
            Renderer.getInstance().renderLines(oldRectangle.getLines(), true, RasterBuffer.getInstance().buildBufferId(ActionType.Rectangle, currentId)); // Remove

            RectangleCanvas.getInstance().editRectangleById(point, currentId);
            Rectangle newRectangle = RectangleCanvas.getInstance().getRectangleById(currentId);
            Renderer.getInstance().renderLines(newRectangle.getLines(), false, RasterBuffer.getInstance().buildBufferId(ActionType.Rectangle, currentId)); // Add

            Renderer.getInstance().rerender();
        }
    }
}
