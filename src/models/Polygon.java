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

    private ArrayList<Point> points = new ArrayList<>();
    private Color color;
    private int thickness;
    private LineType lineType;
    private int id;

    public Polygon(Point firstPoint, Color color, LineType lineType, int thickness, int id) {
        addPoint(firstPoint);
        this.color = color;
        this.lineType = lineType;
        this.thickness = thickness;
        this.id = id;
    }

    public void addPoint(Point point) { this.points.add(point); }

    public Color getColor() { return color; }

    public void updateProperties() {
        lineType = LineType.Default;
        Renderer.getInstance().renderLines(getLines(), true, "P" + id); // Unrender

        color = EnumStore.getInstance().getDrawColor();
        lineType = EnumStore.getInstance().lineType;
        thickness = StateStore.getInstance().getThickness();

        // Rerender
        Renderer.getInstance().renderLines(getLines(), false, "P" + id);
        Frame.getInstance().getPanel().repaint();
    }

    public LineType getLineType() { return lineType; }

    public ArrayList<Point> getPoints() { return points; }

    public ArrayList<Line> getLines() {
        ArrayList<Line> lines = new ArrayList<>();
        if (points.size() < 2) { return lines; } // If there are not at least 2 points to form a line, return empty array

        Point firstPoint, secondPoint;

        for (int i = 1; i <= points.size(); i++) {
            firstPoint = points.get(i - 1);
            secondPoint = points.get(i % points.size());

            Line polygonLine = new Line(firstPoint, secondPoint, color, lineType, Alignment.Unaligned, thickness, -1);
            lines.add(polygonLine);
        }

        return lines;
    }

    public int getId() { return id; }

    public void disable() { editable = false; }

    public boolean getEditable() { return editable; }

    public void editPoint(int index, int x, int y) {
        ArrayList<Point> oldPoints = getPoints();
        points = new ArrayList<>();

        for (int i = 0; i < oldPoints.size(); i++) {
            if (i == index) {
                addPoint(new Point(x, y));
            } else {
                addPoint(oldPoints.get(i));
            }
        }
    }

    public void alterPoints(int xDiff, int yDiff) {
        ArrayList<Point> newPoints = new ArrayList<>();

        for (Point point : points) {
            Point newPoint = new Point(point.getX() + xDiff, point.getY() + yDiff);
            newPoints.add(newPoint);
        }

        points = newPoints;
    }
}
