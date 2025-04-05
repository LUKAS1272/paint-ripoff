package models;

import enums.Alignment;
import enums.LineType;
import stores.EnumStore;
import stores.StateStore;

import java.awt.*;
import java.util.ArrayList;

public class Rectangle {
    private ArrayList<Point> points = new ArrayList<>();
    private Color color;
    private int thickness;
    private Alignment alignment;

    public Rectangle(Point firstPoint, Point secondPoint) {
        this.color = EnumStore.getInstance().getDrawColor();
        addPoint(firstPoint);
        createFromTwoPoints(secondPoint);
    }

    public void createFromTwoPoints(Point secondPoint) {
        thickness = StateStore.getInstance().getThickness(); // Updates the thickness of the object
        alignment = EnumStore.getInstance().alignment; // Updates the alignment (rectangle / squalre)

        Point firstPoint = points.getFirst();
        points.clear();
        addPoint(firstPoint); // First point is the same everywhere

        if (alignment == Alignment.Aligned) { // Square
            int yDiff = secondPoint.getY() - firstPoint.getY();
            int xDiff = secondPoint.getX() - firstPoint.getX();

            int xSign = xDiff == 0 ? 0 : xDiff / Math.abs(xDiff);
            int ySign = yDiff == 0 ? 0 : yDiff / Math.abs(yDiff);
            int sign = xSign * ySign;

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

    public void addPoint(Point point) { this.points.add(point); }

    public Color getColor() { return color; }

    public ArrayList<Line> getLines() {
        ArrayList<Line> lines = new ArrayList<>();
        if (points.size() < 2) { return lines; } // If there are not at least 2 points to form a line, return empty array

        Point firstPoint, secondPoint;

        for (int i = 1; i <= points.size(); i++) {
            firstPoint = points.get(i - 1);
            secondPoint = points.get(i % points.size());

            Line polygonLine = new Line(firstPoint, secondPoint, color, LineType.Default, Alignment.Unaligned, thickness, -1);
            lines.add(polygonLine);
        }

        return lines;
    }
}
