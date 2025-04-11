package rasters;

import enums.ActionType;
import fillers.BasicFiller;
import models.canvases.LineCanvas;
import models.canvases.PolygonCanvas;
import models.canvases.RectangleCanvas;
import stores.EnumStore;
import utilities.Frame;

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
            Frame.getInstance().getRaster().setPixel(x, y, EnumStore.getInstance().getDrawColor().getRGB());
        }
    }

    public void removeFromBuffer(int x, int y, String targetBuffer) {
        ArrayList<String> currentBuffer = getBuffer(x, y);
        currentBuffer.remove(targetBuffer); // Remove the object

        if (currentBuffer.isEmpty()) {
            buffer.remove(x + ";" + y);
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
        String topLayer = getTopLayer(x, y);

        if (topLayer.length() != 2) { return BasicFiller.getInstance().getBackgroundPixelColor(x, y); }

        char objectType = topLayer.charAt(0);
        int objectId = Integer.parseInt(topLayer.substring(1, 2));

        return switch (objectType) {
            case 'L' -> LineCanvas.getInstance().getLineById(objectId).getColor().getRGB();
            case 'P' -> PolygonCanvas.getInstance().getPolygonById(objectId).getColor().getRGB();
            case 'R' -> RectangleCanvas.getInstance().getRectangleById(objectId).getColor().getRGB();
            default -> BasicFiller.getInstance().getBackgroundPixelColor(x, y);
        };
    }

    public ArrayList<String> getBuffer(int x, int y) {
        return buffer.getOrDefault(x + ";" + y, new ArrayList<>());
    }

    public HashMap<String, ArrayList<String>> getBuffer() {
        return buffer;
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
