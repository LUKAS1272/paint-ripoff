package models.canvases;

import models.Line;

import java.util.ArrayList;

public class LineCanvas {
    private static LineCanvas instance;

    private ArrayList<Line> lines;

    private LineCanvas() {
        this.lines = new ArrayList<>();
    }

    public static LineCanvas getInstance() {
        if (instance == null) {
            instance = new LineCanvas();
        }
        return instance;
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }

    public void clearLines() { this.lines.clear(); }

    public void removeLineAt(int index) { this.lines.remove(index); }

    public Line getLineById(int id) {
        for (Line line : lines) {
            if (line.getId() == id) {
                return line;
            }
        }
        return null;
    }

    public void disableLineById(int id) {
        for (Line line : lines) {
            if (line.getId() == id) {
                line.disable();
                return;
            }
        }
    }

    public void editLineById(int id, Line newLine) {
        lines.remove(getLineById(id));
        lines.add(newLine);
    }

    public Line getLineAt(int index) { return this.lines.get(index); }

    public ArrayList<Line> getLines() {
        return lines;
    }
}
