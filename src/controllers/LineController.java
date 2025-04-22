package controllers;

import stores.EnumStore;
import models.Point;
import models.Line;

import models.canvases.LineCanvas;
import stores.StateStore;
import utilities.Frame;
import utilities.Renderer;

public class LineController {
    private static LineController instance;

    private LineController() {}

    public static LineController getInstance() {
        if (instance == null) {
            instance = new LineController();
        }
        return instance;
    }

    private int id = 0;
    private int currentId = 0;

    private Point point;

    public void clearPoint() { point = null; }

    public void createNewLine(int x, int y) {
        point = new Point(x, y);
        id += 1; // Iterate id of the newly created line
        currentId = id; // Update currently used id to the new line id
        createLine(x, y); // Create a line object
    }

    public void createLine(int x, int y) {
        if (point != null) {
            Point point2 = new Point(x, y);
            Line line = new Line(point, point2, EnumStore.getInstance().getDrawColor(), EnumStore.getInstance().lineType, EnumStore.getInstance().alignment, StateStore.getInstance().getThickness(), currentId);

            LineCanvas.getInstance().addLine(line); // Add currently drawn line to the canvas
        }
    }

    public void edit(String object) {
        String[] identifiers = object.substring(1).split(";"); // Divides objectIdentifier ("L5;1") into 2 parts ("L5" and "1")

        currentId = Integer.parseInt(identifiers[0]); // Gets line id ("L5" = 5)
        Line editedLine = LineCanvas.getInstance().getLineById(currentId); // Retrieves currently edited line

        int pointId = Integer.parseInt(identifiers[1]); // Gets point number that is being moved ("1" = 1)

        // If the selected point is valid, pick its opposite point as the starting one
        if (pointId == 1) {
            point = editedLine.getPoint2();
        } else if (pointId == 2) {
            point = editedLine.getPoint1();
        } else { // Otherwise call fallback case
            point = null;
            currentId = -1;
        }
    }

    public void MoveLineDrag(int x, int y) {
        if (point != null) { // There is a valid starting point
            Renderer.getInstance().renderLine(LineCanvas.getInstance().getLineById(currentId), true, null); // Remove current line from buffer

            Point point2 = new Point(x, y); // Get the second point of line
            Line line = new Line(point, point2, EnumStore.getInstance().getDrawColor(), EnumStore.getInstance().lineType, EnumStore.getInstance().alignment, StateStore.getInstance().getThickness(), currentId);
            LineCanvas.getInstance().editLineById(currentId, line); // Replace the old line with new one

            Renderer.getInstance().renderLine(LineCanvas.getInstance().getLineById(currentId), false, null); // Add current line to buffer

            Frame.getInstance().getPanel().repaint(); // Update the frame
        }
    }

    public void changePointsOf(int id, int xDiff, int yDiff) {
        Line oldLine = LineCanvas.getInstance().getLineById(id); // Get the old line
        Renderer.getInstance().renderLine(oldLine, true, null); // Unrender the old line

        oldLine.alterPoints(xDiff, yDiff); // Move the old line
        Renderer.getInstance().renderLine(oldLine, false, null); // Render the new line
    }
}
