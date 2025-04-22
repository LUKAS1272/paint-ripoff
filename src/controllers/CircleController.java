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

    private Point point; // The starting corner point of the circle

    private int id = 0; // Last circle id
    private int currentId = 0; // Currently edited circle id

    public void createPoint(int x, int y) { // Creating a starting point is perceived as creating a new circle
        point = new Point(x, y); // Set the starting point
        id += 1; // Iterate last circle id
        currentId = id; // Set the last circle id as the current circle
    }

    public void clearPoint() { // Clear the circle starting point
        point = null;
    }

    public void createCircle(int x, int y) {
        if (point == null) { // If the point is null - the circle is being created
            createPoint(x, y); // Create a new circle point
            Circle circle = new Circle(point, point, currentId); // Create the circle object with both points set as the current cursor position

            CircleCanvas.getInstance().addCircle(circle); // Add currently drawn circle to the canvas
        } else { // If the point is not null - the circle is being edited
            CircleCanvas.getInstance().getCircleById(currentId).updateProperties(); // Update circle properties (thickness, color)
            Point point2 = new Point(x, y);

            Circle oldCircle = CircleCanvas.getInstance().getCircleById(currentId); // Get the old circle
            Renderer.getInstance().renderCircle(oldCircle, true); // Remove the old circle from buffer

            CircleCanvas.getInstance().editCircleById(point, point2, currentId); // Update the second corner point of the circle
            Circle newCircle = CircleCanvas.getInstance().getCircleById(currentId); // Get the new circle
            Renderer.getInstance().renderCircle(newCircle, false); // Add the new circle to buffer

            Renderer.getInstance().rerender(); // Rerender the canvas
        }
    }

    public void edit(String object, int editX, int editY) {
        currentId = Integer.parseInt(object.substring(1)); // Extract the edited circle id (object = "C15" -> currentId = 15)

        Circle editedCircle = CircleCanvas.getInstance().getCircleById(currentId); // Get the circle that is edited

        Point center = editedCircle.getCenter(); // Get the center of the current circle
        int radius = editedCircle.getRadius(); // Get the radius of the current circle

        ArrayList<Point> circlePoints = new ArrayList<>();
        circlePoints.add(new Point(center.getX() + radius, center.getY() + radius)); // Bottom right corner
        circlePoints.add(new Point(center.getX() + radius, center.getY() - radius)); // Up right corner
        circlePoints.add(new Point(center.getX() - radius, center.getY() + radius)); // Bottom left corner
        circlePoints.add(new Point(center.getX() - radius, center.getY() - radius)); // Up left corner

        // Get the furthest corner from the cursor
        Point furthestPoint = null;
        float furthestPointDistance = 0;

        for (Point circlePoint : circlePoints) {
            if (HelperFunctions.getInstance().getDistance(circlePoint, editX, editY) > furthestPointDistance) {
                furthestPoint = circlePoint;
                furthestPointDistance = HelperFunctions.getInstance().getDistance(circlePoint, editX, editY);
            }
        }
        point = furthestPoint; // Set the furthest corner as the first point of the circle
    }

    public void changePointsOf(int id, int xDiff, int yDiff) {
        Circle oldCircle = CircleCanvas.getInstance().getCircleById(id); // Get the old circle
        Renderer.getInstance().renderCircle(oldCircle, true); // Remove the old circle from buffer

        oldCircle.alterCenter(xDiff, yDiff); // Move the center of the circle - meaning all its pixels are moved
        Renderer.getInstance().renderCircle(oldCircle, false); // Add the new circle to buffer
        Renderer.getInstance().rerender(); // Rerender the canvas
    }
}
