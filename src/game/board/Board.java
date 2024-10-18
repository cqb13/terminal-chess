package game.board;

import chessNotation.Move;

public class Board {
    private final Square[][] board;

    public Board() {
        Square[][] board = new Square[8][8];
        int boardSize = 8;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = new Square();
            }
            if (i == 0) {
                for (int j = 0; j < boardSize; j++) {
                    if (j == 0 || j == 7) {
                        board[i][j].setPiece(new Piece(Color.Black, Piece.Type.Rook));
                    } else if (j == 1 || j == 6) {
                        board[i][j].setPiece(new Piece(Color.Black, Piece.Type.Knight));
                    } else if (j == 2 || j == 5) {
                        board[i][j].setPiece(new Piece(Color.Black, Piece.Type.Bishop));
                    } else if (j == 3) {
                        board[i][j].setPiece(new Piece(Color.Black, Piece.Type.Queen));
                    } else {
                        board[i][j].setPiece(new Piece(Color.Black, Piece.Type.King));
                    }
                }
            } else if (i == 1) {
                for (int j = 0; j < boardSize; j++) {
                    board[i][j].setPiece(new Piece(Color.Black, Piece.Type.Pawn));
                }
            } else if (i == 6) {
                for (int j = 0; j < boardSize; j++) {
                    board[i][j].setPiece(new Piece(Color.White, Piece.Type.Pawn));
                }
            } else if (i == 7) {
                for (int j = 0; j < boardSize; j++) {
                    if (j == 0 || j == 7) {
                        board[i][j].setPiece(new Piece(Color.White, Piece.Type.Rook));
                    } else if (j == 1 || j == 6) {
                        board[i][j].setPiece(new Piece(Color.White, Piece.Type.Knight));
                    } else if (j == 2 || j == 5) {
                        board[i][j].setPiece(new Piece(Color.White, Piece.Type.Bishop));
                    } else if (j == 3) {
                        board[i][j].setPiece(new Piece(Color.White, Piece.Type.Queen));
                    } else {
                        board[i][j].setPiece(new Piece(Color.White, Piece.Type.King));
                    }
                }
            }
        }
        this.board = board;
    }

    public static boolean playerWon() {
        return false;
    }

    public Piece getPiece(int x, int y) {
        return this.board[y][x].piece;
    }

    public void setPiece(int x, int y, Piece piece) {
        this.board[y][x].setPiece(piece);
    }

    public boolean validMove(Move move) {
        Piece destinationPiece = this.board[move.endY][move.endX].piece; //todo: broken (see Pa2 -> a4)
        if (destinationPiece == null) {
            return false;
        }
        switch (move.selectedPiece.getType()){
            case King:
                //TODO: make sure the king not move into a check
                return this.board[move.endY][move.endX].piece == null || this.board[move.endY][move.endX].piece.color != move.selectedPiece.color;
            case Pawn:
                if(move.endX == move.startX)
                    return true; //todo
                else
                    return destinationPiece != null && destinationPiece.color != move.selectedPiece.color;
            case Rook:
                return (destinationPiece == null || destinationPiece.color != move.selectedPiece.color) && piecesBetweenSquares(move.startX, move.startY, move.endX, move.endY);
            case Queen:
            case Bishop:
            case Knight:
                return this.board[move.endY][move.endX].piece == null || this.board[move.endY][move.endX].piece.color != move.selectedPiece.color;
            default:
                return false;
        }
    }

    private boolean piecesBetweenSquares(int startX, int startY, int endX, int endY){
        if(startX == endX && startY != endY){
            //vertical
            int start = Math.min(startY, endY) + 1;
            int end = Math.max(startY, endY);

            for (int y = start; y < end; y++) {
                if (this.board[y][startX].piece != null) {
                    return false;
                }
            }
        } else if(startX != endX && startY == endY){
            //horizontal
            int start = Math.min(startX, endX) + 1;
            int end = Math.max(startX, endX);
            System.out.println(end + " " + start);
            for (int x = start; x < end; x++) {
                if (this.board[startY][x].piece != null) {
                    System.out.println("piece in the way at " + x + ", " + startY);
                    return false;
                }
            }
        } else if(Math.abs(startX - endX) == Math.abs(startY - endY)){
            //diagonal
            int start = Math.min(startX, endX) + 1;
            int end = Math.max(startX, endX);

            for(int i = start; i < end; i++){
                if(this.board[i][i].piece != null) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public void printBoard()
    {
        final String BLACK_BG = "\u001B[42m";
        final String WHITE_BG = "\u001B[103m";
        final String RESET = "\u001B[0m";
        for(int i = 0; i < 4; i++){
            System.out.print(8 - i * 2 + " ");
            for(int j = 0; j < 4; j++){
                System.out.print(WHITE_BG + ' ' + this.board[i * 2][j * 2].toStr() + ' ' + BLACK_BG + ' ' + this.board[i * 2][j * 2 + 1].toStr() + ' ');
            }
            System.out.println(RESET);
            System.out.print(7 - i * 2 + " ");
            for(int j = 0; j < 4; j++){
                System.out.print(BLACK_BG + ' ' + this.board[i * 2 + 1][j * 2].toStr() + ' ' + WHITE_BG + ' ' + this.board[i * 2 + 1][j * 2 + 1].toStr() + ' ');
            }
            System.out.println(RESET);
        }
        System.out.println("   a  b  c  d  e  f  g  h");
    }
}
