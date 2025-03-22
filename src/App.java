import enums.*;
import fillers.BasicFiller;
import models.Line;
import models.Polygon;
import models.canvases.LineCanvas;
import models.Point;
import models.canvases.PolygonCanvas;
import rasterizers.*;
import rasters.Raster;
import rasters.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.Serial;

import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

public class App {
    static public int width = 1920;
    static public int height = 1080;

    private Renderer renderer;

    private final JPanel panel;
    private final Raster raster;

    private MouseAdapter mouseAdapter;
    private KeyAdapter keyAdapter;

    private Color currentColor = Color.white;
    private Point point = null;

    // Rasterizers
    private Rasterizer rasterizer;
    private Rasterizer dottedRasterizer;
    private Rasterizer dashedRasterizer;

    // Fillers
    private BasicFiller basicFiller;

    // Canvases
    private LineCanvas canvas;
    private PolygonCanvas polygonCanvas;

    // Enum modes
    private EnumStore enumStore = EnumStore.getInstance();

    private LineType currentMode = LineType.DEFAULT;
    private ObjectType currentObject = ObjectType.LINE;
    private Alignment currentAlignment = Alignment.UNALIGNED;
    private int currentButton = MouseEvent.NOBUTTON;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App(width, height).start());
    }

    public void clear(int color) {
        raster.setClearColor(color);
        raster.clear();
    }

    public void present(Graphics graphics) {
        raster.repaint(graphics);
    }

    public void start() {
        clear(0xaaaaaa);
        renderer.rerender();
    }

    public App(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());

        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferedImage(width, height);

        panel = new JPanel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        renderer = Renderer.getInstance(raster, panel);
        basicFiller = BasicFiller.getInstance(raster);

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        rasterizer = TrivialLineRasterizer.getInstance(raster);
        dottedRasterizer = DottedLineRasterizer.getInstance(raster);
        dashedRasterizer = DashedLineRasterizer.getInstance(raster);

        canvas = LineCanvas.getInstance();
        polygonCanvas = PolygonCanvas.getInstance();

        createAdapters();
        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
        panel.addKeyListener(keyAdapter);

        panel.requestFocus();
        panel.requestFocusInWindow();
    }

    private float getDistance(Point p, int x, int y) {
        int px = p.getX();
        int py = p.getY();

        int xDiff = Math.abs(px - x);
        int yDiff = Math.abs(py - y);

        return (float) Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
    }

    private void createAdapters() {
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentButton = e.getButton(); // Register currently pressed mouse button

                if (currentButton == MouseEvent.BUTTON1) {
                    switch (enumStore.getObjectType()) {
                        case LINE:
                            point = new Point(e.getX(), e.getY());
                            break;
                        case POLYGON:
                            if (point == null) { // If there is no point created, create one and register polygon
                                point = new Point(e.getX(), e.getY());
                                polygonCanvas.addPolygon(new Polygon(point, currentColor, currentMode));
                            } else { // Otherwise add current point to the polygon and rerender
                                point = new Point(e.getX(), e.getY());
                                polygonCanvas.editLastPolygon(point);
                                renderer.rerender();
                            }
                            break;
                        case FILL:
                            basicFiller.fill(new Point(e.getX(), e.getY()), currentColor);
                            renderer.rerender();
                            panel.repaint();
                            break;
                    }
                } else if (currentButton == MouseEvent.BUTTON3 || currentButton == MouseEvent.BUTTON2) {
                    int lineIndex = -1;
                    Point otherPoint = null;
                    float closestDistance = Float.MAX_VALUE;

                    int index = 0;
                    for (Line line : canvas.getLines()) {
                        float distanceP1 = getDistance(line.getPoint1(), e.getX(), e.getY());
                        float distanceP2 = getDistance(line.getPoint2(), e.getX(), e.getY());

                        if (distanceP1 < closestDistance) {
                            otherPoint = line.getPoint2();
                            closestDistance = distanceP1;
                            lineIndex = index;
                        }

                        if (distanceP2 < closestDistance) {
                            otherPoint = line.getPoint1();
                            closestDistance = distanceP2;
                            lineIndex = index;
                        }

                        index++;
                    }

                    if (closestDistance <= 30) {
                        point = new Point(otherPoint.getX(), otherPoint.getY());
                        canvas.removeLineAt(lineIndex);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (point != null && enumStore.getObjectType() == ObjectType.LINE) {
                    Point point2 = new Point(e.getX(), e.getY());
                    Line line = new Line(point, point2, currentColor, currentMode, currentAlignment);

                    canvas.addLine(line); // Add currently drawn line to the canvas
                    renderer.rerender();
                }

                currentButton = MouseEvent.NOBUTTON; // Reset currently pressed mouse button
            }

            public void mouseDragged(MouseEvent e) {
                if (point != null && enumStore.getObjectType() == ObjectType.LINE) {
                    Point point2 = new Point(e.getX(), e.getY());
                    Line line = new Line(point, point2, currentColor, currentMode, currentAlignment);

                    renderer.rerender();
                    renderer.renderLines(new ArrayList<>(List.of(line))); // Render currently drawn line
                    panel.repaint(); // Update the canvas
                }
            }
        };

        keyAdapter = new KeyAdapter() {
            ArrayList<Integer> pressedKeys = new ArrayList<>();

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    raster.clear();
                    canvas.clearLines();
                    polygonCanvas.clearPolygons();
                    panel.repaint();
                }

                pressedKeys.add(e.getKeyCode());
                update();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(Integer.valueOf(e.getKeyCode()));
                update();
            }

            private void update() {
                updateMode();
                updateAlignment();
                updateObject();
            }

            private void updateMode() {
                if (pressedKeys.isEmpty()) {
                    currentMode = LineType.DEFAULT;
                    return;
                }

                switch (pressedKeys.getFirst()) {
                    case KeyEvent.VK_CONTROL:
                        currentMode = LineType.DOTTED;
                        break;
                    case KeyEvent.VK_ALT:
                        currentMode = LineType.DASHED;
                        break;
                    default:
                        currentMode = LineType.DEFAULT;
                }
            }

            private void updateAlignment() {
                if (pressedKeys.contains(KeyEvent.VK_SHIFT)) {
                    currentAlignment = Alignment.ALIGNED;
                } else {
                    currentAlignment = Alignment.UNALIGNED;
                }
            }

            private void updateObject() {
                if (pressedKeys.contains(KeyEvent.VK_E)) {
                    point = null; // Reset point for purpose of creating polygons
                    enumStore.moveEnum(Enums.ActionType);
                    renderer.rerender();
                }
            }
        };
    }
}
