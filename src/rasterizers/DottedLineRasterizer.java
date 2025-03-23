package rasterizers;

import enums.Alignment;
import models.Line;
import rasters.Raster;
import utilities.Frame;

import java.awt.*;
import java.util.ArrayList;

public class DottedLineRasterizer implements Rasterizer {
    private static DottedLineRasterizer instance;

    private Raster raster;

    @Override
    public void setColor(Color color) {}

    private DottedLineRasterizer() { this.raster = Frame.getInstance().getRaster();}

    public static DottedLineRasterizer getInstance() {
        if (instance == null) {
            instance = new DottedLineRasterizer();
        }
        return instance;
    }

    @Override
    public void rasterize(Line line) {
        int x1 = line.getPoint1().getX();
        int y1 = line.getPoint1().getY();
        int x2 = line.getPoint2().getX();
        int y2 = line.getPoint2().getY();

        int spaceLength = 1;
        int untilSpace;

        int xDiff = x2 - x1;
        int yDiff = y2 - y1;

        if (Math.abs(xDiff) >= Math.abs(yDiff)) {
            int multiplier = 1;
            for (int layerNumber = 1; layerNumber <= line.getThickness(); layerNumber++) {
                untilSpace = spaceLength * line.getThickness();

                float k = (float) yDiff / xDiff;
                if (line.getAlignment() == Alignment.Aligned) { k = Math.round(k); }
                float q = y1 - (k * x1);

                int greaterX = Math.max(x1, x2);
                int lesserX = Math.min(x1, x2);

                for (int x = lesserX; x <= greaterX; x++) {
                    int y = Math.round(k * x + q);

                    if (x >= 0 && x < raster.getWidth() && y >= 0 && y < raster.getHeight()) { // Out of bounds prevention
                        raster.setPixel(x, y, line.getColor().getRGB());
                    }

                    untilSpace--;
                    if (untilSpace == 0) {
                        x += spaceLength * line.getThickness();
                        untilSpace = spaceLength * line.getThickness();
                    }
                }

                y1 += layerNumber * multiplier;
                multiplier *= -1;
            }
        } else {
            int multiplier = 1;
            for (int layerNumber = 1; layerNumber <= line.getThickness(); layerNumber++) {
                untilSpace = spaceLength * line.getThickness();

                float k = (float) xDiff / yDiff;
                if (line.getAlignment() == Alignment.Aligned) {
                    k = Math.round(k);
                }
                float q = x1 - (k * y1);

                int greaterY = Math.max(y1, y2);
                int lesserY = Math.min(y1, y2);

                for (int y = lesserY; y <= greaterY; y++) {
                    int x = Math.round(k * y + q);

                    if (x >= 0 && x < raster.getWidth() && y >= 0 && y < raster.getHeight()) { // Out of bounds prevention
                        raster.setPixel(x, y, line.getColor().getRGB());
                    }

                    untilSpace--;
                    if (untilSpace == 0) {
                        y += spaceLength * line.getThickness();
                        untilSpace = spaceLength * line.getThickness();
                    }
                }

                x1 += layerNumber * multiplier;
                multiplier *= -1;
            }
        }
    }

    public void rasterizeArray(ArrayList<Line> lines) {
        for (Line line : lines) {
            rasterize(line);
        }
    }
}