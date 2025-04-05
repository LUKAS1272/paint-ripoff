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
    public void rasterize(Line line, boolean removeMode) {
        int x1 = line.getPoint1().getX();
        int y1 = line.getPoint1().getY();
        int x2 = line.getPoint2().getX();
        int y2 = line.getPoint2().getY();

        int dashLength = 6;
        int spaceLength = 3;

        int untilSpace;

        int xDiff = x2 - x1;
        int yDiff = y2 - y1;

        if (xDiff == 0 && yDiff == 0) { return; }

        if (Math.abs(xDiff) >= Math.abs(yDiff)) {
            for (int layerNumber = 0; layerNumber < line.getThickness(); layerNumber++) {
                untilSpace = dashLength * line.getThickness();

                float k = (float) yDiff / xDiff;
                if (line.getAlignment() == Alignment.Aligned) {
                    k = Math.round(k);
                }
                float q = y1 - (k * x1);

                int greaterX = Math.max(x1, x2);
                int lesserX = Math.min(x1, x2);

                for (int x = lesserX; x <= greaterX; x++) {
                    int y = Math.round(k * x + q);

                    if (layerNumber % 2 == 0) { y += layerNumber / 2; }
                    else { y -= (layerNumber + 1) / 2; }

                    RasterBuffer.getInstance().setPixel(x, y, ActionType.Line, line.getId(), removeMode);

                    untilSpace--;
                    if (untilSpace == 0) {
                        x += spaceLength * line.getThickness();
                        untilSpace = dashLength * line.getThickness();
                    }
                }
            }
        } else {
            for (int layerNumber = 0; layerNumber < line.getThickness(); layerNumber++) {
                untilSpace = dashLength * line.getThickness();

                float k = (float) xDiff / yDiff;
                if (line.getAlignment() == Alignment.Aligned) {
                    k = Math.round(k);
                }
                float q = x1 - (k * y1);

                int greaterY = Math.max(y1, y2);
                int lesserY = Math.min(y1, y2);

                for (int y = lesserY; y <= greaterY; y++) {
                    int x = Math.round(k * y + q);

                    if (layerNumber % 2 == 0) { x += layerNumber / 2; }
                    else { x -= (layerNumber + 1) / 2; }

                    RasterBuffer.getInstance().setPixel(x, y, ActionType.Line, line.getId(), removeMode);

                    untilSpace--;
                    if (untilSpace == 0) {
                        y += spaceLength * line.getThickness();
                        untilSpace = dashLength * line.getThickness();
                    }
                }
            }
        }

        Renderer.getInstance().rerender();
    }
}