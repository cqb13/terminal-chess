package board;

public class Board {
    public Square[][] board;
    int boardSize = 8;

    public Board(){
        Square[][] board = new Square[8][8];
        for (int i = 0; i < this.boardSize; i++) {
            if (i == 0) {
                // Black main piece
            } else if (i == 1) {

            } else if (i == 6) {

            } else if (i == 7) {

            } else {

            }
        }
        this.board = board;
    }
    public void printBoard()
    {
        final String BLACK_BG = "\u001B[40m";
        final String WHITE_FG = "\u001B[37m";
        final String WHITE_BG = "\u001B[47m";
        final String BLACK_FG = "\u001B[30m";
        final String RESET = "\u001B[0m";
        //TODO: this.board[][] is not a char yet
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                System.out.print(WHITE_BG + BLACK_FG + this.board[i * 2][j * 2].piece.toStr() + BLACK_BG + WHITE_FG + this.board[i * 2][j * 2 + 1].piece.toStr());
            }
            System.out.println(RESET);
            for(int j = 0; j < 4; j++){
                System.out.print(BLACK_BG + WHITE_FG + this.board[i * 2 + 1][j * 2].piece.toStr() + WHITE_BG + BLACK_FG + this.board[i * 2 + 1][j * 2 + 1].piece.toStr());
            }
            System.out.println(RESET);
        }
    }
}
