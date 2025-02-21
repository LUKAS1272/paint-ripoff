package models;

import java.util.ArrayList;

public class LineCanvas {
    private ArrayList<Line> lines;

    public LineCanvas() {
        this.lines = new ArrayList<>();
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }

    public void clearLine() {
        this.lines.clear();
    }

    public ArrayList<Line> getLines() {
        return lines;
    }
}
