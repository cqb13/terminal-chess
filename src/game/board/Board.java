package game.board;

import chessNotation.Move;
import game.Player;

public class Board {
    private final Square[][] board;
    private int whiteKingX = 4;
    private int whiteKingY = 7;
    private int blackKingX = 4;
    private int blackKingY = 0;
    private boolean whiteMovedKing = false;
    private boolean whiteMovedLeftRook = false;
    private boolean whiteMovedRightRook = false;
    private boolean blackMovedKing = false;
    private boolean blackMovedLeftRook = false;
    private boolean blackMovedRightRook = false;
    private boolean whiteInCheck = false;
    private boolean blackInCheck = false;
    public static final int BOARD_SIZE = 8;

    public void whiteMovedKing() {
        this.whiteMovedKing = true;
    }

    public void blackMovedKing() {
        this.blackMovedKing = true;
    }

    public void whiteMovedLeftRook() {
        this.whiteMovedLeftRook = true;
    }

    public void whiteMovedRightRook() {
        this.whiteMovedRightRook = true;
    }

    public void blackMovedLeftRook() {
        this.blackMovedLeftRook = true;
    }

    public void blackMovedRightRook() {
        this.blackMovedRightRook = true;
    }

    public Board() {
        this.board = new Square[8][8];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.board[i][j] = new Square();
            }
            if (i == 0) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    this.populateRow(Color.Black, 0);
                }
            } else if (i == 1) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    this.setPiece(j, i, new Piece(Color.Black, Piece.Type.Pawn));
                }
            } else if (i == 6) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    this.setPiece(j, i, new Piece(Color.White, Piece.Type.Pawn));
                }
            } else if (i == 7) {
                this.populateRow(Color.White, 7);
            }
        }
    }

    private void populateRow(Color color, int row) {
        for (int x = 0; x < BOARD_SIZE; x++) {
            if (x == 0 || x == 7) {
                this.setPiece(x, row, new Piece(color, Piece.Type.Rook));
            } else if (x == 1 || x == 6) {
                this.setPiece(x, row, new Piece(color, Piece.Type.Knight));
            } else if (x == 2 || x == 5) {
                this.setPiece(x, row, new Piece(color, Piece.Type.Bishop));
            } else if (x == 3) {
                this.setPiece(x, row, new Piece(color, Piece.Type.Queen));
            } else {
                this.setPiece(x, row, new Piece(color, Piece.Type.King));
            }
        }
    }

    public boolean movePiece(Move move) {
        boolean moveIsAllowed = simulateMove(move);
        if(!moveIsAllowed){
            System.out.println("sim move not allowed");
            return false;
        }
        System.out.println(move.startX + " " + move.startY);

        for (Square square : this.board[move.startY]) {
            System.out.print(square.toStr() + ", ");
        }
        Piece movingPiece = this.getPiece(move.startX, move.startY);
        System.out.println(movingPiece.toStr());
        this.setPiece(move.endX, move.endY, movingPiece);
        this.setPiece(move.startX, move.startY, null);

        if (move.selectedPiece.getType() == Piece.Type.King) {
            System.out.println("moving king");
            this.updateKingPosition(move.endX, move.endY, move.currentPlayer);
        }
        return true;
    }

    public boolean playerWon() {
        return false;
    }

    private Piece getPiece(int x, int y) {
        return board[y][x].piece;
    }

    public void setPiece(int x, int y, Piece piece) {
        board[y][x].setPiece(piece);
    }

    public void updateKingPosition(int x, int y, Player player) {
        switch (player) {
            case One:
                this.whiteKingX = x;
                this.whiteKingY = y;
                break;
            case Two:
                this.blackKingX = x;
                this.blackKingY = y;

        }
    }

    public boolean validMove(Move move) {
        Piece destinationPiece = this.getPiece(move.endX, move.endY);
        Piece targetPiece = this.getPiece(move.startX, move.startY);
        if (targetPiece == null || targetPiece.color != move.currentPlayer.color) {
            return false;
        }
        switch (move.selectedPiece.getType()){
            case Pawn:
                if(move.endX == move.startX) {
                    return destinationPiece == null && !piecesBetweenSquares(move.startX, move.startY, move.endX, move.endY);
                } else {
                    return destinationPiece != null && destinationPiece.color != targetPiece.color;
                }
            case Rook:
            case Queen:
            case Bishop:
                return (destinationPiece == null || destinationPiece.color != targetPiece.color) && !piecesBetweenSquares(move.startX, move.startY, move.endX, move.endY);
            case King:
                if(Math.abs(move.startX - move.endX) <= 1 && Math.abs(move.startY - move.endY) <= 1){
                    //castling
                    /**
                     *                 if (move.currentPlayer == Player.One && move.selectedPiece.getType() == Piece.Type.Rook) {
                     *                     if (move.startX == 0 && move.startY == 7) {
                     *                         board.whiteMovedLeftRook();
                     *                     } else if (move.startX == 7 && move.startY == 7) {
                     *                         board.whiteMovedRightRook();
                     *                     }
                     *                 }
                     *
                     *                 if (move.currentPlayer == Player.Two && move.selectedPiece.getType() == Piece.Type.Rook) {
                     *                     if (move.startX == 0 && move.startY == 0) {
                     *                         board.blackMovedLeftRook();
                     *                     } else if (move.startX == 7 && move.startY == 0) {
                     *                         board.blackMovedRightRook();
                     *                     }
                     *                 }
                     */

                    if (move.currentPlayer == Player.One) {
                        //TODO
                    }
                }
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
                System.out.println("Piece is scanning (" + this.getNotation(startX, y) + ")");
                if (this.getPiece(startX, y) != null) {
                    return true;
                }
            }
        } else if(startX != endX && startY == endY){
            //horizontal
            int start = Math.min(startX, endX) + 1;
            int end = Math.max(startX, endX);
            for (int x = start; x < end; x++) {
                if (this.getPiece(x, startY) != null) {
                    return true;
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
                scanStartY = Math.min(startY, endY) + 1;
                yModifier = 1;

            } else {
                // low x and high y to high x and low y
                scanStartY = Math.max(startY, endY) -1;
                yModifier = -1;
            }

            for(int i = 0; i < scanEndX - scanStartX; i++){
                if (this.getPiece(scanStartX + i, scanStartY + i * yModifier) != null) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    private boolean simulateMove(Move move){
        if(!move.possibleMovement() || !this.validMove(move)){
            return false;
        }

        Piece movingPiece = this.getPiece(move.startX, move.startY);
        this.setPiece(move.endX, move.endY, movingPiece);
        Piece takenPiece = this.getPiece(move.endX, move.endY);
        this.setPiece(move.startX, move.startY, null);
        if(movingPiece.type == Piece.Type.King){
            if(move.currentPlayer == Player.One){
                this.whiteKingX = move.endX;
                this.whiteKingY = move.endY;
            } else {
                this.blackKingX = move.endX;
                this.blackKingY = move.endY;
            }
        }

        boolean movedIntoCheck = isCheck(move.currentPlayer);
        if(movedIntoCheck){
            System.out.println("nope that's check");
            this.setPiece(move.startX, move.startY, movingPiece);
            this.setPiece(move.endX, move.endY,takenPiece);
            if(movingPiece.type == Piece.Type.King){
                if(move.currentPlayer == Player.One){
                    this.whiteKingX = move.startX;
                    this.whiteKingY = move.startY;
                } else {
                    this.blackKingX = move.startX;
                    this.blackKingY = move.startY;
                }
            }
            return false;
        }
        
        if(move.currentPlayer == Player.One){
            this.blackInCheck = isCheck(Player.Two);
        } else {
            this.whiteInCheck = isCheck(Player.One);
        }
        System.out.println("black: " + this.blackInCheck);
        System.out.println("white: " + this.whiteInCheck);
        this.setPiece(move.startX, move.startY, movingPiece);
        this.setPiece(move.endX, move.endY,takenPiece);
        if(movingPiece.type == Piece.Type.King){
            if(move.currentPlayer == Player.One){
                this.whiteKingX = move.startX;
                this.whiteKingY = move.startY;
            } else {
                this.blackKingX = move.startX;
                this.blackKingY = move.startY;
            }
        }
        return true;
    }

    private boolean isCheck(Player player) {
        Player otherPlayer;
        int kingX, kingY;
        if(player == Player.One) {
            otherPlayer = Player.Two;
            kingX = whiteKingX;
            kingY = whiteKingY;
        } else {
            otherPlayer = Player.One;
            kingX = blackKingX;
            kingY = blackKingY;
        }

        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                Piece currentPiece = this.getPiece(x, y);
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

    public void printBoard()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
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

    public String getNotation(int x, int y){
        return new String[]{"a", "b", "c", "d", "e", "f", "g", "h"}[x] + (8 - y);
    }
}
