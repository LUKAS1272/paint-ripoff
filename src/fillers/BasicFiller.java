package fillers;

import models.Point;
import rasters.Raster;
import utilities.Frame;
import utilities.Renderer;

import java.awt.*;
import java.util.Stack;

public class BasicFiller implements Filler {
    private static BasicFiller instance;

    private Raster raster;

    private Stack<Cords> nextSeeds = new Stack<>();

    private int[][] colorCanvas;

    private BasicFiller() {
        this.raster = Frame.getInstance().getRaster();
        this.colorCanvas = new int[raster.getWidth()][raster.getHeight()];
    }

    public static BasicFiller getInstance() {
        if (instance == null) {
            instance = new BasicFiller();
        }
        return instance;
    }

    public int[][] getColorCanvas() { return colorCanvas; }

    public void clearColorCanvas() {  colorCanvas = new int[raster.getWidth()][raster.getHeight()]; }

    @Override
    public void fill(Point click, Color fillColor) {
        int baseColor = raster.getPixel(click.getX(), click.getY());
        int fillColorInt = fillColor.getRGB();

        if (baseColor == fillColorInt) { return; }

        nextSeeds.push(new Cords(click.getX(), click.getY()));

        while (!nextSeeds.isEmpty()) {
            processSeed(baseColor, fillColorInt);
        }

        Renderer.getInstance().rerender();
    }

    private void processSeed(int baseColor, int fillColor) {
        Cords seed = nextSeeds.pop();

        int seedX = seed.getX();
        int seedY = seed.getY();

        boolean aboveSeed = false;
        boolean belowSeed = false;

        boolean nonBaseSequenceAbove = false;
        boolean nonBaseSequenceBelow = false;

        int x = seedX;
        do { // Traverse to the right
            raster.setPixel(x, seedY, fillColor);
            colorCanvas[x][seedY] = fillColor;

            if ((!aboveSeed || nonBaseSequenceAbove) && seedY - 1 >= 0 && raster.getPixel(x, seedY - 1) == baseColor) {
                nextSeeds.push(new Cords(x, seedY - 1));
                aboveSeed = true;
                nonBaseSequenceAbove = false;
            } else if (seedY - 1 >= 0 && raster.getPixel(x, seedY - 1) != baseColor) {
                nonBaseSequenceAbove = true;
            }

            if ((!belowSeed || nonBaseSequenceBelow) && seedY + 1 < raster.getHeight() && raster.getPixel(x, seedY + 1) == baseColor) {
                nextSeeds.push(new Cords(x, seedY + 1));
                belowSeed = true;
                nonBaseSequenceBelow = false;
            } else if (seedY + 1 < raster.getHeight() && raster.getPixel(x, seedY + 1) != baseColor) {
                nonBaseSequenceBelow = true;
            }

            x++;
        } while (x < raster.getWidth() && raster.getPixel(x, seedY) == baseColor);

        x = seedX;
        do { // Traverse to the left
            raster.setPixel(x, seedY, fillColor);
            colorCanvas[x][seedY] = fillColor;

            if ((!aboveSeed || nonBaseSequenceAbove) && seedY - 1 >= 0 && raster.getPixel(x, seedY - 1) == baseColor) {
                nextSeeds.push(new Cords(x, seedY - 1));
                aboveSeed = true;
                nonBaseSequenceAbove = false;
            } else if (seedY - 1 >= 0 && raster.getPixel(x, seedY - 1) != baseColor) {
                nonBaseSequenceAbove = true;
            }

            if ((!belowSeed || nonBaseSequenceBelow) && seedY + 1 < raster.getHeight() && raster.getPixel(x, seedY + 1) == baseColor) {
                nextSeeds.push(new Cords(x, seedY + 1));
                belowSeed = true;
                nonBaseSequenceBelow = false;
            } else if (seedY + 1 < raster.getHeight() && raster.getPixel(x, seedY + 1) != baseColor) {
                nonBaseSequenceBelow = true;
            }

            x--;
        } while (x > 0 && raster.getPixel(x, seedY) == baseColor);
    }
}

class Cords {
    private int x;
    private int y;

    public Cords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return this.x; }

    public int getY() { return this.y;}
}
