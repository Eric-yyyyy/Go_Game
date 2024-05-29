package Main;

import java.awt.*;
import java.util.List;

import java.util.ArrayList;
import java.util.Collections;

public class BoarderAnalyze {

    private final Piece[][] board;
    private final int GRID_SIZE;
    private boolean[][] visited;
    private Piece.Color currentColor;

    public BoarderAnalyze(Piece[][] board, int gridSize) {
        this.board = board;
        this.GRID_SIZE = gridSize;
    }

    public boolean isValid(int x, int y) {
        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
    }


    public List<Point> getCapturedPieces(int x, int y) {
        if (board[y][x] == null) {
            return Collections.emptyList();
        }
        currentColor = board[y][x].getColor();
        visited = new boolean[GRID_SIZE][GRID_SIZE];
        List<Point> group = new ArrayList<>();
        boolean isCaptured = dfs(x, y, group);

        if (isCaptured) {
            return group;
        } else {
            return Collections.emptyList();
        }
    }
    private boolean dfs(int x, int y, List<Point> group) {
        if (!isValid(x, y) || visited[y][x]) {
            return true;
        }

        if (board[y][x] == null) {
            return false;
        }

        if (board[y][x].getColor() != currentColor) {
            return true;
        }

        visited[y][x] = true;
        group.add(new Point(x, y));

        boolean top = dfs(x, y - 1, group);
        boolean bottom = dfs(x, y + 1, group);
        boolean left = dfs(x - 1, y, group);
        boolean right = dfs(x + 1, y, group);

        return top && bottom && left && right;
    }
}
