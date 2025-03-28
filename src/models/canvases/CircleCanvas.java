package models.canvases;

import models.Circle;
import models.Point;
import utilities.Renderer;

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

    public void addCircle(Circle circle) {
        this.circles.add(circle);
    }

    public void editLastCircle(Point secondPoint) {
        this.circles.getLast().editCircle(secondPoint);
        Renderer.getInstance().rerender();
    }

    public void clearCircles() { this.circles.clear(); }

    public ArrayList<Circle> getCircles() {
        return circles;
    }
}
