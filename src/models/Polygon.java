package models;

import enums.Alignment;
import enums.LineType;
import stores.EnumStore;
import stores.StateStore;
import utilities.Frame;
import utilities.HelperFunctions;
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

    public void alterPoints(int xDiff, int yDiff) { points = HelperFunctions.getInstance().alterPoints(points, xDiff, yDiff); }

    public ArrayList<Line> getLines() { return HelperFunctions.getInstance().getLines(points, color, lineType, thickness); }

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
}
