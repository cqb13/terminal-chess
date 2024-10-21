package game;

import chessNotation.ChessNotation;
import chessNotation.Move;
import game.board.Board;
import game.board.Piece;
import utils.Result;

import java.util.Scanner;

public class Game {
    private boolean whiteMovedKing = false;
    private boolean whiteMovedLeftRook = false;
    private boolean whiteMovedRightRook = false;

    private boolean blackMovedKing = false;
    private boolean blackMovedLeftRook = false;
    private boolean blackMovedRightRook = false;

    private Player currentPlayer = Player.One;

    private final Board board;
    private final Scanner input;

    public Game() {
        this.board = new Board();
        this.input = new Scanner(System.in);
    }

    public void takeTurn() {
        String userInput = input.nextLine();

        Result<Move> result = ChessNotation.getMovePosition(userInput, currentPlayer);

        if (result.isSuccess()) {
            Move move = result.getValue();
            boolean possible = move.possibleMovement();
            if(possible && this.board.validMove(move)) {
                //TODO: move move logic into Board.java
                Piece movingPiece = this.board.getPiece(move.startX, move.startY);
                System.out.println(movingPiece.toStr());
                this.board.setPiece(move.endX, move.endY, movingPiece);
                this.board.setPiece(move.startX, move.startY, null);

                if (move.selectedPiece.getType() == Piece.Type.King) {
                    this.board.updateKingPosition(move.endX, move.endY, move.currentPlayer);
                }

                //clear console
                this.board.printBoard();
                if (this.currentPlayer == Player.One) {
                    this.currentPlayer = Player.Two;
                } else {
                    this.currentPlayer = Player.One;
                }
            } else {
                System.out.println("no");
                if(!possible){
                    System.out.println("not possible");
                }
                if(!this.board.validMove(move)){
                    System.out.println("invalid move");
                }
            }
        } else {
            System.out.println("Error: " + result.getErrorMessage());
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean playerWon() {
        return this.board.playerWon();
    }

    public void display() {
        this.board.printBoard();
    }
}
