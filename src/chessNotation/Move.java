package chessNotation;

import game.board.Piece;
import game.Player;

public class Move {
    public Position start;
    public Position end;
    public Piece selectedPiece;
    public Player currentPlayer;

    public Move(int startX, int startY, int endX, int endY, Piece selectedPiece, Player currentPlayer) {
        this.start = new Position(startX, startY);
        this.end = new Position(endX, endY);
        this.selectedPiece = selectedPiece;
        this.currentPlayer = currentPlayer;
    }

    public boolean possibleMovement() {
        if (Math.min(this.end.x, this.end.y) < 0 || Math.max(this.end.x, this.end.y) > 7) {
            return false;
        }

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
     * <p>
     * The knight moves in an "L" shape: two squares in one direction (horizontally or vertically)
     * followed by one square perpendicular to that direction, or vice versa.
     * This is the only piece that can "jump" over others.
     *
     * @return true if the move is a valid knight move, false otherwise.
     */
    private boolean calculateForKnight() {
        return (Math.abs(this.start.x - this.end.x) == 2 && Math.abs(this.start.y - this.end.y) == 1) ||
                (Math.abs(this.start.x - this.end.x) == 1 && Math.abs(this.start.y - this.end.y) == 2);
    }

    /**
     * Validates a pawn's movement.
     * <p>
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

        if (this.start.x == this.end.x && this.start.y + direction == this.end.y) {
            return true;
        }

        if (this.start.x == this.end.x && this.start.y == startRow && this.start.y + 2 * direction == this.end.y) {
            return true;
        }

        return Math.abs(this.start.x - this.end.x) == 1 && this.start.y + direction == this.end.y;
    }

    /**
     * Validates a rook's movement.
     * <p>
     * Rooks can move any number of squares in a straight line, either horizontally (same row) or vertically (same column).
     *
     * @return true if the move is a valid rook move, false otherwise.
     */
    private boolean calculateForRook() {
        return this.start.x == this.end.x || this.start.y == this.end.y;
    }

    /**
     * Validates a bishop's movement.
     * <p>
     * Bishops move diagonally, meaning they must move an equal number of squares horizontally and vertically.
     *
     * @return true if the move is a valid bishop move, false otherwise.
     */
    private boolean calculateForBishop() {
        return Math.abs(this.start.x - this.end.x) == Math.abs(this.start.y - this.end.y);
    }

    /**
     * Validates a queen's movement.
     * <p>
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
     * <p>
     * The king can move one square in any direction (horizontally, vertically, or diagonally).
     *
     * @return true if the move is a valid king move, false otherwise.
     */
    private boolean calculateForKing() {
        if (this.currentPlayer == Player.One && this.start.x == 4 && this.start.y == 7 && this.end.y == 7 && (this.end.x == 0 || this.end.x == 7)) {
            return true;
        } else if (this.currentPlayer == Player.Two && this.start.x == 4 && this.start.y == 0 && this.end.y == 0 && (this.end.x == 0 || this.end.x == 7)) {
            return true;
        }

        return Math.abs(this.start.x - this.end.x) <= 1 && Math.abs(this.start.y - this.end.y) <= 1;
    }

    public boolean isCastling() {
        if (this.currentPlayer == Player.One && this.start.x == 4 && this.start.y == 7 && this.end.y == 7 && (this.end.x == 0 || this.end.x == 7)) {
            return true;
        } else {
            return this.currentPlayer == Player.Two && this.start.x == 4 && this.start.y == 0 && this.end.y == 0 && (this.end.x == 0 || this.end.x == 7);
        }
    }
}
