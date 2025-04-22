package models.canvases;

import models.Circle;
import models.Point;

import java.util.ArrayList;

public class CircleCanvas {
    private static CircleCanvas instance;

    private ArrayList<Circle> circles;

    private CircleCanvas() { this.circles = new ArrayList<>(); }

    public static CircleCanvas getInstance() {
        if (instance == null) {
            instance = new CircleCanvas();
        }
        return instance;
    }

    public void addCircle(Circle circle) { this.circles.add(circle); }
    public void clearCircles() { this.circles.clear(); }

    public void editCircleById(Point firstPoint, Point secondPoint, int id) {
        Circle circleToEdit = getCircleById(id); // Get old circle
        circles.remove(circleToEdit); // Remove old circle

        circleToEdit.createCircle(firstPoint, secondPoint); // Edit old circle
        circles.add(circleToEdit); // Add new circle
    }

    public Circle getCircleById(int id) {
        for (Circle circle : circles) { // Iterate through every circle
            if (circle.getId() == id) { // Look for matching id
                return circle; // If matching id is found, return it
            }
        }
        return null; // Otherwise, if there is no matching id found, return null
    }

    public void disableCircleById(int id) {
        for (Circle circle : circles) { // Iterate through every circle
            if (circle.getId() == id) { // Look for matching id
                circle.disable(); // If matching id is found, disable given circle
                return; // Return, because id should be unique
            }
        }
    }
}
