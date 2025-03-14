import enums.Alignment;
import enums.LineType;
import models.Line;
import models.LineCanvas;
import models.Point;
import rasterizers.DashedLineRasterizer;
import rasterizers.DottedLineRasterizer;
import rasterizers.Rasterizer;
import rasterizers.TrivialLineRasterizer;
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
    static public int width = 800;
    static public int height = 600;

    private final JPanel panel;
    private final Raster raster;

    private MouseAdapter mouseAdapter;
    private int currentButton = MouseEvent.NOBUTTON;
    private KeyAdapter keyAdapter;

    private Point point = null;
    private Rasterizer rasterizer;
    private Rasterizer dottedRasterizer;
    private Rasterizer dashedRasterizer;
    private LineCanvas canvas;

    private Color currentColor = Color.white;
    private LineType currentMode = LineType.DEFAULT;
    private Alignment currentAlignment = Alignment.UNALIGNED;

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
        panel.repaint();
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

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        rasterizer = new TrivialLineRasterizer(raster);
        dottedRasterizer = new DottedLineRasterizer(raster);
        dashedRasterizer = new DashedLineRasterizer(raster);
        canvas = new LineCanvas();

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
                point = null;

                if (currentButton == MouseEvent.BUTTON1) {
                    point = new Point(e.getX(), e.getY());
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
                if (point != null) {
                    Point point2 = new Point(e.getX(), e.getY());
                    Line line = new Line(point, point2, currentColor, currentMode, currentAlignment);

                    raster.clear(); // Clear the canvass
                    canvas.addLine(line); // Add currently drawn line to the canvas
                    renderLines(canvas.getLines()); // Render all lines

                    panel.repaint(); // Update the canvas
                }

                currentButton = MouseEvent.NOBUTTON; // Reset currently pressed mouse button
            }

            public void mouseDragged(MouseEvent e) {
                if (point != null) {
                    Point point2 = new Point(e.getX(), e.getY());
                    Line line = new Line(point, point2, currentColor, currentMode, currentAlignment);

                    raster.clear(); // Clear the canvas
                    renderLines(new ArrayList<>(List.of(line))); // Render currently drawn line
                    renderLines(canvas.getLines()); // Render all already drawn lines

                    panel.repaint(); // Update the canvas
                }
            }
        };

        keyAdapter = new KeyAdapter() {
            ArrayList<Integer> pressedKeys = new ArrayList<>();

            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                updateMode();
                updateAlignment();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(Integer.valueOf(e.getKeyCode()));
                updateMode();
                updateAlignment();
            }

            private void updateMode() {
                if (pressedKeys.size() < 1) {
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
        };
    }

    private void renderLines(ArrayList<Line> lines) {
        for (Line line : lines) {
            switch (line.getLineType()) {
                case LineType.DEFAULT:
                    rasterizer.rasterize(line);
                    break;
                case LineType.DOTTED:
                    dottedRasterizer.rasterize(line);
                    break;
                case LineType.DASHED:
                    dashedRasterizer.rasterize(line);
                    break;
            }
        }
    }
}
