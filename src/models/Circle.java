package models;

import stores.EnumStore;
import stores.StateStore;
import utilities.Frame;
import utilities.Renderer;

import java.awt.*;

public class Circle {
    private boolean editable = true;

    private Point center;
    private int radius;

    private int id;

    private Color color;
    private int thickness;

    public Circle(Point firstPoint, Point secondPoint, int id) {
        this.color = EnumStore.getInstance().getDrawColor();
        this.id = id;
        createCircle(firstPoint, secondPoint);
    }

    public void createCircle(Point firstPoint, Point secondPoint) {
        this.thickness = StateStore.getInstance().getThickness(); // Load thickness from store

        int xDiff = secondPoint.getX() - firstPoint.getX();
        int yDiff = secondPoint.getY() - firstPoint.getY();

        int largerDiff = Math.max(Math.abs(xDiff), Math.abs(yDiff)); // Choose the greater difference

        int xSign = xDiff == 0 ? 1 : xDiff / Math.abs(xDiff); // Assess the direction of circle on x-axis (from first point to second point - LTR = 1, RTL = -1)
        int ySign = yDiff == 0 ? 1 : yDiff / Math.abs(yDiff); // Assess the direction of circle on y-axis (from first point to second point - TTB = 1, BTT = -1)

        radius = largerDiff / 2; // Get the radius based on the greater difference
        center = new Point(firstPoint.getX() + radius * xSign, firstPoint.getY() + radius * ySign); // Create a center point (first point + directed radius on both axis)
    }

    public void updateProperties() {
        Renderer.getInstance().renderCircle(this, true); // Unrender

        color = EnumStore.getInstance().getDrawColor(); // Update this circle's color from store
        thickness = StateStore.getInstance().getThickness(); // Update this circle's thickness from store

        // Rerender
        Renderer.getInstance().renderCircle(this, false);
        Frame.getInstance().getPanel().repaint();
    }

    public void alterCenter(int xDiff, int yDiff) { center = new Point(center.getX() + xDiff, center.getY() + yDiff); }

    public int getRadius() { return this.radius; }

    public Point getCenter() { return this.center; }

    public int getThickness() { return this.thickness; }

    public Color getColor() { return this.color; }

    public int getId() { return this.id; }

    public void disable() { editable = false; }

    public boolean getEditable() { return editable; }
}
