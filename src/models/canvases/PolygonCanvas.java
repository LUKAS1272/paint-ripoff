package models.canvases;

import models.Line;
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

    public void addPolygon(Polygon polygon) {
        this.polygons.add(polygon);
    }

    public void editPolygonById(Point newPoint, int id) {
        Polygon polygonToEdit = getPolygonById(id);
        polygons.remove(polygonToEdit);
        polygonToEdit.addPoint(newPoint);
        polygons.add(polygonToEdit);
    }

    public void clearPolygons() { this.polygons.clear(); }

    public Polygon getPolygonById(int id) {
        for (Polygon polygon : polygons) {
            if (polygon.getId() == id) {
                return polygon;
            }
        }
        return null;
    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }
}
