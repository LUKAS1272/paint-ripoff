package utilities;

import models.canvases.CircleCanvas;
import models.canvases.RectangleCanvas;
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
//        Frame.getInstance().getRaster().clear();
//
//        for (int y = 0; y < Frame.getInstance().getRaster().getHeight(); y++) {
//            for (int x = 0; x < Frame.getInstance().getRaster().getWidth(); x++) {
//                int color = BasicFiller.getInstance().getColorCanvas()[x][y];
//
//                if (color != 0) {
//                    Frame.getInstance().getRaster().setPixel(x, y, color);
//                }
//            }
//        }
//
//        renderLines(LineCanvas.getInstance().getLines());
//
//        for (Polygon polygon : PolygonCanvas.getInstance().getPolygons()) {
//            TrivialLineRasterizer.getInstance().rasterizeArray(polygon.getLines());
//        }
//
//        for (Rectangle rectangle : RectangleCanvas.getInstance().getRectangles()) {
//            TrivialLineRasterizer.getInstance().rasterizeArray(rectangle.getLines());
//        }
//
//        for (Circle circle : CircleCanvas.getInstance().getCircles()) {
//            CircleRasterizer.getInstance().rasterize(circle);
//        }
//
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

    public void renderLine(Line line, boolean removeMode) {
        switch (line.getLineType()) {
            case LineType.Default:
                TrivialLineRasterizer.getInstance().rasterize(line, removeMode);
                break;
            case LineType.Dotted:
                DottedLineRasterizer.getInstance().rasterize(line);
                break;
            case LineType.Dashed:
                DashedLineRasterizer.getInstance().rasterize(line);
                break;
        }
    }
}
