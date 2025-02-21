import models.Line;
import models.LineCanvas;
import models.Point;
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

public class App {

    private final JPanel panel;
    private final Raster raster;

    private MouseAdapter mouseAdapter;
    private KeyAdapter keyAdapter;

    private Point point;
    private Rasterizer rasterizer;
    private Rasterizer dottedRasterizer;
    private LineCanvas canvas;

    private Color currentColor = Color.white;
    private boolean ctrlMode = false;

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

                Line line = new Line(point, point2, currentColor, ctrlMode);

                raster.clear();
                canvas.addLine(line);

                for (Line lineToRender : canvas.getLines()) {
                    if (lineToRender.getIsDotted()) { dottedRasterizer.rasterize(lineToRender); }
                    else { rasterizer.rasterize(lineToRender); }
                }

                panel.repaint();
            }

            public void mouseDragged(MouseEvent e) {
                Point point2 = new Point(e.getX(), e.getY());

                Line line = new Line(point, point2, currentColor, ctrlMode);

                raster.clear();

                for (Line lineToRender : canvas.getLines()) {
                    if (lineToRender.getIsDotted()) { dottedRasterizer.rasterize(lineToRender); }
                    else { rasterizer.rasterize(lineToRender); }
                }

                if (line.getIsDotted()) { dottedRasterizer.rasterize(line); }
                else { rasterizer.rasterize(line); }

                panel.repaint();
            }
        };

        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    ctrlMode = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    ctrlMode = false;
                }
            }
        };
    }
}
