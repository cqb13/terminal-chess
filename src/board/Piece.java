package board;

public class Piece {
    public Color color;
    public Type type;

    public enum Type {
        King ('♔', '♚'),
        Queen ('♕', '♛'),
        Rook ('♖', '♜'),
        Bishop ('♗', '♝'),
        Knight ('♘', '♞'),
        Pawn ('♙', '♟');

        public final char whiteChar;
        public final char blackChar;

        Type(char whiteChar, char blackChar){
            this.whiteChar = whiteChar;
            this.blackChar = blackChar;
        }

        char getChar(Color color){
            if(color == Color.White)
                return this.whiteChar;
            else
                return this.blackChar;
        }

        //TODO: define function to get piece
    }

    public Piece(Color color, Type chessPiece) {
        this.color = color;
        this.type = chessPiece;
    }

    public char toStr() {
        return this.type.getChar(this.color);
    }
}
