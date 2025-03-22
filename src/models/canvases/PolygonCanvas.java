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

    public void addPolygon(Polygon polygon) {
        this.polygons.add(polygon);
    }

    public void editLastPolygon(Point newPoint) { this.polygons.getLast().addPoint(newPoint); }

    public void clearPolygons() { this.polygons.clear(); }

    public Polygon getPolygonAt(int index) { return this.polygons.get(index); }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }
}
