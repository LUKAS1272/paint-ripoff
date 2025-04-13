package utilities;

import models.Line;
import models.Point;
import models.Polygon;
import models.canvases.CircleCanvas;
import models.canvases.LineCanvas;
import models.canvases.PolygonCanvas;
import models.canvases.RectangleCanvas;
import rasters.RasterBuffer;

public class HelperFunctions {
    private static HelperFunctions instance;

    private HelperFunctions() {}

    public static HelperFunctions getInstance() {
        if (instance == null) {
            instance = new HelperFunctions();
        }
        return instance;
    }

    // -----------------------------------------

    public float getDistance(Point p, int x, int y) {
        int px = p.getX();
        int py = p.getY();

        int xDiff = Math.abs(px - x);
        int yDiff = Math.abs(py - y);

        return (float) Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
    }

    public String getClosestObject(int pointX, int pointY) {
        String closestObject = "";
        float closestDistance = Float.MAX_VALUE;
        int tolerance = 30;

        // Find the closest Line
        for (Line line : LineCanvas.getInstance().getLines()) {
            if (!line.getEditable()) { continue; }

            float distanceP1 = HelperFunctions.getInstance().getDistance(line.getPoint1(), pointX, pointY); // Calculates distance from first point of the line
            float distanceP2 = HelperFunctions.getInstance().getDistance(line.getPoint2(), pointX, pointY); // Calculates distance from the second point of the line

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
                if (HelperFunctions.getInstance().getDistance(polygonPoint, pointX, pointY) < closestDistance) {
                    closestDistance = HelperFunctions.getInstance().getDistance(polygonPoint, pointX, pointY);
                    closestObject = "P" + polygon.getId() + ";" + pointIndex;
                }
                pointIndex++;
            }
        }

        // Find the closest Rectangle / Circle
        int startY = pointY - tolerance;
        int startX = pointX - tolerance;

        int endY = pointY + tolerance;
        int endX = pointX + tolerance;

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                float currentPixelDistance = HelperFunctions.getInstance().getDistance(new Point(pointX, pointY), x, y);
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
            return "";
        }
        return closestObject;
    }
}
