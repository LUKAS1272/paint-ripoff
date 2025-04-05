import adapters.Keyboard;
import adapters.Mouse;
import fillers.BasicFiller;
import models.canvases.LineCanvas;
import models.canvases.PolygonCanvas;
import rasterizers.*;
import utilities.Frame;
import utilities.Renderer;

public class App {
    public static void main(String[] args) {
        // Must be initialized first, because it stores RASTER and PANEL
        Frame.getInstance(); // Initializes raster and panel variable, creates app frame

        // Adds listeners to add dynamic controls
        Mouse.getInstance();
        Keyboard.getInstance();

        // Creates the first render of frame on start
        Renderer.getInstance().rerender();
    }
}
