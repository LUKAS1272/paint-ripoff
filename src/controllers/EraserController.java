package controllers;

import enums.ActionType;
import fillers.BasicFiller;
import models.Point;
import models.canvases.CircleCanvas;
import models.canvases.LineCanvas;
import models.canvases.PolygonCanvas;
import models.canvases.RectangleCanvas;
import rasters.RasterBuffer;
import stores.StateStore;
import utilities.HelperFunctions;
import utilities.Renderer;

public class EraserController {
    private static EraserController instance;

    private EraserController() {}

    public static EraserController getInstance() {
        if (instance == null) {
            instance = new EraserController();
        }
        return instance;
    }

    // -----------------------------------------

    private int eraseRadiusMultiplier = 5;

    public void erase(int pointX, int pointY) {
        int eraseRadius = eraseRadiusMultiplier * StateStore.getInstance().getThickness();

        int startY = pointY - eraseRadius;
        int startX = pointX - eraseRadius;

        int endY = pointY + eraseRadius;
        int endX = pointX + eraseRadius;

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                if (HelperFunctions.getInstance().getDistance(new Point(pointX, pointY), x, y) <= eraseRadius) {
                    for (String object : RasterBuffer.getInstance().getBuffer(x, y)) {
                        ActionType objectType = RasterBuffer.getInstance().getActionTypeFromBufferId(object);
                        int objectId = RasterBuffer.getInstance().getObjectIdFromBufferId(object);

                        switch (objectType) {
                            case Line -> LineCanvas.getInstance().disableLineById(objectId);
                            case Rectangle -> RectangleCanvas.getInstance().disableRectangleById(objectId);
                            case Polygon -> PolygonCanvas.getInstance().disablePolygonById(objectId);
                            case Circle -> CircleCanvas.getInstance().disableCircleById(objectId);
                        }
                    }

                    RasterBuffer.getInstance().clear(x, y);
                    BasicFiller.getInstance().setBackgroundPixelColor(x, y, 0);
                }
            }
        }

        Renderer.getInstance().rerender();
    }
}
