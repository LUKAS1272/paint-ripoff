package rasterizers;

import enums.ActionType;
import models.Circle;
import rasters.Raster;
import rasters.RasterBuffer;
import utilities.Frame;
import utilities.HelperFunctions;

public class CircleRasterizer {
    private static CircleRasterizer instance;

    private CircleRasterizer() {}

    public static CircleRasterizer getInstance() {
        if (instance == null) {
            instance = new CircleRasterizer();
        }
        return instance;
    }

    public void rasterize(Circle circle, boolean removeMode) { // False mode = add to buffer ; True mode = remove from buffer
        int radius = circle.getRadius() + circle.getThickness() - 1; // Add thickness to the radius in order to divide the thickness evenly
        int centerX = circle.getCenter().getX();
        int centerY = circle.getCenter().getY();

        // Set up the perimeter for lookup - a square, in which a circle field is formed
        int startX = centerX - radius;
        int startY = centerY - radius;

        int endX = centerX + radius;
        int endY = centerY + radius;
        // -----------------------------------------------------------------------------

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                // If the pixel is within the range of <radius - thickness / 2 ; radius> from the circle center
                // Ensures that line thickness can vary
                if (HelperFunctions.getInstance().getDistance(circle.getCenter(), x, y) <= radius && HelperFunctions.getInstance().getDistance(circle.getCenter(), x, y) >= circle.getRadius() - (float) circle.getThickness() / 2) {
                    String targetBuffer = RasterBuffer.getInstance().buildBufferId(ActionType.Circle, circle.getId());
                    RasterBuffer.getInstance().setPixel(x, y, targetBuffer, removeMode);
                }
            }
        }
    }
}
