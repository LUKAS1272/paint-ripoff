package controllers;

import models.Line;
import models.Point;
import models.Polygon;
import models.canvases.CircleCanvas;
import models.canvases.LineCanvas;
import models.canvases.PolygonCanvas;
import models.canvases.RectangleCanvas;
import rasters.RasterBuffer;

public class EditController {
    private static EditController instance;

    private EditController() {}

    public static EditController getInstance() {
        if (instance == null) {
            instance = new EditController();
        }
        return instance;
    }

    // -----------------------------------------

    public void clear() {
        closestObject = "";
        closestDistance = Float.MAX_VALUE;

        LineController.getInstance().clearPoint();
        PolygonController.getInstance().clearPoint();
        RectangleController.getInstance().clearPoint();
        CircleController.getInstance().clearPoint();
    }

    private String closestObject = "";
    private float closestDistance = Float.MAX_VALUE;
    int tolerance = 30;

    public void edit(int pointX, int pointY) {
        // Find the closest Line
        for (Line line : LineCanvas.getInstance().getLines()) {
            if (!line.getEditable()) { continue; }

            float distanceP1 = getDistance(line.getPoint1(), pointX, pointY); // Calculates distance from first point of the line
            float distanceP2 = getDistance(line.getPoint2(), pointX, pointY); // Calculates distance from the second point of the line

            if (distanceP1 < closestDistance) {
                closestDistance = distanceP1;
                closestObject = "L" + line.getId() + ";1";
            }

            if (distanceP2 < closestDistance) {
                closestDistance = distanceP2;
                closestObject = "L" + line.getId() + ";2";
            }
        }

        // Find the closest Polygon
        for (Polygon polygon : PolygonCanvas.getInstance().getPolygons()) {
            if (!polygon.getEditable()) { continue; }

            int pointIndex = 0;
            for (Point polygonPoint : polygon.getPoints()) {
                if (getDistance(polygonPoint, pointX, pointY) < closestDistance) {
                    closestDistance = getDistance(polygonPoint, pointX, pointY);
                    closestObject = "P" + polygon.getId() + ";" + pointIndex;
                }
                pointIndex++;
            }
        }

        // Find the closest Rectangle / Circle
        int startY = pointY - 30;
        int startX = pointX - 30;

        int endY = pointY + 30;
        int endX = pointX + 30;

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                float currentPixelDistance = getDistance(new Point(pointX, pointY), x, y);
                if (currentPixelDistance > closestDistance) { continue; }

                for (String object : RasterBuffer.getInstance().getBuffer(x, y)) {
                    if (object.startsWith("R")) {
                        int rectangleId = Integer.parseInt(object.substring(1));
                        if (!RectangleCanvas.getInstance().getRectangleById(rectangleId).getEditable()) { continue; }

                        closestObject = object;
                        closestDistance = currentPixelDistance;
                        break;
                    }

                    if (object.startsWith("C")) {
                        int circleId = Integer.parseInt(object.substring(1));
                        if (!CircleCanvas.getInstance().getCircleById(circleId).getEditable()) { continue; }

                        closestObject = object;
                        closestDistance = currentPixelDistance;
                        break;
                    }
                }
            }
        }

        if (closestDistance > tolerance) {
            return;
        }

        switch (RasterBuffer.getInstance().getActionTypeFromBufferId(closestObject)) {
            case Line:
                LineController.getInstance().edit(closestObject);
                break;
            case Polygon:
                PolygonController.getInstance().edit(closestObject);
                break;
            case Rectangle:
                RectangleController.getInstance().edit(closestObject, pointX, pointY);
                break;
            case Circle:
                CircleController.getInstance().edit(closestObject, pointX, pointY);
                break;
        }
    }

    public void editAction(int x, int y) {
        if (closestObject.length() < 2) { return; }

        switch (RasterBuffer.getInstance().getActionTypeFromBufferId(closestObject)) {
            case Line:
                LineController.getInstance().MoveLineDrag(x, y);
                break;
            case Polygon:
                PolygonController.getInstance().updatePolygon(x, y);
                break;
            case Rectangle:
                RectangleController.getInstance().createRectangle(x, y);
                break;
            case Circle:
                CircleController.getInstance().createCircle(x, y);
                break;
        }
    }

    private float getDistance(Point p, int x, int y) {
        int px = p.getX();
        int py = p.getY();

        int xDiff = Math.abs(px - x);
        int yDiff = Math.abs(py - y);

        return (float) Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
    }
}
