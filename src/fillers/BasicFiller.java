package fillers;

import models.Point;
import rasters.Raster;
import stores.EnumStore;
import utilities.Frame;
import utilities.Renderer;

import java.util.Stack;

public class BasicFiller {
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

    public int getBackgroundPixelColor(int x, int y) { // Gets the background color of given pixel
        return colorCanvas[x][y] == 0 ? Frame.getInstance().getBackgroundColor() : colorCanvas[x][y];
    }

    public void setBackgroundPixelColor(int x, int y, int color) { // Sets the background color of given pixel
        colorCanvas[x][y] = color;
    }

    public void clearColorCanvas() { colorCanvas = new int[raster.getWidth()][raster.getHeight()]; } // Clears the whole background color canvas

    public void fill(Point click) {
        int baseColor = raster.getPixel(click.getX(), click.getY()); // Gets the color of the starting pixel
        int fillColorInt = EnumStore.getInstance().getDrawColor().getRGB(); // Gets the color that will be filled with

        if (baseColor == fillColorInt) { return; } // If the base color and fill color are the same, terminate the filling

        nextSeeds.push(new Cords(click.getX(), click.getY())); // Add the starting pixel as the first seed

        while (!nextSeeds.isEmpty()) { // Unless there are no seeds, process next seed
            processSeed(baseColor, fillColorInt);
        }

        Renderer.getInstance().rerender(); // Rerender the frame
    }

    private void processSeed(int baseColor, int fillColor) {
        Cords seed = nextSeeds.pop(); // Get the seed that is being processed

        // Get seed's cords
        int seedX = seed.getX();
        int seedY = seed.getY();

        // Variables to track, whether there has been a seed put for the above / below line
        boolean aboveSeed = false;
        boolean belowSeed = false;

        // Variables to track, whether there is currently a non-base sequence of pixels above / below
        // Helps to prevent filling errors
        boolean nonBaseSequenceAbove = false;
        boolean nonBaseSequenceBelow = false;

        int x = seedX;
        do { // Traverse to the right
            // Set the current pixel to the fill color
            raster.setPixel(x, seedY, fillColor);
            colorCanvas[x][seedY] = fillColor;

            // Check to add seed to the line above
            // If there is not an above seed set yet OR there is a sequence of non-base color
            // AND the seed's Y axis is within the frame
            // AND the seed's pixel's color is baseColor
            if ((!aboveSeed || nonBaseSequenceAbove) && seedY - 1 >= 0 && raster.getPixel(x, seedY - 1) == baseColor) {
                nextSeeds.push(new Cords(x, seedY - 1)); // Add the pixel above to a queue
                aboveSeed = true;
                nonBaseSequenceAbove = false; // Reset the variable
            } else if (seedY - 1 >= 0 && raster.getPixel(x, seedY - 1) != baseColor) { // If the seed's Y axis is within the frame AND the seed's pixel's color is NOT baseColor
                nonBaseSequenceAbove = true; // There is a sequence of non-base color pixels
            }

            // Check to add seed to the line below
            // If there is not a below seed set yet OR there is a sequence of non-base color
            // AND the seed's Y axis is within the frame
            // AND the seed's pixel's color is baseColor
            if ((!belowSeed || nonBaseSequenceBelow) && seedY + 1 < raster.getHeight() && raster.getPixel(x, seedY + 1) == baseColor) {
                nextSeeds.push(new Cords(x, seedY + 1)); // Add the pixel below to a queue
                belowSeed = true;
                nonBaseSequenceBelow = false; // Reset the variable
            } else if (seedY + 1 < raster.getHeight() && raster.getPixel(x, seedY + 1) != baseColor) { // If the seed's Y axis is within the frame AND the seed's pixel's color is NOT baseColor
                nonBaseSequenceBelow = true; // There is a sequence of non-base color pixels
            }

            x++; // Move next pixel to the right
        } while (x < raster.getWidth() && raster.getPixel(x, seedY) == baseColor); // Iterate while the x is within frame range AND current pixel is of base color

        x = seedX;
        do { // Traverse to the left
            // Set the current pixel to the fill color
            raster.setPixel(x, seedY, fillColor);
            colorCanvas[x][seedY] = fillColor;

            // Check to add seed to the line above
            // If there is not an above seed set yet OR there is a sequence of non-base color
            // AND the seed's Y axis is within the frame
            // AND the seed's pixel's color is baseColor
            if ((!aboveSeed || nonBaseSequenceAbove) && seedY - 1 >= 0 && raster.getPixel(x, seedY - 1) == baseColor) {
                nextSeeds.push(new Cords(x, seedY - 1)); // Add the pixel above to a queue
                aboveSeed = true;
                nonBaseSequenceAbove = false; // Reset the variable
            } else if (seedY - 1 >= 0 && raster.getPixel(x, seedY - 1) != baseColor) { // If the seed's Y axis is within the frame AND the seed's pixel's color is NOT baseColor
                nonBaseSequenceAbove = true; // There is a sequence of non-base color pixels
            }

            // Check to add seed to the line below
            // If there is not a below seed set yet OR there is a sequence of non-base color
            // AND the seed's Y axis is within the frame
            // AND the seed's pixel's color is baseColor
            if ((!belowSeed || nonBaseSequenceBelow) && seedY + 1 < raster.getHeight() && raster.getPixel(x, seedY + 1) == baseColor) {
                nextSeeds.push(new Cords(x, seedY + 1)); // Add the pixel below to a queue
                belowSeed = true;
                nonBaseSequenceBelow = false; // Reset the variable
            } else if (seedY + 1 < raster.getHeight() && raster.getPixel(x, seedY + 1) != baseColor) { // If the seed's Y axis is within the frame AND the seed's pixel's color is NOT baseColor
                nonBaseSequenceBelow = true; // There is a sequence of non-base color pixels
            }

            x--; // Move next pixel to the left
        } while (x > 0 && raster.getPixel(x, seedY) == baseColor); // Iterate while the x is within frame range AND current pixel is of base color
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

    public int getY() { return this.y; }
}
