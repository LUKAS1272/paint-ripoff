package models;

import enums.Alignment;
import enums.LineType;

import java.awt.Color;

public class Line {
    private Point point1;
    private Point point2;
    private Color color;
    private LineType lineType;
    private Alignment alignment;
    private int thickness;

    public Line(Point point1, Point point2, Color color, LineType lineType, Alignment alignment, int thickness) {
        this.point1 = point1;
        this.point2 = point2;
        this.color = color;
        this.lineType = lineType;
        this.alignment = alignment;
        this.thickness = thickness;
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

    public Alignment getAlignment() {
        return alignment;
    }

    public int getThickness() { return thickness; }
}
