package controllers;

import stores.EnumStore;
import models.Point;
import models.Line;

import models.canvases.LineCanvas;
import stores.StateStore;
import utilities.Frame;
import utilities.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LineController {
    private static LineController instance;

    private LineController() {}

    public static LineController getInstance() {
        if (instance == null) {
            instance = new LineController();
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

    public void createLine(int x, int y) {
        if (point != null) {
            Point point2 = new Point(x, y);
            Line line = new Line(point, point2, StateStore.getInstance().lineColor, EnumStore.getInstance().lineType, EnumStore.getInstance().alignment, StateStore.getInstance().thickness);
            clearPoint();

            LineCanvas.getInstance().addLine(line); // Add currently drawn line to the canvas
            Renderer.getInstance().rerender();
        }
    }

    // ------------------------------------------
    // MoveLine
       int tolerance = 30;
    // ------------------------------------------

    public void MoveLineClick(int x, int y) {
        point = null; // Nullify point to prevent issues
        
        int closestLineIndex = -1;
        Point otherPoint = null; // Second point of the closest line
        float closestDistance = Float.MAX_VALUE;

        int index = 0;
        for (Line line : LineCanvas.getInstance().getLines()) {
            float distanceP1 = getDistance(line.getPoint1(), x, y); // Calculates distance from first point of the line
            float distanceP2 = getDistance(line.getPoint2(), x, y); // Calculates distance from the second point of the line

            if (distanceP1 < closestDistance) {
                otherPoint = line.getPoint2();
                closestDistance = distanceP1;
                closestLineIndex = index;
            }

            if (distanceP2 < closestDistance) {
                otherPoint = line.getPoint1();
                closestDistance = distanceP2;
                closestLineIndex = index;
            }

            index++;
        }

        if (closestDistance <= tolerance) { // If the closest point is within tolerance
            point = new Point(otherPoint.getX(), otherPoint.getY());
            LineCanvas.getInstance().removeLineAt(closestLineIndex);
        }
    }

    public void MoveLineDrag(int x, int y) {
        if (point != null) {
            Point point2 = new Point(x, y);
            Line line = new Line(point, point2, StateStore.getInstance().lineColor, EnumStore.getInstance().lineType, EnumStore.getInstance().alignment, StateStore.getInstance().thickness);

            Renderer.getInstance().rerender();
            Renderer.getInstance().renderLines(new ArrayList<>(List.of(line))); // Render currently drawn line
            Frame.getInstance().getPanel().repaint(); // Update the canvas
        }
    }

    private float getDistance(Point p, int x, int y) {
        int px = p.getX();
        int py = p.getY();

        int xDiff = Math.abs(px - x);
        int yDiff = Math.abs(py - y);

        return (float) Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
    }
}
