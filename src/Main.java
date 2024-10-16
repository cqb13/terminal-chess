import game.board.Board;
import chessNotation.ChessNotation;
import utils.Result;
import chessNotation.Move;
import java.util.Scanner;
import game.Player;

public class Main {
  public static Player currentPlayer = Player.One;

  public static void main(String[] args) {
    Board board = new Board();

    board.printBoard();

    Scanner input = new Scanner(System.in);

    String userInput = input.nextLine();

    Result<Move> result = ChessNotation.getMovePosition(userInput, currentPlayer);

    if (result.isSuccess()) {
      Move move = result.getValue();
      boolean possible = move.possibleMovement();
      System.out.println(possible);
    } else {
      System.out.println("Error: " + result.getErrorMessage());
    }
  }
}
