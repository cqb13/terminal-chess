package game.board;

public class Board {
    public Square[][] board;
    int boardSize = 8;
    int selectedX;
    int selectedY;

    public Board(){
        Square[][] board = new Square[8][8];
        for (int i = 0; i < this.boardSize; i++) {
            for(int j = 0; j < this.boardSize; j++){
                board[i][j] = new Square();
            }
            if (i == 0) {
                for(int j = 0; j < this.boardSize; j++) {
                    if(j == 0 || j == 7) {
                        board[i][j].setPiece(new Piece(Color.Black, Piece.Type.Rook));
                    } else if(j == 1 || j == 6) {
                        board[i][j].setPiece(new Piece(Color.Black, Piece.Type.Knight));
                    } else if(j == 2 || j == 5) {
                        board[i][j].setPiece(new Piece(Color.Black, Piece.Type.Bishop));
                    } else if(j == 3) {
                        board[i][j].setPiece(new Piece(Color.Black, Piece.Type.Queen));
                    } else {
                        board[i][j].setPiece(new Piece(Color.Black, Piece.Type.King));
                    }
                }
            } else if (i == 1) {
                for(int j = 0; j < this.boardSize; j++){
//                    game.board[i][j] = new Square();
                    board[i][j].setPiece(new Piece(Color.Black, Piece.Type.Pawn));
                }
            } else if (i == 6) {
                for(int j = 0; j < this.boardSize; j++){
//                    game.board[i][j] = new Square();
                    board[i][j].setPiece(new Piece(Color.White, Piece.Type.Pawn));
                }
            } else if (i == 7) {
                for(int j = 0; j < this.boardSize; j++) {
                    if(j == 0 || j == 7) {
                        board[i][j].setPiece(new Piece(Color.White, Piece.Type.Rook));
                    } else if(j == 1 || j == 6) {
                        board[i][j].setPiece(new Piece(Color.White, Piece.Type.Knight));
                    } else if(j == 2 || j == 5) {
                        board[i][j].setPiece(new Piece(Color.White, Piece.Type.Bishop));
                    } else if(j == 3) {
                        board[i][j].setPiece(new Piece(Color.White, Piece.Type.Queen));
                    } else {
                        board[i][j].setPiece(new Piece(Color.White, Piece.Type.King));
                    }
                }
            } else {

            }
        }
        this.board = board;
    }
    public void printBoard()
    {
//        final String BLACK_BG = "\u001B[100m";
//        final String WHITE_BG = "\u001B[47m";
        final String BLACK_BG = "\u001B[42m";
        final String WHITE_BG = "\u001B[103m";
        final String RESET = "\u001B[0m";
        //TODO: this.game.board[][] is not a char yet
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                System.out.print(WHITE_BG + ' ' + this.board[i * 2][j * 2].toStr() + ' ' + BLACK_BG + ' ' + this.board[i * 2][j * 2 + 1].toStr() + ' ');
            }
            System.out.println(RESET);
            for(int j = 0; j < 4; j++){
                System.out.print(BLACK_BG + ' ' + this.board[i * 2 + 1][j * 2].toStr() + ' ' + WHITE_BG + ' ' + this.board[i * 2 + 1][j * 2 + 1].toStr() + ' ');
            }
            System.out.println(RESET);
        }
    }
}
