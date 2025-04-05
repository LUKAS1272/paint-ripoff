package rasterizers;

import models.Line;

public interface Rasterizer {
    void rasterize(Line line, boolean removeMode);
}