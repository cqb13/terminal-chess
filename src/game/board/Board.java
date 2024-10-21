package game.board;

import chessNotation.Move;
import game.Player;

public class Board {
    private final Square[][] board;
    public int whiteKingX;
    public int whiteKingY;
    public int blackKingX;
    public int blackKingY;

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

    public boolean playerWon() {
        return false;
    }

    public Piece getPiece(int x, int y) {
        return this.board[y][x].piece;
    }

    public void setPiece(int x, int y, Piece piece) {
        this.board[y][x].setPiece(piece);
    }

    public void updateKingPosition(int x, int y, Player player) {
        switch (player) {
            case One:
                this.whiteKingX = x;
                this.whiteKingY = y;
            case Two:
                this.blackKingX = x;
                this.blackKingY = y;
        }
    }

    public boolean validMove(Move move) {
        Piece destinationPiece = this.board[move.endY][move.endX].piece;
        Piece targetPiece = this.board[move.startY][move.startX].piece;
        if (targetPiece == null || targetPiece.color != move.currentPlayer.color) {
            return false;
        }
        switch (move.selectedPiece.getType()){
            case King:
                return (destinationPiece == null || destinationPiece.color != targetPiece.color) && !isCheck(move.currentPlayer, move.endX, move.endY);
            case Pawn:
                if(move.endX == move.startX) {
                    return destinationPiece == null && piecesBetweenSquares(move.startX, move.startY, move.endX, move.endY);
                } else {
                    return destinationPiece != null && destinationPiece.color != targetPiece.color;
                }
            case Rook:
            case Queen:
            case Bishop:
                return (destinationPiece == null || destinationPiece.color != targetPiece.color) && piecesBetweenSquares(move.startX, move.startY, move.endX, move.endY);
            case Knight:
                return destinationPiece == null || destinationPiece.color != targetPiece.color;
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
            for (int x = start; x < end; x++) {
                if (this.board[startY][x].piece != null) {
                    return false;
                }
            }
        } else if(Math.abs(startX - endX) == Math.abs(startY - endY)){
            //diagonal
            double slope = (double) (startY - endY) / (startX - endX);

            int scanStartX;
            int scanEndX;
            int scanStartY;
            int yModifier;

            scanStartX = Math.min(startX, endX) + 1;
            scanEndX = Math.max(startX, endX);
            if (slope > 0) {
                // low x and y to high x and y
                scanStartY = Math.min(startY, endY);
                yModifier = 1;

            } else {
                // low x and high y to high x and low y
                scanStartY = Math.max(startY, endY);
                yModifier = -1;
            }

            for(int i = 0; i < scanEndX - scanStartX; i++){
                if(this.board[scanStartX + i][scanStartY + i * yModifier].piece != null) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    //TODO
    private boolean isCheck(Player player) {
        Player otherPlayer;
        int kingX, kingY;
        if(player == Player.One) {
            otherPlayer = Player.Two;
            kingX = this.whiteKingX;
            kingY = this.whiteKingY;
        } else {
            otherPlayer = Player.One;
            kingX = this.blackKingX;
            kingY = this.blackKingY;
        }
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                Piece currentPiece = this.board[y][x].piece;
                if(currentPiece == null || currentPiece.color == player.color){
                    continue;
                }
                Move testMove = new Move(x, y, kingX, kingY, currentPiece, otherPlayer);
                if(testMove.possibleMovement() && this.validMove(testMove)) {
                    return true;
                }
            }
        }
        return false;
    }

    //TODO
    private boolean isCheck(Player player, int endX, int endY) {
        Player otherPlayer;
        if(player == Player.One) {
            otherPlayer = Player.Two;
        } else {
            otherPlayer = Player.One;
        }
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                Piece currentPiece = this.board[y][x].piece;
                if(currentPiece == null || currentPiece.color == player.color){
                    continue;
                }
                Move testMove = new Move(x, y, endX, endY, currentPiece, otherPlayer);
                if(testMove.possibleMovement() && this.validMove(testMove)) {
                    return true;
                }
            }
        }
        return false;
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
