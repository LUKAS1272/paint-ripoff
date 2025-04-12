package utilities;

import models.Circle;
import models.canvases.CircleCanvas;
import models.canvases.RectangleCanvas;
import rasterizers.CircleRasterizer;
import stores.EnumStore;
import enums.LineType;
import fillers.BasicFiller;
import models.Line;
import models.canvases.LineCanvas;
import models.canvases.PolygonCanvas;
import rasterizers.DashedLineRasterizer;
import rasterizers.DottedLineRasterizer;
import rasterizers.TrivialLineRasterizer;

import java.awt.*;
import java.util.ArrayList;

public class Renderer {
    private static Renderer instance = null;

    private Renderer() {}

    public static Renderer getInstance() {
        if (instance == null) {
            instance = new Renderer();
        }
        return instance;
    }

    public void rerender()  {
        for (int x = 0; x < Frame.getInstance().getRaster().getWidth(); x++) {
            for (int y = 0; y < 50; y++) {
                Frame.getInstance().getRaster().setPixel(x, y, 0);
            }
        }

        Graphics2D g2d = (Graphics2D) Frame.getInstance().getRaster().getGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        g2d.drawString("Mode: " + EnumStore.getInstance().actionType, 10, 20);
        g2d.drawString("Color: " + EnumStore.getInstance().drawColor, 10, 40);

        Frame.getInstance().getPanel().repaint();
    }

    public void clear() { // Clears the canvas and rerenders frame
        LineCanvas.getInstance().clearLines();
        PolygonCanvas.getInstance().clearPolygons();
        RectangleCanvas.getInstance().clearRectangles();
        CircleCanvas.getInstance().clearCircles();
        BasicFiller.getInstance().clearColorCanvas();
        rerender();
    }

    public void renderCircle(Circle circle, boolean removeMode) {
        CircleRasterizer.getInstance().rasterize(circle, removeMode);
    }

    public void renderLines(ArrayList<Line> lines, boolean removeMode, String bufferOverride) {
        for (Line line : lines) {
            renderLine(line, removeMode, bufferOverride);
        }
    }

    public void renderLine(Line line, boolean removeMode, String bufferOverride) {
        switch (line.getLineType()) {
            case LineType.Default:
                TrivialLineRasterizer.getInstance().rasterize(line, removeMode, bufferOverride);
                break;
            case LineType.Dotted:
                DottedLineRasterizer.getInstance().rasterize(line, removeMode, bufferOverride);
                break;
            case LineType.Dashed:
                DashedLineRasterizer.getInstance().rasterize(line, removeMode, bufferOverride);
                break;
        }
    }
}
