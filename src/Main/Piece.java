package Main;

public class Piece {
    enum Color {
        BLACK, WHITE
    }

    private Color color;
    private int MoveNumber;

    public Piece(Color color,int MoveNumber) {
        this.color = color;
        this.MoveNumber = MoveNumber;
    }

    public Color getColor() {
        return color;
    }
    public int getMoveNumber() {
        return MoveNumber;
    }
}
