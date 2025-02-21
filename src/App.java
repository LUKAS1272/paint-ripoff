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
import java.util.Arrays;
import java.util.List;

public class App {

    private final JPanel panel;
    private final Raster raster;

    private MouseAdapter mouseAdapter;
    private KeyAdapter keyAdapter;

    private Point point;
    private Rasterizer rasterizer;
    private Rasterizer dottedRasterizer;
    private Rasterizer dashedRasterizer;
    private LineCanvas canvas;

    private Color currentColor = Color.white;
    private boolean ctrlMode = false;
    private LineType currentMode = LineType.DEFAULT;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App(800, 600).start());
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

    private void createAdapters() {
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                point = new Point(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point point2 = new Point(e.getX(), e.getY());
                Line line = new Line(point, point2, currentColor, currentMode);

                raster.clear(); // Clear the canvass
                canvas.addLine(line); // Add currently drawn line to the canvas
                renderLines(canvas.getLines()); // Render all lines

                panel.repaint(); // Update the canvas
            }

            public void mouseDragged(MouseEvent e) {
                Point point2 = new Point(e.getX(), e.getY());
                Line line = new Line(point, point2, currentColor, currentMode);

                raster.clear(); // Clear the canvas
                renderLines(new ArrayList<>(List.of(line))); // Render currently drawn line
                renderLines(canvas.getLines()); // Render all already drawn lines

                panel.repaint(); // Update the canvas
            }
        };

        keyAdapter = new KeyAdapter() {
            ArrayList<Integer> pressedKeys = new ArrayList<Integer>();

            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                updateMode();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(Integer.valueOf(e.getKeyCode()));
                updateMode();
            }

            private void updateMode() {
                if (pressedKeys.size() != 1) {
                    currentMode = LineType.DEFAULT;
                    return;
                }

                switch (pressedKeys.getFirst()) {
                    case KeyEvent.VK_CONTROL:
                        currentMode = LineType.DOTTED;
                        break;
                    case KeyEvent.VK_SHIFT:
                        currentMode = LineType.DASHED;
                        break;
                    default:
                        currentMode = LineType.DEFAULT;
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
