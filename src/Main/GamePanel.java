package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.*;
import java.util.List;
import AI.Player;
import AI.AIPanel;

class GamePanel extends JPanel {
    private static final int GRID_SIZE = 19;
    //private Stack<Point> moveHistory = new Stack<>();
    private Stack<MoveSnapshot> moveHistory = new Stack<>();
    private static final int CELL_SIZE = 30; // 单元格大小
    private static final int MARGIN = 100; // 边距
    private static final int PIECE_DIAMETER = 20; // 棋子直径
    private static final int STAR_DIAMETER = 8;
    private Piece[][] board = new Piece[GRID_SIZE][GRID_SIZE];
    private boolean isBlackTurn = true;  // 黑子先行

    private int currentMoveNumber = 1;
    private BoarderAnalyze boarderAnalyze;
    private KO ko = new KO();
    private AIPanel aiPanel;
    private Point lastMove = null;
    private Point currentMousePosition = null;

    public GamePanel(ChessFrame chessFrame, AIPanel aiPanel) {
        boarderAnalyze = new BoarderAnalyze(board, GRID_SIZE);
        this.aiPanel = aiPanel;

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = (e.getX() - MARGIN + CELL_SIZE / 2) / CELL_SIZE;
                int y = (e.getY() - MARGIN + CELL_SIZE / 2) / CELL_SIZE;
                if (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE) {
                    currentMousePosition = new Point(x, y);
                } else {
                    currentMousePosition = null;
                }
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = Math.round((float)(e.getX() - MARGIN) / CELL_SIZE);
                int y = Math.round((float)(e.getY() - MARGIN) / CELL_SIZE);

                boolean allOpponentAdjacent = true;
                boolean canCapture = false;
                boolean canselfCapture = false;



                Piece.Color opponentColor = isBlackTurn ? Piece.Color.WHITE : Piece.Color.BLACK;
                Piece.Color currentColor = isBlackTurn ? Piece.Color.BLACK : Piece.Color.WHITE;

                Piece.Color TempColor = opponentColor;

                int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                List<Point> potentialcapturedPieces = new ArrayList<>();
                for (int[] dir : directions) {
                    int newX = x + dir[0];
                    int newY = y + dir[1];

                    if (boarderAnalyze.isValid(newX, newY) && (board[newY][newX] == null || board[newY][newX].getColor() == currentColor)) {
                        allOpponentAdjacent = false;
                    }

                }



                if ((allOpponentAdjacent && x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE && board[y][x] == null) || canCapture){

                        moveHistory.push(new MoveSnapshot(new Point(x, y), board, currentMoveNumber));
                        board[y][x] = new Piece(isBlackTurn ? Piece.Color.BLACK : Piece.Color.WHITE, currentMoveNumber);
                        lastMove = new Point(x, y);

                        List<Point> capturedPieces = new ArrayList<>();
                        for (int[] dir : directions) {
                            int newX = x + dir[0];
                            int newY = y + dir[1];
                            if (boarderAnalyze.isValid(newX, newY) && board[newY][newX] != null
                                    && board[newY][newX].getColor() == opponentColor
                                    && board[y][x] != null) {
                                capturedPieces.addAll(boarderAnalyze.getCapturedPieces(newX, newY));
                            }
                        }
                        List<Point> selfCapture = new ArrayList<>();
                        for (int[] dir : directions) {
                            int newX = x + dir[0];
                            int newY = y + dir[1];
                            if (boarderAnalyze.isValid(newX, newY) && board[newY][newX] != null
                                    && board[newY][newX].getColor() == currentColor
                                    && board[y][x] != null) {
                                selfCapture.addAll(boarderAnalyze.getCapturedPieces(newX, newY));
                            }
                        }
                    System.out.println("cancapture " + canCapture);
                        if(!capturedPieces.isEmpty()){

                            canCapture = true;
                            System.out.println("cancapture " + canCapture);
                        }
                        if(!selfCapture.isEmpty()){
                            canselfCapture = true;
                        }
                        if(!ko.isKoViolation(new Point(x,y)) && canCapture){
//                            undoMove();
                            for (Point p : capturedPieces) {
                                System.out.println("yes");
                                board[p.y][p.x] = null;
                                SoundEffect.playLuoZiSound("/Assets/CaptureMusic.wav");
                            }
                            if (capturedPieces.size() == 1) {
                                ko.setLastCapturedSingleStone(capturedPieces.get(0));
                            } else {
                                ko.reset();
                            }
                            currentMoveNumber++;

                            isBlackTurn = !isBlackTurn;
                            if (aiPanel.defaultAI) {

                                if ((isBlackTurn && aiPanel.AIPlayer.getMoveNumber() % 2 == 1) || (!isBlackTurn && aiPanel.AIPlayer.getMoveNumber() % 2 == 0)) {
                                    makeAIMove();
                                }
                            }
                            SoundEffect.playLuoZiSound("/Assets/PlaySound.wav");
                        }else {
                            undoMove();
                            isBlackTurn = !isBlackTurn;
                        }
                        repaint();
                }else if(!allOpponentAdjacent && x>=0 && x<GRID_SIZE && y>=0 && y<GRID_SIZE && board[y][x] == null){
                    if(!ko.isKoViolation(new Point(x,y))){
                        moveHistory.push(new MoveSnapshot(new Point(x, y), board, currentMoveNumber));
                        board[y][x] = new Piece(isBlackTurn ? Piece.Color.BLACK : Piece.Color.WHITE, currentMoveNumber);
                        lastMove = new Point(x, y);


                        List<Point> capturedPieces = new ArrayList<>();
                        for (int[] dir : directions) {
                            int newX = x + dir[0];
                            int newY = y + dir[1];
                            if (boarderAnalyze.isValid(newX, newY) && board[newY][newX] != null
                                    && board[newY][newX].getColor() == opponentColor
                                    && board[y][x] != null) {

                                capturedPieces.addAll(boarderAnalyze.getCapturedPieces(newX, newY));
                            }
                            if(!capturedPieces.isEmpty()){
                                canCapture = true;
                            }
                            System.out.println(capturedPieces);
                        }
                        List<Point> selfCapture = new ArrayList<>();
                        for (int[] dir : directions) {
                            int newX = x + dir[0];
                            int newY = y + dir[1];
                            if (boarderAnalyze.isValid(newX, newY) && board[newY][newX] != null
                                    && board[newY][newX].getColor() == currentColor
                                    && board[y][x] != null) {
                                selfCapture.addAll(boarderAnalyze.getCapturedPieces(newX, newY));
                            }
                        }
                        if(!selfCapture.isEmpty()){
                            canselfCapture = true;
                        }
                        if(!canselfCapture || canCapture ){
                            for (Point p : capturedPieces) {
                                System.out.println("yes2");
                                board[p.y][p.x] = null;
                                SoundEffect.playLuoZiSound("/Assets/CaptureMusic.wav");
                            }
                            if (capturedPieces.size() == 1) {
                                ko.setLastCapturedSingleStone(capturedPieces.get(0));
                            } else {
                                ko.reset();
                            }
                            currentMoveNumber++;
                            //moveHistory.push(new Point(x, y));

                            isBlackTurn = !isBlackTurn;
                            if (aiPanel.defaultAI) {
                                // If it's AI's turn now, make a move
                                if ((isBlackTurn && aiPanel.AIPlayer.getMoveNumber() % 2 == 1) || (!isBlackTurn && aiPanel.AIPlayer.getMoveNumber() % 2 == 0)) {
                                    makeAIMove();
                                }
                            }
                            SoundEffect.playLuoZiSound("/Assets/PlaySound.wav");
                        }else{
                            undoMove();
                            isBlackTurn = !isBlackTurn;
                        }

                        repaint();

                    }
                }
            }
        });
    }
    private Piece[][] cloneBoard(Piece[][] original) {
        Piece[][] copy = new Piece[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
    public void undoMove() {
        if (!moveHistory.isEmpty()) {
            MoveSnapshot lastMoveSnapshot = moveHistory.pop();
            board = deepCopyBoard(lastMoveSnapshot.boardState);
            boarderAnalyze = new BoarderAnalyze(board, GRID_SIZE);
            currentMoveNumber = lastMoveSnapshot.moveNumber;
            lastMove = lastMoveSnapshot.move;
            isBlackTurn = !isBlackTurn;
            if (!moveHistory.isEmpty()) {
                lastMove = moveHistory.peek().move;
            } else {
                lastMove = null;
            }
            repaint();

        }
    }
    private Piece[][] deepCopyBoard(Piece[][] original) {
        Piece[][] copy = new Piece[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                if (original[i][j] != null) {
                    copy[i][j] = new Piece(original[i][j].getColor(), original[i][j].getMoveNumber());
                }
            }
        }
        return copy;
    }
    public int getMoveNumber() {
        return currentMoveNumber;
    }

    public Map<Point, Piece> getStonesState() {
        Map<Point, Piece> stoneStates = new HashMap<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (board[i][j] != null) {
                    stoneStates.put(new Point(j, i), board[i][j]);
                }
            }
        }
        return stoneStates;
    }
    public void setStonesState(Map<Point, Piece> stonesState) {
        Point maxMovePoint = null;
        int maxMoveNumber = 0;

        for (Map.Entry<Point, Piece> entry : stonesState.entrySet()) {
            Point point = entry.getKey();
            Piece piece = entry.getValue();
            board[point.y][point.x] = piece;

            if (piece.getMoveNumber() > maxMoveNumber) {

                maxMoveNumber = piece.getMoveNumber();
                maxMovePoint = point;
            }
        }
        // Update
        this.currentMoveNumber = maxMoveNumber + 1;

        this.lastMove = maxMovePoint;

        repaint();
    }
    public void resetGame() {
        // Reset the board
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                board[i][j] = null;
            }
        }

        isBlackTurn = true;
        currentMoveNumber = 1;
        ko.reset();
        moveHistory.clear();
        lastMove = null;

        repaint();
    }
    private void makeAIMove() {
        Random rand = new Random();
        int aiX, aiY;

        do {
            aiX = rand.nextInt(GRID_SIZE);
            aiY = rand.nextInt(GRID_SIZE);
        } while (board[aiY][aiX] != null);

        board[aiY][aiX] = new Piece(isBlackTurn ? Piece.Color.BLACK : Piece.Color.WHITE, currentMoveNumber);
        lastMove = new Point(aiX, aiY);

        currentMoveNumber++;
        isBlackTurn = !isBlackTurn;
        repaint();
    }
    @Override

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;  // 将Graphics对象转换为Graphics2D对象
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // 启用抗锯齿功能
        if (aiPanel.defaultAI && aiPanel.AIPlayer.getMoveNumber() == 1 && currentMoveNumber == 1) {
            // If AI plays black and it's the start of the game, AI makes the first move
            makeAIMove();

        }
        // Draw the board lines
        for (int i = 0; i < GRID_SIZE; i++) {
            g2d.drawLine(MARGIN, MARGIN + i * CELL_SIZE, MARGIN + CELL_SIZE * (GRID_SIZE - 1), MARGIN + i * CELL_SIZE);
            g2d.drawLine(MARGIN + i * CELL_SIZE, MARGIN, MARGIN + i * CELL_SIZE, MARGIN + CELL_SIZE * (GRID_SIZE - 1));
        }

        // Draw the star at (4,4)
        if (currentMousePosition != null) {
            int highlightRadius = PIECE_DIAMETER / 2 ; // Size of the glow effect
            int x = MARGIN + currentMousePosition.x * CELL_SIZE;
            int y = MARGIN + currentMousePosition.y * CELL_SIZE;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            int alphaStep = 15;
            for (int i = 0; i < 5; i++) {
                int alpha = 120 - (alphaStep * i);
                if (alpha < 0) alpha = 0;
                if(isBlackTurn){
                    g2.setColor(new Color(0, 0, 0, alpha));
                }else{
                    g2.setColor(new Color(255, 255, 255, alpha));
                }

                g2.fillOval(x - highlightRadius + i * 2, y - highlightRadius + i * 2, 2 * (highlightRadius - i * 2), 2 * (highlightRadius - i * 2));
            }
            g2.dispose();
        }
        g2d.setColor(Color.BLACK);
        g2d.fillOval(MARGIN + 3 * CELL_SIZE - STAR_DIAMETER / 2,
                MARGIN + 3 * CELL_SIZE - STAR_DIAMETER / 2,
                STAR_DIAMETER, STAR_DIAMETER);

        g2d.fillOval(MARGIN + 9 * CELL_SIZE - STAR_DIAMETER / 2,
                MARGIN + 3 * CELL_SIZE - STAR_DIAMETER / 2,
                STAR_DIAMETER, STAR_DIAMETER);

        g2d.fillOval(MARGIN + 15 * CELL_SIZE - STAR_DIAMETER / 2,
                MARGIN + 3 * CELL_SIZE - STAR_DIAMETER / 2,
                STAR_DIAMETER, STAR_DIAMETER);

        g2d.fillOval(MARGIN + 3 * CELL_SIZE - STAR_DIAMETER / 2,
                MARGIN + 9 * CELL_SIZE - STAR_DIAMETER / 2,
                STAR_DIAMETER, STAR_DIAMETER);

        g2d.fillOval(MARGIN + 9 * CELL_SIZE - STAR_DIAMETER / 2,
                MARGIN + 9 * CELL_SIZE - STAR_DIAMETER / 2,
                STAR_DIAMETER, STAR_DIAMETER);

        g2d.fillOval(MARGIN + 15 * CELL_SIZE - STAR_DIAMETER / 2,
                MARGIN + 9 * CELL_SIZE - STAR_DIAMETER / 2,
                STAR_DIAMETER, STAR_DIAMETER);

        g2d.fillOval(MARGIN + 3 * CELL_SIZE - STAR_DIAMETER / 2,
                MARGIN + 15 * CELL_SIZE - STAR_DIAMETER / 2,
                STAR_DIAMETER, STAR_DIAMETER);

        g2d.fillOval(MARGIN + 9 * CELL_SIZE - STAR_DIAMETER / 2,
                MARGIN + 15 * CELL_SIZE - STAR_DIAMETER / 2,
                STAR_DIAMETER, STAR_DIAMETER);

        g2d.fillOval(MARGIN + 15 * CELL_SIZE - STAR_DIAMETER / 2,
                MARGIN + 15 * CELL_SIZE - STAR_DIAMETER / 2,
                STAR_DIAMETER, STAR_DIAMETER);

        // Draw the pieces
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (board[i][j] != null) {
                    g2d.setColor(board[i][j].getColor() == Piece.Color.BLACK ? Color.BLACK : Color.WHITE);
                    g2d.fillOval(MARGIN + j * CELL_SIZE - PIECE_DIAMETER / 2,
                            MARGIN + i * CELL_SIZE - PIECE_DIAMETER / 2,
                            PIECE_DIAMETER, PIECE_DIAMETER);
                    String moveNumStr = String.valueOf(board[i][j].getMoveNumber());
                    FontMetrics fm = g2d.getFontMetrics();
                    int moveNumWidth = fm.stringWidth(moveNumStr);
                    int moveNumHeight = fm.getAscent();

                    g2d.setColor(board[i][j].getColor() == Piece.Color.BLACK ? Color.WHITE : Color.BLACK);

                    int moveNumX = MARGIN + j * CELL_SIZE - moveNumWidth / 2;
                    int moveNumY = MARGIN + i * CELL_SIZE + moveNumHeight / 2 - PIECE_DIAMETER / 8;

                    g2d.drawString(moveNumStr,
                            moveNumX,
                            moveNumY);
                    if (lastMove != null && board[lastMove.y][lastMove.x] != null) {
                        g2d.setColor(Color.YELLOW);
                        g2d.setStroke(new BasicStroke(2));  // Adjust for desired circle thickness
                        int radius = PIECE_DIAMETER / 2 + 1;  // Adjust the "+3" for the desired distance from the stone
                        g2d.drawOval(MARGIN + lastMove.x * CELL_SIZE - radius,
                                MARGIN + lastMove.y * CELL_SIZE - radius,
                                2 * radius,
                                2 * radius);
                        g2d.setStroke(new BasicStroke(1));  // Reset to default
                    }

                }
            }
        }
    }
}

