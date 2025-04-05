package utilities;

import rasters.Raster;
import rasters.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class Frame {
    private final int width = 1920 / 2;
    private final int height = 1080;
    private final int backgroundColor = 0x000000;

    private static Frame instance;

    private Raster raster;
    private JPanel panel;

    private Frame() {
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
                raster.repaint(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        raster.setClearColor(backgroundColor);
        raster.clear();
        panel.repaint();
    }

    public static Frame getInstance() {
        if (instance == null) {
            instance = new Frame();
        }
        return instance;
    }

    public JPanel getPanel() { return panel; }
    public Raster getRaster() { return raster; }
    public int getBackgroundColor() { return backgroundColor; }
}
