package models.canvases;

import models.Line;

import java.util.ArrayList;

public class LineCanvas {
    private ArrayList<Line> lines;

    public LineCanvas() {
        this.lines = new ArrayList<>();
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }

    public void clearLines() { this.lines.clear(); }

    public void removeLineAt(int index) { this.lines.remove(index); }

    public Line getLineAt(int index) { return this.lines.get(index); }

    public ArrayList<Line> getLines() {
        return lines;
    }
}
