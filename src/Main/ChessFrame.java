package Main;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import AI.AIPanel;

public class ChessFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;
    private MenuPanel menuPanel;

    private AIPanel aiPanel;

    private ControlPanel controlPanel;

    public ChessFrame(){
        setTitle("围棋游戏");
        setSize(800, 800);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        aiPanel = new AIPanel(this);

        gamePanel = new GamePanel(this,aiPanel);
        gamePanel.setBackground(Color.ORANGE);
        menuPanel = new MenuPanel(this);

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> gamePanel.undoMove());

        controlPanel = new ControlPanel(
                () -> gamePanel.undoMove(),
                () -> saveGame()
        );


        JPanel gameWrapperPanel = new JPanel(new BorderLayout());
        gameWrapperPanel.add(gamePanel, BorderLayout.CENTER);
        gameWrapperPanel.add(controlPanel, BorderLayout.SOUTH);


        mainPanel.add(menuPanel, "MENU_PANEL");
        mainPanel.add(gameWrapperPanel, "GAME_PANEL");
        mainPanel.add(aiPanel,"AI_PANEL");
        add(mainPanel);

        setVisible(true);
        pack();
        setSize(800,800);
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "MENU_PANEL");
    }

    public void showGame() {
        gamePanel.resetGame();
        aiPanel.defaultAI = false;
        cardLayout.show(mainPanel, "GAME_PANEL");
    }
    public void showAIGame() {
        gamePanel.resetGame();
        cardLayout.show(mainPanel, "GAME_PANEL");
    }
    public void showSavedGame(){
        cardLayout.show(mainPanel, "GAME_PANEL");
    }


    public void loadAndShowGame() {
        Map<Point, Piece> loadedStonesState = new HashMap<>();


        try (BufferedReader reader = new BufferedReader(new FileReader("savedGame.txt"))) {
            String line;
           int defaultnumber = 1;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Stone:")) {
                    String[] parts = line.split(", ");

                    String[] coordinates = parts[0].split(":")[1].trim().split(",");
                    int x = Integer.parseInt(coordinates[0].trim());
                    int y = Integer.parseInt(coordinates[1].trim());

                    String color = parts[1].split(":")[1].trim();
                    int moveNumber = Integer.parseInt(parts[2].split(":")[1].trim());
                    if(moveNumber <= defaultnumber){
                        moveNumber = defaultnumber;
                    }
                    loadedStonesState.put(new Point(x, y), new Piece(Piece.Color.valueOf(color), moveNumber));
                }

            }

            gamePanel.setStonesState(loadedStonesState);
            showSavedGame();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading the game!");
        }
    }




    private void saveGame() {
        String filename = "savedGame.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("TotalMoves: " + gamePanel.getMoveNumber());
            writer.newLine();

            for(Map.Entry<Point, Piece> entry : gamePanel.getStonesState().entrySet()) {
                Point stonePoint = entry.getKey();
                Piece stonePiece = entry.getValue();
                writer.write("Stone: " + stonePoint.x + "," + stonePoint.y +
                        ", Color: " + stonePiece.getColor() +
                        ", MoveNumber: " + stonePiece.getMoveNumber());
                writer.newLine();
            }

            System.out.println("Game saved successfully!");

            showMenu();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving the game!");
        }
        System.out.println("Saving the game...");
    }
    public void showAIGamePanel() {
        cardLayout.show(mainPanel, "AI_PANEL");
    }



}
