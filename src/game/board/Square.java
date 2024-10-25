package game.board;

public class Square {
  public Piece piece;

  public Square() {
    this.piece = null;
  }

  public void setPiece(Piece piece) {
    this.piece = piece;
  }

  public boolean hasPiece() {
      return this.piece != null;
  }

  public String toStr() {
    if (this.piece == null)
      return " ";
    else
      return this.piece.toStr();
  }
}
