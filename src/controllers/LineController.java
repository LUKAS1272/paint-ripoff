package controllers;

import rasterizers.TrivialLineRasterizer;
import stores.EnumStore;
import models.Point;
import models.Line;

import models.canvases.LineCanvas;
import stores.StateStore;
import utilities.Frame;
import utilities.Renderer;

public class LineController {
    private static LineController instance;

    private LineController() {}

    public static LineController getInstance() {
        if (instance == null) {
            instance = new LineController();
        }
        return instance;
    }

    private int id = 0;
    private int currentId = 0;

    // -----------------------------------------

    private Point point;

    public void createNewLine(int x, int y) {
        point = new Point(x, y);
        id += 1; // Iterate id of the newly created line
        currentId = id; // Update currently used id to the new line id
        createLine(x, y); // Create a line object
    }

    public void clearPoint() {
        point = null;
    }

    public void createLine(int x, int y) {
        if (point != null) {
            Point point2 = new Point(x, y);
            Line line = new Line(point, point2, EnumStore.getInstance().getDrawColor(), EnumStore.getInstance().lineType, EnumStore.getInstance().alignment, StateStore.getInstance().getThickness(), currentId);

            LineCanvas.getInstance().addLine(line); // Add currently drawn line to the canvas
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
            currentId = LineCanvas.getInstance().getLines().get(closestLineIndex).getId();
        }
    }

    public void MoveLineDrag(int x, int y) {
        if (point != null) {
            Point point2 = new Point(x, y);

            Renderer.getInstance().renderLine(LineCanvas.getInstance().getLineById(currentId), true, null); // Remove current line from buffer

            Line line = new Line(point, point2, EnumStore.getInstance().getDrawColor(), EnumStore.getInstance().lineType, EnumStore.getInstance().alignment, StateStore.getInstance().getThickness(), currentId);
            LineCanvas.getInstance().editLineById(currentId, line);

            Renderer.getInstance().renderLine(LineCanvas.getInstance().getLineById(currentId), false, null); // Add current line to buffer

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
