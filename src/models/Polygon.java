package models;

import enums.Alignment;
import enums.LineType;
import stores.EnumStore;
import stores.StateStore;
import utilities.Frame;
import utilities.Renderer;

import java.awt.Color;
import java.util.ArrayList;

public class Polygon {
    private boolean editable = true;

    private int id;

    private ArrayList<Point> points = new ArrayList<>();

    private Color color;
    private int thickness;
    private LineType lineType;

    public Polygon(Point firstPoint, Color color, LineType lineType, int thickness, int id) {
        this.id = id;

        addPoint(firstPoint);

        this.color = color;
        this.thickness = thickness;
        this.lineType = lineType;
    }

    public void addPoint(Point point) { this.points.add(point); }

    public ArrayList<Point> getPoints() { return points; }

    public Color getColor() { return color; }

    public int getId() { return id; }

    public boolean getEditable() { return editable; }

    public void disable() { editable = false; }

    public void updateProperties() {
        Renderer.getInstance().renderLines(getLines(), true, "P" + id); // Unrender

        color = EnumStore.getInstance().getDrawColor(); // Update this polygon's color from store
        lineType = EnumStore.getInstance().lineType; // Update this polygon's line type from store
        thickness = StateStore.getInstance().getThickness(); // Update this polygon's thickness from store

        // Rerender
        Renderer.getInstance().renderLines(getLines(), false, "P" + id);
        Frame.getInstance().getPanel().repaint();
    }

    public ArrayList<Line> getLines() {
        ArrayList<Line> lines = new ArrayList<>(); // Create a list to store lines
        if (points.size() < 2) { return lines; } // If there are not at least 2 points to form a line, return empty array

        Point firstPoint, secondPoint;

        for (int i = 1; i <= points.size(); i++) { // Iterate through every possible starting point
            firstPoint = points.get(i - 1); // Assign starting point
            secondPoint = points.get(i % points.size()); // Starting point index + 1 (modulo prevents overflowing)

            Line polygonLine = new Line(firstPoint, secondPoint, color, lineType, Alignment.Unaligned, thickness, -1); // Create a line
            lines.add(polygonLine); // Add line to the list
        }

        return lines; // Return generated lines
    }

    public void editPoint(int index, int x, int y) { // Edits point with certain index to be on the cords xy
        ArrayList<Point> oldPoints = getPoints(); // Gets old points to iterate through
        points = new ArrayList<>(); // Clears current points

        for (int i = 0; i < oldPoints.size(); i++) { // Iterate through every point
            if (i == index) { // If the index matches the one, that should be edited
                addPoint(new Point(x, y)); // Add a point that is moved
            } else {
                addPoint(oldPoints.get(i)); // Otherwise add a point as it was
            }
        }
    }

    public void alterPoints(int xDiff, int yDiff) {
        ArrayList<Point> newPoints = new ArrayList<>(); // Create a list to store new points (after being moved)

        for (Point point : points) { // Iterate through every point
            Point newPoint = new Point(point.getX() + xDiff, point.getY() + yDiff);
            newPoints.add(newPoint); // Add moved point to new points list
        }

        points = newPoints; // Set moved points as current points
    }
}
