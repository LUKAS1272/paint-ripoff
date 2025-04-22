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

    public float getDistance(Point p, int x, int y) { // Gets the distance between two points using Pythagorean theorem
        int px = p.getX();
        int py = p.getY();

        int xDiff = Math.abs(px - x);
        int yDiff = Math.abs(py - y);

        return (float) Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
    }

    public String getClosestObject(int pointX, int pointY, boolean lookForPoints) {
        String closestObject = "";
        float closestDistance = Float.MAX_VALUE;
        int tolerance = 30;

        // Find the closest Line
        for (Line line : LineCanvas.getInstance().getLines()) {
            if (!line.getEditable()) { continue; } // Look only for editable objects

            float distanceP1 = HelperFunctions.getInstance().getDistance(line.getPoint1(), pointX, pointY); // Calculates distance from first point of the line
            float distanceP2 = HelperFunctions.getInstance().getDistance(line.getPoint2(), pointX, pointY); // Calculates distance from the second point of the line

            // If any of the two points is closer than the current closestDistance, save it as the closest object
            // L20;1 = Line, id 20, first point
            // L20;2 = Line, id 20, second point
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
            if (!polygon.getEditable()) { continue; } // Look only for editable objects

            int pointIndex = 0;
            for (Point polygonPoint : polygon.getPoints()) { // Iterate through every point of the polygon
                if (HelperFunctions.getInstance().getDistance(polygonPoint, pointX, pointY) < closestDistance) { // Calculate the distance from given point AND check whether it is closer than the closestDistance
                    // If so, save the point as the closest object
                    // P10;1 = Polygon, id 10, first point
                    // P10;N = Polygon, id 10, Nth point
                    closestDistance = HelperFunctions.getInstance().getDistance(polygonPoint, pointX, pointY);
                    closestObject = "P" + polygon.getId() + ";" + pointIndex;
                }
                pointIndex++;
            }
        }

        // Find the closest Rectangle / Circle
        // Set up the perimeter for lookup - a square, in which a circle field is formed
        int startY = pointY - tolerance;
        int startX = pointX - tolerance;

        int endY = pointY + tolerance;
        int endX = pointX + tolerance;
        // -----------------------------------------------------------------------------

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                float currentPixelDistance = HelperFunctions.getInstance().getDistance(new Point(pointX, pointY), x, y); // Get the distance of current pixel from the cursor
                if (currentPixelDistance > closestDistance) { continue; } // If the distance is further than the closest distance, skip to another pixel

                for (String object : RasterBuffer.getInstance().getBuffer(x, y)) { // Iterate through every object stored in current pixel's buffer
                    char objectType = object.charAt(0); // Get object type
                    int objectId = Integer.parseInt(object.substring(1)); // Get object id

                    boolean editable = switch (objectType) { // Check whether the object is editable
                        case 'L' -> !lookForPoints && LineCanvas.getInstance().getLineById(objectId).getEditable(); // Only possible to be true, if lookForPoints is false -> says whether to look for pixels (false) or points (true) of lines
                        case 'P' -> !lookForPoints && PolygonCanvas.getInstance().getPolygonById(objectId).getEditable(); // Only possible to be true, if lookForPoints is false -> says whether to look for pixels (false) or points (true) of polygons
                        case 'R' -> RectangleCanvas.getInstance().getRectangleById(objectId).getEditable();
                        case 'C' -> CircleCanvas.getInstance().getCircleById(objectId).getEditable();
                        default -> false; // If the objectType is invalid, return false
                    };

                    if (editable) { // If the object is editable (it can be moved / edited)
                        closestObject = object;
                        closestDistance = currentPixelDistance;
                        break;
                    }
                }
            }
        }

        return closestDistance > tolerance ? "" : closestObject; // If the closestDistance is not within the tolerance, return empty string, otherwise return the closestObject variable
    }
}
