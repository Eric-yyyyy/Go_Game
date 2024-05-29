package AI;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

import Main.ChessFrame;


public class AIPanel extends JPanel {
    public Player humanPlayer;
    public Player AIPlayer;
    private ChessFrame chessFrame;
    public boolean defaultAI = false;

    public AIPanel(ChessFrame frame) {
        this.chessFrame = frame;

        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel playerLabel = new JLabel("玩家", SwingConstants.CENTER);
        JLabel aiLabel = new JLabel("AI", SwingConstants.CENTER);

        JComboBox<String> colorChoice = new JComboBox<>(new String[]{"黑","白", "猜先"});
        JButton startGameButton = new JButton("开始");

        startGameButton.addActionListener(e -> startAIGame(colorChoice.getSelectedIndex()));

        add(playerLabel);
        add(aiLabel);
        add(colorChoice);
        add(new JLabel(""));
        add(startGameButton);
        add(new JLabel(""));
    }

    private void startAIGame(int choiceIndex) {

         humanPlayer = new Player();
         AIPlayer = new Player();
         defaultAI = true;
        Random random = new Random();
        if (choiceIndex == 0) {
            humanPlayer.setMoveNumber(1);
            AIPlayer.setMoveNumber(2);
        } else if (choiceIndex == 1) {
            humanPlayer.setMoveNumber(2);
            AIPlayer.setMoveNumber(1);
        }else{
            if (random.nextBoolean()) {
                humanPlayer.setMoveNumber(1);
                AIPlayer.setMoveNumber(2);
            } else {
                humanPlayer.setMoveNumber(2);
                AIPlayer.setMoveNumber(1);
            }

        }


        chessFrame.showAIGame();
    }
}
