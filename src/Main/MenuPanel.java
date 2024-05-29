package Main;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private ChessFrame chessFrame;
    public MenuPanel(ChessFrame chessFrame){
        this.chessFrame = chessFrame;
        initialize();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon background = new ImageIcon(getClass().getResource("/Assets/GoImage.jpg"));
        Image img = background.getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("GoGame");
        titleLabel.setFont(new Font("Times New Roman", Font.ITALIC, 24));

        buttonsPanel.add(titleLabel);

        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 90)));

        JButton newGameBtn = new JButton("开始新游戏");
        newGameBtn.addActionListener(e -> chessFrame.showGame());

        JButton continueGameBtn = new JButton("继续游戏");

        continueGameBtn.addActionListener(e -> chessFrame.loadAndShowGame());

        JButton exitBtn = new JButton("退出");
        exitBtn.addActionListener(e -> System.exit(0));

        JButton aiGameBtn = new JButton("人机对弈");
        aiGameBtn.addActionListener(e -> chessFrame.showAIGamePanel());

        buttonsPanel.add(newGameBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        buttonsPanel.add(continueGameBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        buttonsPanel.add(exitBtn);

        //buttonsPanel.add(aiGameBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        add(buttonsPanel, gbc);
    }


}
