package rasterizers;

import enums.ActionType;
import enums.Alignment;
import models.Line;
import rasters.RasterBuffer;
import utilities.Renderer;

public class DashedLineRasterizer implements Rasterizer {
    private static DashedLineRasterizer instance;

    private DashedLineRasterizer() {}

    public static DashedLineRasterizer getInstance() {
        if (instance == null) {
            instance = new DashedLineRasterizer();
        }
        return instance;
    }

    @Override
    public void rasterize(Line line, boolean removeMode, String bufferOverride) { // False mode = add to buffer ; True mode = remove from buffer
        int x1 = line.getPoint1().getX();
        int y1 = line.getPoint1().getY();
        int x2 = line.getPoint2().getX();
        int y2 = line.getPoint2().getY();

        int dashLength = 6;
        int spaceLength = 3;

        int untilSpace;

        int xDiff = x2 - x1;
        int yDiff = y2 - y1;

        if (xDiff == 0 && yDiff == 0) { return; } // If the axis differences are null, there is nothing to render

        if (Math.abs(xDiff) >= Math.abs(yDiff)) { // Horizontal like line
            for (int layerNumber = 0; layerNumber < line.getThickness(); layerNumber++) { // Iterate for every thickness layer
                untilSpace = dashLength * line.getThickness(); // Adapt the dash size based on the thickness

                float k = (float) yDiff / xDiff; // Get the tilt coefficient
                if (line.getAlignment() == Alignment.Aligned) { k = Math.round(k); } // Rounding the coefficient results in snapping the line by 45° angles
                float q = y1 - (k * x1);

                // Operate as greater / lesser X to be able to draw both LTR and RTL lines
                int greaterX = Math.max(x1, x2);
                int lesserX = Math.min(x1, x2);

                for (int x = lesserX; x <= greaterX; x++) {
                    int y = Math.round(k * x + q); // Calculate the Y axis of the pixel

                    if (layerNumber % 2 == 0) { y += layerNumber / 2; }
                    else { y -= (layerNumber + 1) / 2; }

                    String targetBuffer = RasterBuffer.getInstance().buildBufferId(ActionType.Line, line.getId()); // Get line's buffer value (Line, id 5 = "L5")
                    RasterBuffer.getInstance().setPixel(x, y, bufferOverride != null ? bufferOverride : targetBuffer, removeMode); // If there is a bufferOverride set, override the target buffer (override happens when a line is used to form a different object e.g. polygon)

                    if (--untilSpace == 0) { // If there is a space upcoming
                        x += spaceLength * line.getThickness(); // Adapt the space size based on the thickness
                        untilSpace = dashLength * line.getThickness(); // Adapt the dash size based on the thickness
                    }
                }
            }
        } else { // Vertical like line
            for (int layerNumber = 0; layerNumber < line.getThickness(); layerNumber++) { // Iterate for every thickness layer
                untilSpace = dashLength * line.getThickness(); // Adapt the dash size based on the thickness

                float k = (float) xDiff / yDiff; // Get the tilt coefficient
                if (line.getAlignment() == Alignment.Aligned) { k = Math.round(k); } // Rounding the coefficient results in snapping the line by 45° angles
                float q = x1 - (k * y1);

                // Operate as greater / lesser Y to be able to draw both ATB and BTA lines
                int greaterY = Math.max(y1, y2);
                int lesserY = Math.min(y1, y2);

                for (int y = lesserY; y <= greaterY; y++) {
                    int x = Math.round(k * y + q); // Calculate the X axis of the pixel

                    // Adjust the X to paint another layer
                    if (layerNumber % 2 == 0) { x += layerNumber / 2; }
                    else { x -= (layerNumber + 1) / 2; }

                    String targetBuffer = RasterBuffer.getInstance().buildBufferId(ActionType.Line, line.getId()); // Get line's buffer value (Line, id 5 = "L5")
                    RasterBuffer.getInstance().setPixel(x, y, bufferOverride != null ? bufferOverride : targetBuffer, removeMode); // If there is a bufferOverride set, override the target buffer (override happens when a line is used to form a different object e.g. polygon)

                    if (--untilSpace == 0) { // If there is a space upcoming
                        y += spaceLength * line.getThickness(); // Adapt the space size based on the thickness
                        untilSpace = dashLength * line.getThickness(); // Adapt the dot size based on the thickness
                    }
                }
            }
        }

        Renderer.getInstance().rerender();
    }
}