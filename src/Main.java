import game.Game;

public class Main {
  public static void main(String[] args) {
    welcome();
    String[] testState = {
            "rnbqkbnr",
            "pppppppp",
            "        ",
            "        ",
            "    p   ",
            "        ",
            "PPPPPPPP",
            "RNBQKBNR"};
    Game game = new Game(testState);
    game.display();

    while (!game.playerWon()) {
      game.takeTurn();
    }


  }

  public static void welcome() {
    System.out.println("Welcome to Terminal Chess");
    System.out.println("To play a move enter the letter of the piece along with its coordinate, then enter the coordinates of the destination");
    System.out.println("K for king\nQ for queen\nR for rook\nB for bishop\nN for knight\nP for pawn");
    System.out.println("Ex: Pa2 -> a4");
  }
}
