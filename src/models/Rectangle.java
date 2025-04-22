package models;

import enums.Alignment;
import enums.LineType;
import stores.EnumStore;
import stores.StateStore;
import utilities.Frame;
import utilities.HelperFunctions;
import utilities.Renderer;

import java.awt.*;
import java.util.ArrayList;

public class Rectangle {
    private boolean editable = true;

    private int id;

    private ArrayList<Point> points = new ArrayList<>();

    private Color color;
    private int thickness;
    private Alignment alignment;
    private LineType lineType;

    public Rectangle(Point firstPoint, Point secondPoint, int id) {
        this.id = id;

        addPoint(firstPoint);

        this.color = EnumStore.getInstance().getDrawColor();
        this.lineType = EnumStore.getInstance().lineType;
        createFromTwoPoints(firstPoint, secondPoint);
    }

    public int getId() { return id; }

    public Color getColor() { return color; }

    public ArrayList<Point> getPoints() { return points; }

    public void alterPoints(int xDiff, int yDiff) { points = HelperFunctions.getInstance().alterPoints(points, xDiff, yDiff); }

    public ArrayList<Line> getLines() { return HelperFunctions.getInstance().getLines(points, color, lineType, thickness); }

    public boolean getEditable() { return editable; }

    public void disable() { editable = false; }

    public void addPoint(Point point) { this.points.add(point); }

    public void updateProperties() {
        Renderer.getInstance().renderLines(getLines(), true, "R" + id); // Unrender

        color = EnumStore.getInstance().getDrawColor(); // Update this rectangle's color from store
        lineType = EnumStore.getInstance().lineType; // Update this rectangle's line type from store
        thickness = StateStore.getInstance().getThickness(); // Update this rectangle's thickness from store

        // Rerender
        Renderer.getInstance().renderLines(getLines(), false, "R" + id);
        Frame.getInstance().getPanel().repaint();
    }

    public void createFromTwoPoints(Point firstPoint, Point secondPoint) {
        thickness = StateStore.getInstance().getThickness(); // Updates the thickness of the object
        alignment = EnumStore.getInstance().alignment; // Updates the alignment (rectangle / square)

        points.clear(); // Clear old points
        addPoint(firstPoint); // First point is the same everywhere

        if (alignment == Alignment.Aligned) { // Square
            int yDiff = secondPoint.getY() - firstPoint.getY();
            int xDiff = secondPoint.getX() - firstPoint.getX();

            int xSign = xDiff == 0 ? 0 : xDiff / Math.abs(xDiff); // Assess the direction of rectangle on x-axis (from first point to second point - LTR = 1, RTL = -1)
            int ySign = yDiff == 0 ? 0 : yDiff / Math.abs(yDiff); // Assess the direction of rectangle on y-axis (from first point to second point - TTB = 1, BTT = -1)
            int sign = xSign * ySign; // Get overall sign

            // Ensure that cursor always touches the square while drawing by assessing which difference is greater
            if (Math.abs(yDiff) > Math.abs(xDiff)) {
                addPoint(new Point(firstPoint.getX(), firstPoint.getY() + yDiff));
                addPoint(new Point(firstPoint.getX() + yDiff * sign, firstPoint.getY() + yDiff));
                addPoint(new Point(firstPoint.getX() + yDiff * sign, firstPoint.getY()));
            } else {
                addPoint(new Point(firstPoint.getX(), firstPoint.getY() + xDiff * sign));
                addPoint(new Point(firstPoint.getX() + xDiff, firstPoint.getY() + xDiff * sign));
                addPoint(new Point(firstPoint.getX() + xDiff, firstPoint.getY()));
            }
        } else { // Rectangle
            addPoint(new Point(firstPoint.getX(), secondPoint.getY()));
            addPoint(secondPoint);
            addPoint(new Point(secondPoint.getX(), firstPoint.getY()));
        }
    }
}
