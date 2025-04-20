package utilities;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private static GUI instance;

    private GUI() {}

    public static GUI getInstance() {
        if (instance == null) {
            instance = new GUI();
        }
        return instance;
    }

    // -----------------------------------------

    public JPanel crateLabel(String labelText) {
        JPanel labelPanel = new JPanel(); // Create a container for label
        labelPanel.setOpaque(false); // Disable background
        labelPanel.setMaximumSize(new Dimension(1920, 25));
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center the text

        JLabel label = new JLabel(labelText); // Create a label
        label.setFont(new Font("Arial", Font.PLAIN, 15)); // Set font
        label.setForeground(Color.WHITE); // Set text color

        labelPanel.add(label);
        return labelPanel;
    }

    public JButton createButton(String labelText, boolean active) {
        JButton button = new JButton(labelText);
        button.setBackground(active ? Color.yellow : Color.white);
        button.setBorderPainted(false);

        return button;
    }

    public JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setMaximumSize(new Dimension(1920, 1080));
        panel.setOpaque(false);

        return panel;
    }
}
