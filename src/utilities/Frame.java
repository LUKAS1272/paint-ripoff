package utilities;

import enums.ActionType;
import enums.Alignment;
import enums.DrawColor;
import enums.LineType;
import rasters.Raster;
import rasters.RasterBuffer;
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

        frame.setTitle("MS paint ripoff");
        frame.setResizable(false);
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

        createAndShowGUI(); // Renders the GUI
    }

    // --------------------------------------------------

    private void createAndShowGUI() {
        // Create sidebar
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(250, height));
        panel.setBackground(Color.DARK_GRAY);


        panel.add(GUI.getInstance().crateLabel("Color")); // Color label
        JPanel colors = GUI.getInstance().createPanel(); // Color choice panel

        for (DrawColor color : DrawColor.values()) { // Create a button for every color in enum
            JButton colorPanel = GUI.getInstance().createButton(color.toString(), EnumStore.getInstance().drawColor == color);

            colorPanel.addActionListener(e -> { // When clicked, set drawColor to this button's color and rerender UI
               EnumStore.getInstance().drawColor = color;
               panel.removeAll();
               createAndShowGUI();
            });

            colors.add(colorPanel); // Add button to the color choice panel
        }
        panel.add(colors); // Add color choice panel to the UI


        panel.add(GUI.getInstance().crateLabel("Tools")); // Tools label
        JPanel tools = GUI.getInstance().createPanel(); // Tool choice panel

        for (ActionType actionType : ActionType.values()) { // Create a button for every tool in enum
            JButton colorPanel = GUI.getInstance().createButton(actionType.toString(), EnumStore.getInstance().actionType == actionType);

            colorPanel.addActionListener(e -> { // When clicked, set actionType to this button's action and rerender UI
                EnumStore.getInstance().actionType = actionType;
                panel.removeAll();
                createAndShowGUI();
            });

            tools.add(colorPanel); // Add button to the tool choice panel
        }
        panel.add(tools); // Add tool choice panel to the UI


        panel.add(GUI.getInstance().crateLabel("Thickness: " + StateStore.getInstance().getThickness())); // Create a label showing the current thickness
        JPanel thicknessPanel = GUI.getInstance().createPanel(); // Create thickness panel

        JButton minus = GUI.getInstance().createButton("Decrease", false); // Create a button for decreasing the thickness
        minus.addActionListener(e -> { // When clicked, decrease the thickness and rerender UI
           StateStore.getInstance().decreaseThickness();
           panel.removeAll();
           createAndShowGUI();
        });
        thicknessPanel.add(minus); // Add button to the thickness panel

        JButton plus = GUI.getInstance().createButton("Increase", false); // Create a button for increasing the thickness
        plus.addActionListener(e -> { // When clicked, increase the thickness and rerender UI
            StateStore.getInstance().increaseThickness();
            panel.removeAll();
            createAndShowGUI();
        });
        thicknessPanel.add(plus); // Add button to the thickness panel

        panel.add(thicknessPanel); // Add buttons panel to the UI


        panel.add(GUI.getInstance().crateLabel("Line type")); // Line type label
        JPanel lineTypes = GUI.getInstance().createPanel(); // Line type choice panel

        for (LineType lineType : LineType.values()) { // Create a button for every line type in enum
            JButton lineButton = GUI.getInstance().createButton(lineType.toString(), EnumStore.getInstance().lineType == lineType);

            lineButton.addActionListener(e -> { // When clicked, set lineType to this button's type and rerender UI
                EnumStore.getInstance().lineType = lineType;
                panel.removeAll();
                createAndShowGUI();
            });

            lineTypes.add(lineButton); // Add button to the line type choice panel
        }
        panel.add(lineTypes); // Add line type choice panel to the UI


        panel.add(GUI.getInstance().crateLabel("Alignment")); // Alignment label
        JPanel alignments = GUI.getInstance().createPanel(); // Alignment choice panel

        for (Alignment alignment : Alignment.values()) { // Create a button for every alignment in enum
            JButton alignmentButton = GUI.getInstance().createButton(alignment.toString(), EnumStore.getInstance().alignment == alignment);

            alignmentButton.addActionListener(e -> { // When clicked, set alignment to this button's alignment and rerender UI
                EnumStore.getInstance().alignment = alignment;
                panel.removeAll();
                createAndShowGUI();
            });

            alignments.add(alignmentButton); // Add button to the alignment choice panel
        }
        panel.add(alignments); // Add alignment choice panel to the UI


        panel.add(GUI.getInstance().crateLabel("Clear the canvas")); // Clear label
        JPanel clearPanel = GUI.getInstance().createPanel(); // Clear panel

        JButton clearButton = GUI.getInstance().createButton("Clear", false); // Create a clear button
        clearButton.addActionListener(e -> { // When clicked, clear the canvas and rerender UI
            RasterBuffer.getInstance().clearCanvas();
            panel.removeAll();
            createAndShowGUI();
        });

        clearPanel.add(clearButton); // Add button to the clear panel
        panel.add(clearPanel); // Add clear panel to the UI


        frame.add(panel, BorderLayout.EAST); // Add sidebar to the right side of the frame
        frame.setVisible(true); // Enable UI's visibility
    }

    public JPanel getPanel() { return panel; }
    public Raster getRaster() { return raster; }
    public int getBackgroundColor() { return backgroundColor; }
}
