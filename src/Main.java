import game.board.Board;
import chessNotation.ChessNotation;
import game.board.Piece;
import utils.Result;
import chessNotation.Move;
import java.util.Scanner;
import game.Player;

public class Main {
  public static Player currentPlayer = Player.One;
  public static void main(String[] args) {
    welcome();
    Board board = new Board();
    Scanner input = new Scanner(System.in);
    board.printBoard();

    while (!Board.playerWon()) {

      String userInput = input.nextLine();

      Result<Move> result = ChessNotation.getMovePosition(userInput, currentPlayer);

      if (result.isSuccess()) {
        Move move = result.getValue();
        boolean possible = move.possibleMovement();
        if(possible && board.validMove(move)) {
          if(board.getPiece(move.endX, move.endY) != null){
            //capture
            Piece movingPiece = board.getPiece(move.startX, move.startY);
            System.out.println(movingPiece.toStr());
            board.setPiece(move.endX, move.endY, movingPiece);
            board.setPiece(move.startX, move.startY, null);
          } else {
            Piece movingPiece = board.getPiece(move.startX, move.startY);
            System.out.println(movingPiece.toStr());
            board.setPiece(move.endX, move.endY, movingPiece);
            board.setPiece(move.startX, move.startY, null);
          }
          //clear console
          board.printBoard();
          if (currentPlayer == Player.One) {
            currentPlayer = Player.Two;
          } else {
            currentPlayer = Player.One;
          }
        } else {
          System.out.println("no");
        }
      } else {
        System.out.println("Error: " + result.getErrorMessage());
      }
    }
  }

  public static void welcome() {
    System.out.println("Welcome to Terminal Chess");
    System.out.println("To play a move enter the letter of the piece along with its coordinate, then enter the coordinates of the destination");
    System.out.println("K for king\nQ for queen\nR for rook\nB for bishop\nN for knight\nP for pawn");
    System.out.println("Ex: Pa2 -> a4");
  }
}
