package controllers;

import models.Point;
import models.Polygon;
import models.canvases.PolygonCanvas;
import stores.EnumStore;
import stores.StateStore;
import utilities.Renderer;

import java.awt.*;

public class PolygonController {
    private static PolygonController instance;

    public static PolygonController getInstance() {
        if (instance == null) {
            instance = new PolygonController();
        }
        return instance;
    }

    private PolygonController() {}

    // --------------------------------------------

    private Point point;

    public void clearPoint() {
        point = null;
    }

    public void createPolygon(int x, int y) {
        System.out.println("Creating Polygon");
        if (point == null) { // If there is no point created, create one and register polygon
            point = new Point(x, y);
            PolygonCanvas.getInstance().addPolygon(new Polygon(point, EnumStore.getInstance().getDrawColor(), EnumStore.getInstance().lineType, StateStore.getInstance().getThickness()));
        } else { // Otherwise add current point to the polygon and rerender
            point = new Point(x, y);
            PolygonCanvas.getInstance().editLastPolygon(point);
            Renderer.getInstance().rerender();
        }
    }
}
