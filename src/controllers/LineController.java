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

    // -----------------------------------------

    private Point point;

    public void createNewLine(int x, int y) {
        point = new Point(x, y);
        id += 1; // Iterate id of the newly created line
        currentId = id; // Update currently used id to the new line id
        createLine(x, y); // Create a line object
    }

    public void clearPoint() {
        point = null;
    }

    public void createLine(int x, int y) {
        if (point != null) {
            Point point2 = new Point(x, y);
            Line line = new Line(point, point2, EnumStore.getInstance().getDrawColor(), EnumStore.getInstance().lineType, EnumStore.getInstance().alignment, StateStore.getInstance().getThickness(), currentId);

            LineCanvas.getInstance().addLine(line); // Add currently drawn line to the canvas
        }
    }

    // ------------------------------------------

    public void edit(String object) {
        String[] identifiers = object.substring(1).split(";");

        int lineId = Integer.parseInt(identifiers[0]);
        Line editedLine = LineCanvas.getInstance().getLineById(lineId);

        int pointId = Integer.parseInt(identifiers[1]);

        currentId = lineId;
        if (pointId == 1) {
            point = editedLine.getPoint2();
        } else if (pointId == 2) {
            point = editedLine.getPoint1();
        } else {
            point = null;
            currentId = -1;
        }
    }

    public void MoveLineDrag(int x, int y) {
        if (point != null) {
            Point point2 = new Point(x, y);

            Renderer.getInstance().renderLine(LineCanvas.getInstance().getLineById(currentId), true, null); // Remove current line from buffer

            Line line = new Line(point, point2, EnumStore.getInstance().getDrawColor(), EnumStore.getInstance().lineType, EnumStore.getInstance().alignment, StateStore.getInstance().getThickness(), currentId);
            LineCanvas.getInstance().editLineById(currentId, line);

            Renderer.getInstance().renderLine(LineCanvas.getInstance().getLineById(currentId), false, null); // Add current line to buffer

            Frame.getInstance().getPanel().repaint(); // Update the canvas
        }
    }
}
