package models.canvases;

import models.Point;
import models.Rectangle;

import java.util.ArrayList;

public class RectangleCanvas {
    private static RectangleCanvas instance;

    private ArrayList<Rectangle> rectangles;

    private RectangleCanvas() { this.rectangles = new ArrayList<>(); }

    public static RectangleCanvas getInstance() {
        if (instance == null) {
            instance = new RectangleCanvas();
        }
        return instance;
    }

    public void addRectangle(Rectangle rectangle) { this.rectangles.add(rectangle); }
    public void clearRectangles() { this.rectangles.clear(); }

    public Rectangle getRectangleById(int id) {
        for (Rectangle rectangle : rectangles) { // Iterate through every rectangle
            if (rectangle.getId() == id) { // Look for matching id
                return rectangle; // If matching id is found, return it
            }
        }
        return null; // Otherwise, if there is no matching id found, return null
    }

    public void disableRectangleById(int id) {
        for (Rectangle rectangle : rectangles) { // Iterate through every rectangle
            if (rectangle.getId() == id) { // Look for matching id
                rectangle.disable(); // If matching id is found, disable given rectangle
                return; // Return, because id should be unique
            }
        }
    }

    public void editRectangleById(Point firstPoint, Point secondPoint, int id) {
        Rectangle rectangleToEdit = getRectangleById(id); // Get old rectangle
        rectangles.remove(rectangleToEdit); // Remove old rectangle

        rectangleToEdit.createFromTwoPoints(firstPoint, secondPoint); // Edit old rectangle
        rectangles.add(rectangleToEdit); // Add new rectangle
    }
}
