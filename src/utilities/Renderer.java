package utilities;

import enums.EnumStore;
import enums.LineType;
import fillers.BasicFiller;
import models.Line;
import models.Polygon;
import models.canvases.LineCanvas;
import models.canvases.PolygonCanvas;
import rasterizers.DashedLineRasterizer;
import rasterizers.DottedLineRasterizer;
import rasterizers.TrivialLineRasterizer;
import rasters.Raster;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Renderer {
    private static Renderer instance = null;

//    private Raster raster;
//    private JPanel panel;

    private Renderer(Raster raster, JPanel panel) {
//        if (raster != null) { this.raster = raster; }
//        if (panel != null) { this.panel = panel; }
    }

    public static Renderer getInstance(Raster raster, JPanel panel) {
        if (instance == null) {
            instance = new Renderer(raster, panel);
        }
        return instance;
    }

    public static Renderer getInstance() {
        return getInstance(null, null);
    }

    public void rerender()  {
        Frame.getInstance().getRaster().clear();

        for (int y = 0; y < Frame.getInstance().getRaster().getHeight(); y++) {
            for (int x = 0; x < Frame.getInstance().getRaster().getWidth(); x++) {
                int color = BasicFiller.getInstance().getColorCanvas()[x][y];

                if (color != 0) {
                    Frame.getInstance().getRaster().setPixel(x, y, color);
                }
            }
        }

        renderLines(LineCanvas.getInstance().getLines());

        for (Polygon polygon : PolygonCanvas.getInstance().getPolygons()) {
            TrivialLineRasterizer.getInstance().rasterizeArray(polygon.getLines());
        }

        Graphics2D g2d = (Graphics2D) Frame.getInstance().getRaster().getGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String modeText = "Mode: " + EnumStore.getInstance().getObjectType();
        g2d.drawString(modeText, 10, 20);

        Frame.getInstance().getPanel().repaint();
    }

    public void clear() {
        LineCanvas.getInstance().clearLines();
        PolygonCanvas.getInstance().clearPolygons();
        BasicFiller.getInstance().clearColorCanvas();
        rerender();
    }

    public void renderLines(ArrayList<Line> lines) {
        for (Line line : lines) {
            switch (line.getLineType()) {
                case LineType.DEFAULT:
                    TrivialLineRasterizer.getInstance().rasterize(line);
                    break;
                case LineType.DOTTED:
                    DottedLineRasterizer.getInstance().rasterize(line);
                    break;
                case LineType.DASHED:
                    DashedLineRasterizer.getInstance().rasterize(line);
                    break;
            }
        }
    }
}
