package rasters;

import enums.ActionType;
import fillers.BasicFiller;
import models.canvases.CircleCanvas;
import models.canvases.LineCanvas;
import models.canvases.PolygonCanvas;
import models.canvases.RectangleCanvas;
import utilities.Frame;
import utilities.Renderer;

import java.util.ArrayList;
import java.util.HashMap;

public class RasterBuffer {
    private static RasterBuffer instance;

    public static RasterBuffer getInstance() {
        if (instance == null) {
            instance = new RasterBuffer();
        }
        return instance;
    }

    private RasterBuffer() {}

    // --------------------------------------------------------------------------------------------------------
    // Buffer management
    // --------------------------------------------------------------------------------------------------------

    HashMap<String, ArrayList<String>> buffer = new HashMap<String, ArrayList<String>>();

    public void addToBuffer(int x, int y, String targetBuffer) {
        ArrayList<String> currentBuffer = getBuffer(x, y);

        if (!currentBuffer.contains(targetBuffer)) { // If the object isn't part of the buffer on these cords yet
            currentBuffer.add(targetBuffer); // Add the object to the buffer
            buffer.put(x + ";" + y, currentBuffer); // Update the buffer
            Frame.getInstance().getRaster().setPixel(x, y, getTopLayerColor(x, y));
        }
    }

    public void clear(int x, int y) { // Removes buffered object of given pixel
        buffer.remove(x + ";" + y);
        Frame.getInstance().getRaster().setPixel(x, y, getTopLayerColor(x, y));
    }

    public void clearCanvas() {
        buffer = new HashMap<>(); // Clear the objects from buffer
        BasicFiller.getInstance().clearColorCanvas(); // Clear the background

        // Rerender all points of the canvas
        for (int x = 0; x < 1920; x++) {
            for (int y = 0; y < 1080; y++) {
                Frame.getInstance().getRaster().setPixel(x, y, getTopLayerColor(x, y));
            }
        }

        Renderer.getInstance().rerender();
    }

    public void removeFromBuffer(int x, int y, String targetBuffer) {
        ArrayList<String> currentBuffer = getBuffer(x, y);
        currentBuffer.remove(targetBuffer); // Remove the object

        if (currentBuffer.isEmpty()) { // If the pixel's buffer is empty
            buffer.remove(x + ";" + y); // Remove the key from hashmap
        } else {
            buffer.put(x + ";" + y, currentBuffer); // Update the buffer
        }
        Frame.getInstance().getRaster().setPixel(x, y, getTopLayerColor(x, y));
    }

    // --------------------------------------------------------------------------------------------------------
    // Getters
    // --------------------------------------------------------------------------------------------------------

    public String getTopLayer(int x, int y) {
        ArrayList<String> currentBuffer = getBuffer(x, y);
        return currentBuffer.isEmpty() ? "" : currentBuffer.getLast(); // The last object in buffer is top layer - the one that should be rendered
    }

    public int getTopLayerColor(int x, int y) {
        String topLayer = getTopLayer(x, y); // Gets the top layer object

        if (topLayer.length() < 2) { return BasicFiller.getInstance().getBackgroundPixelColor(x, y); } // If the topLayer is invalid, return the background color

        char objectType = topLayer.charAt(0); // Extracts object type
        int objectId = Integer.parseInt(topLayer.substring(1)); // Extracts object id

        return switch (objectType) { // Based on the object parameters, return the color of the object
            case 'L' -> LineCanvas.getInstance().getLineById(objectId).getColor().getRGB();
            case 'P' -> PolygonCanvas.getInstance().getPolygonById(objectId).getColor().getRGB();
            case 'R' -> RectangleCanvas.getInstance().getRectangleById(objectId).getColor().getRGB();
            case 'C' -> CircleCanvas.getInstance().getCircleById(objectId).getColor().getRGB();
            default -> BasicFiller.getInstance().getBackgroundPixelColor(x, y);
        };
    }

    public ArrayList<String> getBuffer(int x, int y) {
        return buffer.getOrDefault(x + ";" + y, new ArrayList<>());
    }

    // --------------------------------------------------------------------------------------------------------

    public void setPixel(int x, int y, String targetBuffer, boolean removeMode) {
        if (x >= 0 && x < Frame.getInstance().getRaster().getWidth() && y >= 0 && y < Frame.getInstance().getRaster().getHeight()) { // Out of bounds prevention
            if (removeMode) {
                removeFromBuffer(x, y, targetBuffer);
            } else {
                addToBuffer(x, y, targetBuffer);
            }
        }
    }

    public ActionType getActionTypeFromBufferId(String bufferId) {
        if (bufferId.length() < 2) { return null; }

        char objectType = bufferId.charAt(0);

        return switch (objectType) {
            case 'L' -> ActionType.Line;
            case 'P' -> ActionType.Polygon;
            case 'R' -> ActionType.Rectangle;
            case 'C' -> ActionType.Circle;
            default -> null;
        };
    }

    public int getObjectIdFromBufferId(String bufferId) {
        if (bufferId.length() < 2) { return -1; }

        return Integer.parseInt(bufferId.substring(1));
    }

    public String buildBufferId(ActionType objectType, int id) {
        return switch (objectType) {
            case Line -> "L" + id;
            case Polygon -> "P" + id;
            case Rectangle -> "R" + id;
            case Circle -> "C" + id;
            default -> "";
        };
    }
}
