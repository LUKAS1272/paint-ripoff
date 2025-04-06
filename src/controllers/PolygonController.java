package controllers;

import enums.ActionType;
import models.Line;
import models.Point;
import models.Polygon;
import models.canvases.PolygonCanvas;
import rasters.RasterBuffer;
import stores.EnumStore;
import stores.StateStore;
import utilities.Renderer;

import java.awt.*;
import java.util.ArrayList;

public class PolygonController {
    private static PolygonController instance;

    public static PolygonController getInstance() {
        if (instance == null) {
            instance = new PolygonController();
        }
        return instance;
    }

    private PolygonController() {}

    private int id = 0;
    private int currentId = 0;

    // --------------------------------------------

    private Point point;

    public void clearPoint() {
        point = null;
    }

    public void createPolygon(int x, int y) {
        System.out.println("Creating Polygon");
        if (point == null) { // If there is no point created, create one and register polygon
            id += 1; // Iterate id of the newly created polygon
            currentId = id;
            point = new Point(x, y);
            PolygonCanvas.getInstance().addPolygon(new Polygon(point, EnumStore.getInstance().getDrawColor(), EnumStore.getInstance().lineType, StateStore.getInstance().getThickness(), id));
        } else { // Otherwise add current point to the polygon and rerender
            PolygonCanvas.getInstance().getPolygonById(currentId).updateProperties();
            point = new Point(x, y);

            Polygon oldPolygon = PolygonCanvas.getInstance().getPolygonById(currentId);
            Renderer.getInstance().renderLines(oldPolygon.getLines(), true, RasterBuffer.getInstance().buildBufferId(ActionType.Polygon, currentId)); // Remove

            PolygonCanvas.getInstance().editPolygonById(point, currentId);
            Polygon newPolygon = PolygonCanvas.getInstance().getPolygonById(currentId);
            Renderer.getInstance().renderLines(newPolygon.getLines(), false, RasterBuffer.getInstance().buildBufferId(ActionType.Polygon, currentId)); // Add

            Renderer.getInstance().rerender();
        }
    }
}
