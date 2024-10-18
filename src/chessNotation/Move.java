package chessNotation;

import game.board.Piece;
import game.Player;

public class Move {
  public int startY;
  public int startX;
  public int endX;
  public int endY;
  public Piece selectedPiece;
  public Player currentPlayer;

  public Move(int startX, int startY, int endX, int endY, Piece selectedPiece, Player currentPlayer) {
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    this.selectedPiece = selectedPiece;
    this.currentPlayer = currentPlayer;
  }

  public boolean possibleMovement() {
      return switch (this.selectedPiece.getType()) {
          case Piece.Type.Pawn -> calculateForPawn();
          case Piece.Type.Rook -> calculateForRook();
          case Piece.Type.Bishop -> calculateForBishop();
          case Piece.Type.Queen -> calculateForQueen();
          case Piece.Type.King -> calculateForKing();
          case Piece.Type.Knight -> calculateForKnight();
      };
  }

  /**
   * Validates a knight's movement.
   *
   * The knight moves in an "L" shape: two squares in one direction (horizontally or vertically)
   * followed by one square perpendicular to that direction, or vice versa.
   * This is the only piece that can "jump" over others.
   *
   * @return true if the move is a valid knight move, false otherwise.
   */
  private boolean calculateForKnight() {
    return (Math.abs(this.startX - this.endX) == 2 && Math.abs(this.startY - this.endY) == 1) ||
            (Math.abs(this.startX - this.endX) == 1 && Math.abs(this.startY - this.endY) == 2);
  }

  /**
   * Validates a pawn's movement.
   *
   * Pawns can:
   * - Move forward one square if the destination is empty.
   * - Move forward two squares from their starting row (row 1 for Player One, row 6 for Player Two).
   * - Capture diagonally one square forward.
   *
   * @return true if the move is a valid pawn move, false otherwise.
   */
  private boolean calculateForPawn() {
    int direction = (this.currentPlayer == Player.One) ? -1 : 1; // Player One moves up, Player Two moves down
    int startRow = (this.currentPlayer == Player.One) ? 6 : 1;

    if (this.startX == this.endX && this.startY + direction == this.endY) {
      return true;
    }

    if (this.startX == this.endX && this.startY == startRow && this.startY + 2 * direction == this.endY) {
      return true;
    }

    return Math.abs(this.startX - this.endX) == 1 && this.startY + direction == this.endY;
  }

  /**
   * Validates a rook's movement.
   *
   * Rooks can move any number of squares in a straight line, either horizontally (same row) or vertically (same column).
   *
   * @return true if the move is a valid rook move, false otherwise.
   */
  private boolean calculateForRook() {
    return this.startX == this.endX || this.startY == this.endY;
  }

  /**
   * Validates a bishop's movement.
   *
   * Bishops move diagonally, meaning they must move an equal number of squares horizontally and vertically.
   *
   * @return true if the move is a valid bishop move, false otherwise.
   */
  private boolean calculateForBishop() {
    return Math.abs(this.startX - this.endX) == Math.abs(this.startY - this.endY);
  }

  /**
   * Validates a queen's movement.
   *
   * The queen combines the movement of a rook and a bishop, meaning it can move either
   * in straight lines (like a rook) or diagonally (like a bishop).
   *
   * @return true if the move is a valid queen move, false otherwise.
   */
  private boolean calculateForQueen() {
    return calculateForRook() || calculateForBishop();
  }

  /**
   * Validates a king's movement.
   *
   * The king can move one square in any direction (horizontally, vertically, or diagonally).
   *
   * @return true if the move is a valid king move, false otherwise.
   */
  private boolean calculateForKing() {
    return Math.abs(this.startX - this.endX) <= 1 && Math.abs(this.startY - this.endY) <= 1;
  }
}
