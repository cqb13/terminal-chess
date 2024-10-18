package chessNotation;

import game.Player;
import game.board.Color;
import game.board.Piece;
import utils.Result;
public class ChessNotation {
  /**
   * Our notation format
   * move from move to
   * [piece][letter][number] -> [letter][number]
   *
   * valid pieces:
   *  - K for king
   *  - Q for queen
   *  - R for rook
   *  - B for bishop
   *  - N for knight
   *  - P for pawn
   *
   * valid letters:
   *  - a...h = 0...7
   *
   * valid numbers:
   *  - 1...8 = 0...7
   */
  public static Result<Move> getMovePosition(String input, Player currentPlayer) {
    String[] inputParts = input.split("->");

    if (inputParts.length != 2) {
      return Result.error("Invalid input: must be in the format '[piece][letter][number] (Ke1) -> [letter][number] (e2)'.");
    }

    if (inputParts[0].trim().length() != 3) {
      return Result.error("Invalid input: first input must be [piece][letter][number] (Ke1).");
    }

    if (inputParts[1].trim().length() != 2) {
      return Result.error("Invalid input: second input must be [letter][number] (e2).");
    }

    int originX = findXPosition(inputParts[0].trim().split("")[1]);
    int destinationX = findXPosition(inputParts[1].trim().split("")[0]);

    if (originX == -1 || destinationX == -1) {
      return Result.error("Invalid input: column must be between 'a' and 'h'.");
    }

    int originY = 8 - parseRow(inputParts[0].trim().split("")[2]);
    int destinationY = 8 - parseRow(inputParts[1].trim().split("")[1]);

    if (originY == -1 || destinationY == -1) {
      return Result.error("Invalid input: row must be between 1 and 8.");
    }

    Piece selectedPiece = determinePiece(inputParts[0].trim().split("")[0], currentPlayer.color);

    if (selectedPiece == null) {
      return Result.error("Invalid input: K for king, Q for queen, R for rook, B for bishop, N for knight, P for pawn");
    }

    return Result.success(new Move(originX, originY, destinationX, destinationY, selectedPiece, currentPlayer));
  }

  private static Piece determinePiece(String value, Color color) {
    return switch (value) {
      case "K" -> new Piece(color, Piece.Type.King);
      case "Q" -> new Piece(color, Piece.Type.Queen);
      case "R" -> new Piece(color, Piece.Type.Rook);
      case "B" -> new Piece(color, Piece.Type.Bishop);
      case "N" -> new Piece(color, Piece.Type.Knight);
      case "P" -> new Piece(color, Piece.Type.Pawn);
      default -> null;
    };
  }

  private static int findXPosition(String value) {
    return switch (value) {
      case "a" -> 0;
      case "b" -> 1;
      case "c" -> 2;
      case "d" -> 3;
      case "e" -> 4;
      case "f" -> 5;
      case "g" -> 6;
      case "h" -> 7;
      default -> -1;
    };
  }

  private static int parseRow(String value) {
    try {
      int row = Integer.parseInt(value);
      if (row >= 1 && row <= 8) {
        return row;
      }
    } catch (NumberFormatException e) {
      return -1;
    }
    return -1;
  }
}
