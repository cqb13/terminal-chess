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

        Type(char blackChar, char whiteChar){
            this.whiteChar = whiteChar;
            this.blackChar = blackChar;
        }

        String getStr(Color color){
//            if(color == Color.White)
                return color.code + this.whiteChar;
//            else
//                return color.code + this.blackChar;
        }

        //TODO: define function to get piece
    }

    public Piece(Color color, Type chessPiece) {
        this.color = color;
        this.type = chessPiece;
    }

    public String toStr() {
        return this.type.getStr(this.color);
    }
}
