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

    public void addRectangle(Rectangle rectangle) {
        this.rectangles.add(rectangle);
    }

    public void clearRectangles() { this.rectangles.clear(); }

    public Rectangle getRectangleById(int id) {
        for (Rectangle rectangle : rectangles) {
            if (rectangle.getId() == id) {
                return rectangle;
            }
        }
        return null;
    }

    public void disableRectangleById(int id) {
        for (Rectangle rectangle : rectangles) {
            if (rectangle.getId() == id) {
                rectangle.disable();
                return;
            }
        }
    }

    public void editRectangleById(Point firstPoint, Point secondPoint, int id) {
        Rectangle rectangleToEdit = getRectangleById(id);
        rectangles.remove(rectangleToEdit);
        rectangleToEdit.createFromTwoPoints(firstPoint, secondPoint);
        rectangles.add(rectangleToEdit);
    }
}
