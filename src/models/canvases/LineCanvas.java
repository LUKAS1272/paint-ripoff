package models.canvases;

import models.Line;

import java.util.ArrayList;

public class LineCanvas {
    private static LineCanvas instance;

    private ArrayList<Line> lines;

    private LineCanvas() { this.lines = new ArrayList<>(); }

    public static LineCanvas getInstance() {
        if (instance == null) {
            instance = new LineCanvas();
        }
        return instance;
    }

    public void addLine(Line line) { this.lines.add(line); }
    public void clearLines() { this.lines.clear(); }
    public ArrayList<Line> getLines() { return lines; }

    public Line getLineById(int id) {
        for (Line line : lines) { // Iterate through every line
            if (line.getId() == id) { // Look for matching id
                return line; // If matching id is found, return it
            }
        }
        return null; // Otherwise, if there is no matching id found, return null
    }

    public void disableLineById(int id) {
        for (Line line : lines) { // Iterate through every line
            if (line.getId() == id) { // Look for matching id
                line.disable(); // If matching id is found, disable given line
                return; // Return, because id should be unique
            }
        }
    }

    public void editLineById(int id, Line newLine) {
        lines.remove(getLineById(id)); // Remove old line
        lines.add(newLine); // Add new line
    }
}
