package chessNotation;

import game.board.Piece.Type;
import game.Player;

public class Move {
  public int originX;
  public int originY;
  public int endX;
  public int endY;
  public Type selectedPiece;
  public Player currentPlayer;

  public Move(int originX, int originY, int endX, int endY, Type selectedPiece, Player currentPlayer) {
    this.originX = originX;
    this.originY = originY;
    this.endX = endX;
    this.endY = endY;
    this.selectedPiece = selectedPiece;
    this.currentPlayer = currentPlayer;
  }

  public boolean possibleMovement() {
      return switch (this.selectedPiece) {
          case Type.Knight -> calculateForKnight();
          case Type.Pawn -> calculateForPawn();
          case Type.Rook -> calculateForRook();
          case Type.Bishop -> calculateForBishop();
          case Type.Queen -> calculateForQueen();
          case Type.King -> calculateForKing();
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
    return (Math.abs(this.originX - this.endX) == 2 && Math.abs(this.originY - this.endY) == 1) ||
            (Math.abs(this.originX - this.endX) == 1 && Math.abs(this.originY - this.endY) == 2);
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

    if (this.originX == this.endX && this.originY + direction == this.endY) {
      return true;
    }

    if (this.originX == this.endX && this.originY == startRow && this.originY  + 2 * direction == this.endY) {
      return true;
    }

    return Math.abs(this.originX - this.endX) == 1 && this.originY + direction == this.endY;
  }

  /**
   * Validates a rook's movement.
   *
   * Rooks can move any number of squares in a straight line, either horizontally (same row) or vertically (same column).
   *
   * @return true if the move is a valid rook move, false otherwise.
   */
  private boolean calculateForRook() {
    return this.originX == this.endX || this.originY == this.endY;
  }

  /**
   * Validates a bishop's movement.
   *
   * Bishops move diagonally, meaning they must move an equal number of squares horizontally and vertically.
   *
   * @return true if the move is a valid bishop move, false otherwise.
   */
  private boolean calculateForBishop() {
    return Math.abs(this.originX - this.endX) == Math.abs(this.originY - this.endY);
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
    return Math.abs(this.originX - this.endX) <= 1 && Math.abs(this.originY - this.endY) <= 1;
  }
}
