package board;

import java.util.Optional;

public class Square {
    public Piece piece;

    public Square() {

    }

    public void setPiece(){
        this.piece = null;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    public char toStr(){
        if(this.piece == null)
            return ' ';
        else
            return this.piece.toStr();
    }
}
