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
                        break;
                    case Edit:
                        EditController
                                .getInstance()
                                .edit(e.getX(), e.getY());
                        break;
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
                    case Edit:
                        EditController
                                .getInstance()
                                .clear();
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
                        break;
                    case Edit:
                        EditController
                                .getInstance()
                                .editAction(e.getX(), e.getY());
                        break;
                }
            }
        };
    }
}
