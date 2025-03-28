package controllers;

import models.Line;
import models.Point;
import models.Rectangle;
import models.canvases.LineCanvas;
import models.canvases.RectangleCanvas;
import stores.EnumStore;
import stores.StateStore;
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

    public void createPoint(int x, int y) {
        point = new Point(x, y);
    }

    public void clearPoint() {
        point = null;
    }

    public void createRectangle(int x, int y) {
        if (point != null) {
            Point point2 = new Point(x, y);
            Rectangle rectangle = new Rectangle(point, point2);
            clearPoint();

            RectangleCanvas.getInstance().addRectangle(rectangle); // Add currently drawn line to the canvas
            Renderer.getInstance().rerender();
        }
    }
}
