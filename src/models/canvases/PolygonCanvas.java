package models.canvases;

import models.Point;
import models.Polygon;

import java.util.ArrayList;

public class PolygonCanvas {
    private static PolygonCanvas instance;

    private ArrayList<Polygon> polygons;

    private PolygonCanvas() { this.polygons = new ArrayList<>(); }

    public static PolygonCanvas getInstance() {
        if (instance == null) {
            instance = new PolygonCanvas();
        }
        return instance;
    }

    public void addPolygon(Polygon polygon) { this.polygons.add(polygon); }
    public void clearPolygons() { this.polygons.clear(); }
    public ArrayList<Polygon> getPolygons() { return polygons; }

    public void editPolygonById(Point newPoint, int id) {
        Polygon polygonToEdit = getPolygonById(id); // Gets old polygon
        polygons.remove(polygonToEdit); // Remove old polygon

        polygonToEdit.addPoint(newPoint); // Add point to old polygon
        polygons.add(polygonToEdit); // Add new polygon
    }

    public Polygon getPolygonById(int id) {
        for (Polygon polygon : polygons) { // Iterate through every polygon
            if (polygon.getId() == id) { // Look for matching id
                return polygon; // If matching id is found, return it
            }
        }
        return null; // Otherwise, if there is no matching id found, return null
    }

    public void disablePolygonById(int id) {
        for (Polygon polygon : polygons) { // Iterate through every polygon
            if (polygon.getId() == id) { // Look for matching id
                polygon.disable(); // If matching id is found, disable given polygon
                return; // Return, because id should be unique
            }
        }
    }
}
