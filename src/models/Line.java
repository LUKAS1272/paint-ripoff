package models;

import enums.Alignment;
import enums.LineType;

import java.awt.Color;

public class Line {
    private boolean editable = true;

    private Point firstPoint;
    private Point secondPoint;

    private int id;
    
    private Color color;
    private LineType lineType;
    private Alignment alignment;
    private int thickness;

    public Line(Point firstPoint, Point secondPoint, Color color, LineType lineType, Alignment alignment, int thickness, int id) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;

        this.id = id;

        this.color = color;
        this.lineType = lineType;
        this.alignment = alignment;
        this.thickness = thickness;
    }

    public Point getPoint1() { return firstPoint; }

    public Point getPoint2() { return secondPoint; }

    public Color getColor() { return color; }

    public LineType getLineType() { return lineType; }

    public Alignment getAlignment() { return alignment; }

    public int getThickness() { return thickness; }

    public int getId() { return id; }

    public void disable() { editable = false; }

    public boolean getEditable() { return editable; }

    public void alterPoints(int xDiff, int yDiff) {
        firstPoint = new Point(firstPoint.getX() + xDiff, firstPoint.getY() + yDiff);
        secondPoint = new Point(secondPoint.getX() + xDiff, secondPoint.getY() + yDiff);
    }
}
