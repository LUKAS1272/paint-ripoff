package utilities;

import enums.ActionType;
import enums.Alignment;
import enums.DrawColor;
import enums.LineType;
import rasters.Raster;
import rasters.RasterBufferedImage;
import stores.EnumStore;
import stores.StateStore;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class Frame {
    private static Frame instance;

    public static Frame getInstance() {
        if (instance == null) {
            instance = new Frame();
        }
        return instance;
    }

    private final int width = 1920;
    private final int height = 1080;
    private final int backgroundColor = 0x000000;

    private Raster raster;
    private JPanel panel;
    private JFrame frame;

    private Frame() {
        frame = new JFrame();
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

        createAndShowGUI();
    }

    // --------------------------------------------------

    private void createAndShowGUI() {
        // Sidebar
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(width / 6, height));
        panel.setBackground(Color.DARK_GRAY);


        panel.add(GUI.getInstance().crateLabel("Color")); // Color label
        JPanel colors = GUI.getInstance().createPanel(); // Color buttons panel

        // Color buttons
        for (DrawColor color : DrawColor.values()) {
            JButton colorPanel = GUI.getInstance().createButton(color.toString(), EnumStore.getInstance().drawColor == color);

            colorPanel.addActionListener(e -> {
               EnumStore.getInstance().drawColor = color;
               panel.removeAll();
               createAndShowGUI();
            });

            colors.add(colorPanel);
        }
        panel.add(colors);


        panel.add(GUI.getInstance().crateLabel("Tools")); // Tools label
        JPanel tools = GUI.getInstance().createPanel(); // Tools panel

        for (ActionType actionType : ActionType.values()) {
            JButton colorPanel = GUI.getInstance().createButton(actionType.toString(), EnumStore.getInstance().actionType == actionType);

            colorPanel.addActionListener(e -> {
                EnumStore.getInstance().actionType = actionType;
                panel.removeAll();
                createAndShowGUI();
            });

            tools.add(colorPanel);
        }
        panel.add(tools);


        panel.add(GUI.getInstance().crateLabel("Thickness: " + StateStore.getInstance().getThickness()));
        JPanel thicknessPanel = GUI.getInstance().createPanel();

        JButton minus = GUI.getInstance().createButton("Decrease", false);
        minus.addActionListener(e -> {
           StateStore.getInstance().decreaseThickness();
           panel.removeAll();
           createAndShowGUI();
        });
        thicknessPanel.add(minus);

        JButton plus = GUI.getInstance().createButton("Increase", false);
        plus.addActionListener(e -> {
            StateStore.getInstance().increaseThickness();
            panel.removeAll();
            createAndShowGUI();
        });
        thicknessPanel.add(plus);

        panel.add(thicknessPanel);


        panel.add(GUI.getInstance().crateLabel("Line type"));
        JPanel lineTypes = GUI.getInstance().createPanel();

        for (LineType lineType : LineType.values()) {
            JButton lineButton = GUI.getInstance().createButton(lineType.toString(), EnumStore.getInstance().lineType == lineType);

            lineButton.addActionListener(e -> {
                EnumStore.getInstance().lineType = lineType;
                panel.removeAll();
                createAndShowGUI();
            });

            lineTypes.add(lineButton);
        }
        panel.add(lineTypes);


        panel.add(GUI.getInstance().crateLabel("Alignment"));
        JPanel alignments = GUI.getInstance().createPanel();

        for (Alignment alignment : Alignment.values()) {
            JButton alignmentButton = GUI.getInstance().createButton(alignment.toString(), EnumStore.getInstance().alignment == alignment);

            alignmentButton.addActionListener(e -> {
                EnumStore.getInstance().alignment = alignment;
                panel.removeAll();
                createAndShowGUI();
            });

            alignments.add(alignmentButton);
        }
        panel.add(alignments);


        frame.add(panel, BorderLayout.EAST);
        frame.setVisible(true);
    }

    public JPanel getPanel() { return panel; }
    public Raster getRaster() { return raster; }
    public int getBackgroundColor() { return backgroundColor; }
}
