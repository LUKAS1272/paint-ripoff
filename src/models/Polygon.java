package models;

import enums.Alignment;
import enums.LineType;

import java.awt.Color;
import java.util.ArrayList;

public class Polygon {
    private ArrayList<Point> points = new ArrayList<>();
    private Color color;
    private LineType lineType;

    public Polygon(Point firstPoint, Color color, LineType lineType) {
        addPoint(firstPoint);
        this.color = color;
        this.lineType = lineType;
    }

    public void addPoint(Point point) { this.points.add(point); }

    public Color getColor() { return color; }

    public LineType getLineType() { return lineType; }

    public ArrayList<Point> getPoints() { return points; }

    public ArrayList<Line> getLines() {
        ArrayList<Line> lines = new ArrayList<>();
        if (points.size() < 2) { return lines; } // If there are not at least 2 points to form a line, return empty array

        Point firstPoint, secondPoint;

        for (int i = 1; i <= points.size(); i++) {
            firstPoint = points.get(i - 1);
            secondPoint = points.get(i % points.size());

            Line polygonLine = new Line(firstPoint, secondPoint, color, lineType, Alignment.UNALIGNED);
            lines.add(polygonLine);
        }

        return lines;
    }
}
