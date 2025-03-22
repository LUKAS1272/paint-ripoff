package fillers;

import models.Point;

import java.awt.*;

public interface Filler {
    void fill(Point click, Color fillColor);
}
