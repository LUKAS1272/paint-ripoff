package controllers;

import models.Circle;
import models.Point;
import models.canvases.CircleCanvas;
import utilities.HelperFunctions;
import utilities.Renderer;

import java.util.ArrayList;

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
            Point point2 = new Point(x, y);

            Circle oldCircle = CircleCanvas.getInstance().getCircleById(currentId);
            Renderer.getInstance().renderCircle(oldCircle, true); // Remove

            CircleCanvas.getInstance().editCircleById(point, point2, currentId);
            Circle newCircle = CircleCanvas.getInstance().getCircleById(currentId);
            Renderer.getInstance().renderCircle(newCircle, false); // Add

            Renderer.getInstance().rerender();
        }
    }

    public void edit(String object, int editX, int editY) {
        currentId = Integer.parseInt(object.substring(1));

        Circle editedCircle = CircleCanvas.getInstance().getCircleById(currentId);

        Point center = editedCircle.getCenter();
        int radius = editedCircle.getRadius();

        ArrayList<Point> circlePoints = new ArrayList<>();
        circlePoints.add(new Point(center.getX() + radius, center.getY() + radius));
        circlePoints.add(new Point(center.getX() + radius, center.getY() - radius));
        circlePoints.add(new Point(center.getX() - radius, center.getY() + radius));
        circlePoints.add(new Point(center.getX() - radius, center.getY() - radius));

        Point furthestPoint = null;
        float furthestPointDistance = 0;

        for (Point circlePoint : circlePoints) {
            if (HelperFunctions.getInstance().getDistance(circlePoint, editX, editY) > furthestPointDistance) {
                furthestPoint = circlePoint;
                furthestPointDistance = HelperFunctions.getInstance().getDistance(circlePoint, editX, editY);
            }
        }
        point = furthestPoint;
    }

    public void changePointsOf(int id, int xDiff, int yDiff) {
        Circle oldCircle = CircleCanvas.getInstance().getCircleById(id);
        Renderer.getInstance().renderCircle(oldCircle, true); // Remove

        oldCircle.alterCenter(xDiff, yDiff);
        Renderer.getInstance().renderCircle(oldCircle, false); // Add
        Renderer.getInstance().rerender();
    }
}
