package controllers;

import models.Circle;
import models.Point;
import models.canvases.CircleCanvas;
import utilities.Renderer;

public class CircleController {
    private static CircleController instance;

    private CircleController() {}

    public static CircleController getInstance() {
        if (instance == null) {
            instance = new CircleController();
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

    public void createCircle(int x, int y) {
        if (point != null) {
            Point point2 = new Point(x, y);
            Circle circle = new Circle(point, point2);
            clearPoint();

            CircleCanvas.getInstance().addCircle(circle); // Add currently drawn line to the canvas
            Renderer.getInstance().rerender();
        }
    }
}
