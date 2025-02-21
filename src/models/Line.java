package models;

import enums.LineType;

import java.awt.Color;

public class Line {
    private Point point1;
    private Point point2;
    private Color color;
    private LineType lineType;

    public Line(Point point1, Point point2, Color color, LineType lineType) {
        this.point1 = point1;
        this.point2 = point2;
        this.color = color;
        this.lineType = lineType;
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public Color getColor() {
        return color;
    }

    public LineType getLineType() {
        return lineType;
    }
}
