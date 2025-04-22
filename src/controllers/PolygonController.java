package controllers;

import enums.ActionType;
import models.Point;
import models.Polygon;
import models.canvases.PolygonCanvas;
import rasters.RasterBuffer;
import stores.EnumStore;
import stores.StateStore;
import utilities.Renderer;

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
    int editedPointIndex = -1;

    public void clearPoint() {
        point = null;
        editedPointIndex = -1;
    }

    public void createPolygon(int x, int y) {
        if (point == null) { // If there is no point created, create one and register polygon
            id += 1; // Iterate id of the newly created polygon
            currentId = id;
            point = new Point(x, y);
            PolygonCanvas.getInstance().addPolygon(new Polygon(point, EnumStore.getInstance().getDrawColor(), EnumStore.getInstance().lineType, StateStore.getInstance().getThickness(), id));
        } else { // Otherwise add current point to the polygon and rerender
            PolygonCanvas.getInstance().getPolygonById(currentId).updateProperties(); // Update polygon's properties (color, line type...)

            Polygon oldPolygon = PolygonCanvas.getInstance().getPolygonById(currentId); // Get old polygon
            Renderer.getInstance().renderLines(oldPolygon.getLines(), true, RasterBuffer.getInstance().buildBufferId(ActionType.Polygon, currentId)); // Remove old polygon

            point = new Point(x, y); // Create a new point on click cords
            PolygonCanvas.getInstance().editPolygonById(point, currentId); // Add new point to the polygon
            Polygon newPolygon = PolygonCanvas.getInstance().getPolygonById(currentId); // Get new polygon
            Renderer.getInstance().renderLines(newPolygon.getLines(), false, RasterBuffer.getInstance().buildBufferId(ActionType.Polygon, currentId)); // Add new polygon

            Renderer.getInstance().rerender();
        }
    }

    public void edit(String object) {
        String[] identifiers = object.substring(1).split(";"); // Divides objectIdentifier ("P8;2") into 2 parts ("P8" and "2")

        currentId = Integer.parseInt(identifiers[0]); // Gets polygon id ("P8" = 8)
        editedPointIndex = Integer.parseInt(identifiers[1]); // Gets point index that is being moved ("2" = 2)
    }

    public void updatePolygon(int x, int y) {
        if (editedPointIndex < 0) { return; } // If the edited point index is invalid, terminate edit

        PolygonCanvas.getInstance().getPolygonById(currentId).updateProperties(); // Update polygon's properties (color, line type...)

        Polygon oldPolygon = PolygonCanvas.getInstance().getPolygonById(currentId); // Get old polygon that is about to be edited
        Renderer.getInstance().renderLines(oldPolygon.getLines(), true, RasterBuffer.getInstance().buildBufferId(ActionType.Polygon, currentId)); // Remove old polygon

        PolygonCanvas.getInstance().getPolygonById(currentId).editPoint(editedPointIndex, x, y); // Change given point's position
        Polygon newPolygon = PolygonCanvas.getInstance().getPolygonById(currentId); // Retrieve new edited polygon
        Renderer.getInstance().renderLines(newPolygon.getLines(), false, RasterBuffer.getInstance().buildBufferId(ActionType.Polygon, currentId)); // Add new polygon

        Renderer.getInstance().rerender();
    }

    public void changePointsOf(int id, int xDiff, int yDiff) {
        Polygon oldPolygon = PolygonCanvas.getInstance().getPolygonById(id); // Get the old polygon
        Renderer.getInstance().renderLines(oldPolygon.getLines(), true, "P" + id); // Unrender the old polygon

        oldPolygon.alterPoints(xDiff, yDiff); // Move the old polygon
        Renderer.getInstance().renderLines(oldPolygon.getLines(), false, "P" + id); // Render the new polygon
    }
}
