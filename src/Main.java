import game.Game;
import game.GameEnding;

public class Main {
  public static void main(String[] args) {
    welcome();
    String[] testState = {
            "k       ",
            " R      ",
            "        ",
            " K     Q",
            "        ",
            "        ",
            "        ",
            "        "};
    Game game = new Game(testState);
    game.display(false);

    GameEnding endingState = GameEnding.None;

    while (endingState == GameEnding.None) {
      endingState = game.takeTurn();

      switch (endingState) {
        case GameEnding.WhiteWon:
          System.out.println("White won the game!");
          break;
        case GameEnding.BlackWon:
          System.out.println("Black won the game!");
          break;
        case GameEnding.Draw:
          System.out.println("The game ending in a draw!");
          break;
        case GameEnding.None:
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
