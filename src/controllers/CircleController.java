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

    public void createCircle(int x, int y) {
        if (point == null) {
            createPoint(x, y);
            Circle circle = new Circle(point, point, currentId);

            CircleCanvas.getInstance().addCircle(circle); // Add currently drawn circle to the canvas
        } else {
            CircleCanvas.getInstance().getCircleById(currentId).updateProperties();
            point = new Point(x, y);

            Circle oldCircle = CircleCanvas.getInstance().getCircleById(currentId);
            Renderer.getInstance().renderCircle(oldCircle, true); // Remove

            CircleCanvas.getInstance().editCircleById(point, currentId);
            Circle newCircle = CircleCanvas.getInstance().getCircleById(currentId);
            Renderer.getInstance().renderCircle(newCircle, false); // Add

            Renderer.getInstance().rerender();
        }
    }
}
