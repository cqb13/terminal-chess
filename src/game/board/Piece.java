package game.board;

public class Piece {
    public Color color;
    public Type type;

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

    public Piece(Color color, Type chessPiece) {
        this.color = color;
        this.type = chessPiece;
    }

    public String toStr() {
        return this.type.getStr(this.color);
    }
}
