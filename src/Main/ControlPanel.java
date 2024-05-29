package Main;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private JButton undoButton;
    private JButton saveButton;

    public ControlPanel(Runnable onUndo, Runnable onSave) {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> onUndo.run());
        add(undoButton);

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> onSave.run());
        add(saveButton);

        setPreferredSize(new Dimension(800, 50));
    }
}
