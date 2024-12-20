package game.board;

import chessNotation.Move;
import chessNotation.Position;
import game.Player;

public class Board {
    private final Position whiteKing = new Position(4, 7);
    private final Position blackKing = new Position(4, 0);
    public static final int BOARD_SIZE = 8;
    private final Position enPassantPiece;
    private final Square[][] board;
    private boolean whiteMovedKing;
    private boolean whiteMovedLeftRook;
    private boolean whiteMovedRightRook;
    private boolean blackMovedKing;
    private boolean blackMovedLeftRook;
    private boolean blackMovedRightRook;
    private boolean whiteInCheck;
    private boolean blackInCheck;

    public Board() {
        whiteMovedKing = false;
        whiteMovedLeftRook = false;
        whiteMovedRightRook = false;
        blackMovedKing = false;
        blackMovedLeftRook = false;
        blackMovedRightRook = false;
        whiteInCheck = false;
        blackInCheck = false;
        enPassantPiece = new Position();
        this.board = new Square[8][8];

        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                this.board[y][x] = new Square();
            }
            if (y == 0) {
                for (int x = 0; x < BOARD_SIZE; x++) {
                    this.populateRow(Color.Black, 0);
                }
            } else if (y == 1) {
                for (int x = 0; x < BOARD_SIZE; x++) {
                    this.setPiece(x, y, new Piece(Color.Black, Piece.Type.Pawn));
                }
            } else if (y == 6) {
                for (int x = 0; x < BOARD_SIZE; x++) {
                    this.setPiece(x, y, new Piece(Color.White, Piece.Type.Pawn));
                }
            } else if (y == 7) {
                this.populateRow(Color.White, 7);
            }
        }
    }

    public Player getChecked() {
        if (this.whiteInCheck) {
            return Player.One;
        } else if (this.blackInCheck) {
            return Player.Two;
        } else {
            return null;
        }
    }

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

    public void setEnPassantPiece(int x, int y) {
        this.enPassantPiece.updatePosition(x, y);
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
            return false;
        }

        Piece movingPiece = this.getPiece(move.start.x, move.start.y);

        if (move.isCastling()) {
            int y = (move.currentPlayer == Player.One) ? 7 : 0;
            Piece rook = (move.currentPlayer == Player.One) ? new Piece(Color.White, Piece.Type.Rook) : new Piece(Color.Black, Piece.Type.Rook);

            this.setPiece(move.end.x, move.end.y, null);
            this.setPiece(move.start.x, move.start.y, null);
            boolean kingSide = move.end.x > move.start.x;

            if (kingSide) {
                this.setPiece(5, y, rook);
                this.setPiece(6, y, movingPiece);
            } else {
                this.setPiece(3, y, rook);
                this.setPiece(2, y, movingPiece);
            }
        } else {
            this.setPiece(move.end.x, move.end.y, movingPiece);
            this.setPiece(move.start.x, move.start.y, null);
        }

        if (move.selectedPiece.getType() == Piece.Type.King) {
            this.updateKingPosition(move.end.x, move.end.y, move.currentPlayer);
        }
        return true;
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
                this.whiteKing.updatePosition(x, y);
                break;
            case Two:
                this.blackKing.updatePosition(x, y);

        }
    }

    private boolean simulateMove(Move move) {
        if(!move.possibleMovement() || !this.validMove(move)){
            return false;
        }

        if (move.isCastling()) {
            if (isCheck(move.currentPlayer)) {
                return false;
            }

            int y = (move.currentPlayer == Player.One) ? 7 : 0;
            boolean kingSide = move.end.x > move.start.x;

            // Test if king moves through check during castling
            // For kingside castling, check squares f1/f8 and g1/g8
            // For queenside castling, check squares d1/d8 and c1/c8
            int[] squaresToCheck = kingSide ?
                    new int[]{5, 6} :  // kingside: f and g files
                    new int[]{3, 2};   // queenside: d and c files

            Piece king = this.getPiece(move.start.x, move.start.y);
            this.setPiece(move.start.x, move.start.y, null);

            for (int x : squaresToCheck) {
                // Temporarily place king on each square it moves through
                this.setPiece(x, y, king);
                if (move.currentPlayer == Player.One) {
                    this.whiteKing.updatePosition(x, y);
                } else {
                    this.blackKing.updatePosition(x, y);
                }

                // Check if this square is under attack
                if (isCheck(move.currentPlayer)) {
                    this.setPiece(x, y, null);
                    this.setPiece(move.start.x, move.start.y, king);
                    if (move.currentPlayer == Player.One) {
                        this.whiteKing.updatePosition(move.start.x, move.start.y);
                    } else {
                        this.blackKing.updatePosition(move.start.x, move.start.y);
                    }
                    return false;
                }

                // Remove king from test square before checking next square
                this.setPiece(x, y, null);
            }

            this.setPiece(move.start.x, move.start.y, king);
            if (move.currentPlayer == Player.One) {
                this.whiteKing.updatePosition(move.start.x, move.start.y);
            } else {
                this.blackKing.updatePosition(move.start.x, move.start.y);
            }

            return true;
        }

        Piece movingPiece = this.getPiece(move.start.x, move.start.y);
        Piece takenPiece = this.getPiece(move.end.x, move.end.y);
        this.setPiece(move.end.x, move.end.y, movingPiece);
        this.setPiece(move.start.x, move.start.y, null);
        if(movingPiece.getType() == Piece.Type.King){
            if(move.currentPlayer == Player.One){
                this.whiteKing.updatePosition(move.end.x, move.end.y);
            } else {
                this.blackKing.updatePosition(move.end.x, move.end.y);
            }
        }

        boolean movedIntoCheck = isCheck(move.currentPlayer);
        if(movedIntoCheck){
            this.setPiece(move.start.x, move.start.y, movingPiece);
            this.setPiece(move.end.x, move.end.y,takenPiece);
            if(movingPiece.getType() == Piece.Type.King){
                if(move.currentPlayer == Player.One){
                    this.whiteKing.updatePosition(move.start.x, move.start.y);
                } else {
                    this.blackKing.updatePosition(move.start.x, move.start.y);
                }
            }
            return false;
        }

        if(move.currentPlayer == Player.One){
            this.blackInCheck = isCheck(Player.Two);
        } else {
            this.whiteInCheck = isCheck(Player.One);
        }
        this.setPiece(move.start.x, move.start.y, movingPiece);
        this.setPiece(move.end.x, move.end.y,takenPiece);
        if(movingPiece.getType() == Piece.Type.King){
            if(move.currentPlayer == Player.One){
                this.whiteKing.updatePosition(move.start.x, move.start.y);
            } else {
                this.blackKing.updatePosition(move.start.x, move.start.y);
            }
        }
        return true;
    }

    public boolean validMove(Move move) {
        Piece destinationPiece = this.getPiece(move.end.x, move.end.y);
        Piece targetPiece = this.getPiece(move.start.x, move.start.y);
        if (targetPiece == null || targetPiece.getColor() != move.currentPlayer.color) {
            return false;
        }
        switch (move.selectedPiece.getType()){
            case Pawn:
                if(move.end.x == move.start.x) {
                    return destinationPiece == null && !piecesBetweenSquares(move.start.x, move.start.y, move.end.x, move.end.y);
                } else {
                    if(destinationPiece != null && destinationPiece.getColor() != targetPiece.getColor()){
                        return true;
                    }
                    if(move.end.x == this.enPassantPiece.x && move.end.y == (move.currentPlayer == Player.One ? this.enPassantPiece.y - 1 : this.enPassantPiece.y + 1)){
                        this.setPiece(this.enPassantPiece.x, this.enPassantPiece.y, null);
                        return true;
                    }
                }
                return false;
            case Rook:
            case Queen:
            case Bishop:
                return (destinationPiece == null || destinationPiece.getColor() != targetPiece.getColor()) && !piecesBetweenSquares(move.start.x, move.start.y, move.end.x, move.end.y);
            case King:
                if (move.currentPlayer == Player.One && !this.whiteMovedKing && !piecesBetweenSquares(move.start.x, move.start.y, move.end.x, move.end.y)) {
                    if (!this.whiteMovedLeftRook && move.end.x == 0 && move.end.y == 7) {
                        return true;
                    } else if (!this.whiteMovedRightRook && move.end.x == 7 && move.end.y == 7) {
                        return true;
                    }
                }

                if (move.currentPlayer == Player.Two && !this.blackMovedKing && !piecesBetweenSquares(move.start.x, move.start.y, move.end.x, move.end.y)) {
                    if (!this.blackMovedLeftRook && move.end.x == 0 && move.end.y == 0) {
                        return true;
                    } else if (!this.blackMovedRightRook && move.end.x == 7 && move.end.y == 0) {
                        return true;
                    }
                }
            case Knight:
                return destinationPiece == null || destinationPiece.getColor() != targetPiece.getColor();
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

    private boolean isCheck(Player player) {
        Player otherPlayer;
        Position king;
        if(player == Player.One) {
            otherPlayer = Player.Two;
            king = new Position(whiteKing.x, whiteKing.y);
        } else {
            otherPlayer = Player.One;
            king = new Position(blackKing.x, blackKing.y);
        }

        for(int y = 0; y < BOARD_SIZE; y++){
            for(int x = 0; x < BOARD_SIZE; x++){
                Piece currentPiece = this.getPiece(x, y);
                if(currentPiece == null || currentPiece.getColor() == player.color){
                    continue;
                }
                Move testMove = new Move(x, y, king.x, king.y, currentPiece, otherPlayer);
                if(testMove.possibleMovement() && this.validMove(testMove)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isStalemate(Player player) {
        return !isCheck(player) && noValidMoves(player);
    }

    public boolean isCheckMate(Player player) {
        return isCheck(player) && noValidMoves(player);
    }

    private boolean noValidMoves(Player player) {
        for(int y = 0; y < BOARD_SIZE; y++){
            for(int x = 0; x < BOARD_SIZE; x++){
                Piece currentPiece = this.getPiece(x, y);
                if(currentPiece == null || currentPiece.getColor() != player.color){
                    continue;
                }
                switch(currentPiece.getType()){
                    case Rook:
                        for (int a = 0; a < BOARD_SIZE; a++) {
                            if (simulateMove(new Move(x, y, x, a, currentPiece, player)) || simulateMove(new Move(x, y, a, y, currentPiece, player))) {
                                return false;
                            }
                        }
                        break;
                    case King:
                    case Queen:
                    case Knight:
                    case Pawn:
                    case Bishop:
                        // try to move the piece to every square on the board
                        for (int otherY = 0; otherY < BOARD_SIZE; otherY++) {
                            for (int otherX = 0; otherX < BOARD_SIZE; otherX++) {
                                Piece piece = this.getPiece(otherX, otherY);
                                Color color = (Player.One == player) ? Color.White : Color.Black;
                                if (piece != null && piece.getColor() == color) {
                                    continue;
                                }

                                if (simulateMove(new Move(x, y, otherX, otherY, currentPiece, player))) {
                                    return false;
                                }
                            }
                        }
                        break;
                }
            }
        }
        return true;
    }

    public void printBoard() {
        final String BLACK_BG = "\u001B[42m";
        final String WHITE_BG = "\u001B[103m";
        final String RESET = "\u001B[0m";
        System.out.println("   a  b  c  d  e  f  g  h");
        for(int i = 0; i < 4; i++){
            System.out.print(8 - i * 2 + " ");
            for(int j = 0; j < 4; j++){
                System.out.print(WHITE_BG + ' ' + this.board[i * 2][j * 2].toStr() + ' ' + BLACK_BG + ' ' + this.board[i * 2][j * 2 + 1].toStr() + ' ');
            }
            System.out.println(RESET + " " + (8 - i * 2));
            System.out.print(7 - i * 2 + " ");
            for(int j = 0; j < 4; j++){
                System.out.print(BLACK_BG + ' ' + this.board[i * 2 + 1][j * 2].toStr() + ' ' + WHITE_BG + ' ' + this.board[i * 2 + 1][j * 2 + 1].toStr() + ' ');
            }
            System.out.println(RESET + " " + (7 - i * 2));
        }
        System.out.println("   a  b  c  d  e  f  g  h");
    }
}
