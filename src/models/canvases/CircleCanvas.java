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

    public void addCircle(Circle circle) {
        this.circles.add(circle);
    }

    public void editCircleById(Point newPoint, int id) {
        Circle circleToEdit = getCircleById(id);
        circles.remove(circleToEdit);
        circleToEdit.editCircle(newPoint);
        circles.add(circleToEdit);
    }

    public Circle getCircleById(int id) {
        for (Circle circle : circles) {
            if (circle.getId() == id) {
                return circle;
            }
        }
        return null;
    }

    public void clearCircles() { this.circles.clear(); }

    public ArrayList<Circle> getCircles() {
        return circles;
    }
}
