package rasterizers;

import models.Circle;
import models.Point;
import rasters.Raster;
import utilities.Frame;

public class CircleRasterizer {
    private static CircleRasterizer instance;

    private Raster raster;

    private CircleRasterizer() { this.raster = Frame.getInstance().getRaster(); }

    public static CircleRasterizer getInstance() {
        if (instance == null) {
            instance = new CircleRasterizer();
        }
        return instance;
    }

    public void rasterize(Circle circle) {
        int radius = circle.getRadius() + circle.getThickness() - 1; // Add thickness to the radius in order to divide the thickness evenly
        int centerX = circle.getCenter().getX();
        int centerY = circle.getCenter().getY();

        int startX = centerX - radius;
        int startY = centerY - radius;

        int endX = centerX + radius;
        int endY = centerY + radius;

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                if (getDistance(circle.getCenter(), x, y) <= radius && getDistance(circle.getCenter(), x, y) >= circle.getRadius() - circle.getThickness() / 2) {
                    if (x >= 0 && x < raster.getWidth() && y >= 0 && y < raster.getHeight()) { // Out of bounds prevention
                        raster.setPixel(x, y, circle.getColor().getRGB());
                    }
                }
            }
        }
    }

    private float getDistance(Point p, int x, int y) {
        int px = p.getX();
        int py = p.getY();

        int xDiff = Math.abs(px - x);
        int yDiff = Math.abs(py - y);

        return (float) Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
    }
}
