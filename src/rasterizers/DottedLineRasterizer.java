package rasterizers;

import enums.Alignment;
import models.Line;
import rasters.Raster;

import java.awt.*;
import java.util.ArrayList;

public class DottedLineRasterizer implements Rasterizer {
    private Raster raster;

    @Override
    public void setColor(Color color) {}

    public DottedLineRasterizer(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        int x1 = line.getPoint1().getX();
        int y1 = line.getPoint1().getY();
        int x2 = line.getPoint2().getX();
        int y2 = line.getPoint2().getY();

        int spaceLength = 10;

        int xDiff = x2 - x1;
        int yDiff = y2 - y1;

        if (Math.abs(xDiff) >= Math.abs(yDiff)) {
            float k = (float) yDiff / xDiff;
            if (line.getAlignment() == Alignment.ALIGNED) { k = Math.round(k); }
            float q = y1 - (k * x1);

            int greaterX = Math.max(x1, x2);
            int lesserX = Math.min(x1, x2);

            for (int x = lesserX; x <= greaterX; x++) {
                int y = Math.round(k * x + q);
                raster.setPixel(x, y, line.getColor().getRGB());

                x += spaceLength;
            }
        } else {
            float k = (float) xDiff / yDiff;
            if (line.getAlignment() == Alignment.ALIGNED) { k = Math.round(k); }
            float q = x1 - (k * y1);

            int greaterY = Math.max(y1, y2);
            int lesserY = Math.min(y1, y2);

            for (int y = lesserY; y <= greaterY; y++) {
                int x = Math.round(k * y + q);
                raster.setPixel(x, y, line.getColor().getRGB());

                y += spaceLength;
            }
        }
    }

    public void rasterizeArray(ArrayList<Line> lines) {
        for (Line line : lines) {
            rasterize(line);
        }
    }
}