package AI;
import Main.Piece;





public class Player {
    private int moveNumber;

    public Player() {
        this.moveNumber = 0;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public void incrementMoveNumber() {
        this.moveNumber++;
    }
}

