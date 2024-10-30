package game.board;

public class Piece {
    private Color color;
    private Type type;

    public enum Type {
        King ('♚'),
        Queen ('♛'),
        Rook ('♜'),
        Bishop ('♝'),
        Knight ('♞'),
        Pawn ('♟');

        public final char character;

        Type(char character){
            this.character = character;
        }

        String getStr(Color color){
            return color.code + this.character;
        }
    }

    public Type getType() {
        return this.type;
    }

    public Color getColor() {return this.color;}

    public Piece(Color color, Type chessPiece) {
        this.color = color;
        this.type = chessPiece;
    }

    public String toStr() {
        return this.type.getStr(this.color);
    }
}
