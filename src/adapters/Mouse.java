package adapters;

import controllers.*;
import stores.EnumStore;
import fillers.BasicFiller;
import utilities.Frame;
import models.Point;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse {
    // Singleton functions
    private static Mouse instance;

    public static Mouse getInstance() {
        if (instance == null) {
            instance = new Mouse();
        }
        return instance;
    }
    // Singleton functions end


    // Initializes mouseAdapter and creates a listener on panel
    private MouseAdapter mouseAdapter;
    private Mouse() {
        CreateMouseAdapter(); // Initialize
        Frame.getInstance().getPanel().addMouseListener(mouseAdapter); // Add click listener
        Frame.getInstance().getPanel().addMouseMotionListener(mouseAdapter); // Add drag listener
    }

    private void CreateMouseAdapter() {
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Left click
                    switch (EnumStore.getInstance().actionType) {
                        case Line:
                            LineController
                                    .getInstance()
                                    .createNewLine(e.getX(), e.getY());
                            break;
                        case Polygon:
                            PolygonController
                                    .getInstance()
                                    .createPolygon(e.getX(), e.getY());
                            break;
                        case Fill:
                            BasicFiller
                                    .getInstance()
                                    .fill(new Point(e.getX(), e.getY()));
                            break;
                        case Rectangle:
                            RectangleController
                                    .getInstance()
                                    .createRectangle(e.getX(), e.getY());
                            break;
                        case Circle:
                            CircleController
                                    .getInstance()
                                    .createCircle(e.getX(), e.getY());
                            break;
                        case Eraser:
                            EraserController
                                    .getInstance()
                                    .erase(e.getX(), e.getY());
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) { // Middle or right click
                    LineController.getInstance().MoveLineClick(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                switch (EnumStore.getInstance().actionType) {
                    case Line:
                        LineController
                                .getInstance()
                                .clearPoint();
                        break;
                    case Rectangle:
                        RectangleController
                                .getInstance()
                                .clearPoint();
                        break;
                    case Circle:
                        CircleController
                                .getInstance()
                                .clearPoint();
                        break;
                }
            }

            public void mouseDragged(MouseEvent e) {
                switch (EnumStore.getInstance().actionType) {
                    case Line:
                        LineController
                                .getInstance()
                                .MoveLineDrag(e.getX(), e.getY());
                        break;
                    case Rectangle:
                        RectangleController
                                .getInstance()
                                .createRectangle(e.getX(), e.getY());
                        break;
                    case Circle:
                        CircleController
                                .getInstance()
                                .createCircle(e.getX(), e.getY());
                        break;
                    case Eraser:
                        EraserController
                                .getInstance()
                                .erase(e.getX(), e.getY());
                }
            }
        };
    }
}
