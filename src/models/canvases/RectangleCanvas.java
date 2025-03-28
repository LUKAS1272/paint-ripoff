package models.canvases;

import models.Point;
import models.Polygon;
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

    public void editLastRectangle(Point secondPoint) { this.rectangles.getLast().createFromTwoPoints(secondPoint); }

    public void clearRectangles() { this.rectangles.clear(); }

    public Rectangle getRectanglesAt(int index) { return this.rectangles.get(index); }

    public ArrayList<Rectangle> getRectangles() {
        return rectangles;
    }
}
