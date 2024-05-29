package Main;

import java.awt.*;

class MoveSnapshot {
    Point move;
    Piece[][] boardState;
    int moveNumber;

    public MoveSnapshot(Point move, Piece[][] boardState, int moveNumber) {
        this.move = move;
        this.boardState = deepCopyBoard(boardState);
        this.moveNumber = moveNumber;
    }

    private Piece[][] deepCopyBoard(Piece[][] original) {
        Piece[][] copy = new Piece[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
}
