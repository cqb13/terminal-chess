package game;

import chessNotation.ChessNotation;
import chessNotation.Move;
import game.board.Board;
import game.board.Piece;
import utils.Result;

import java.util.Scanner;

public class Game {
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
            if(this.board.movePiece(move)) {
                //clear console
                this.board.printBoard();
                if (this.currentPlayer == Player.One) {
                    this.currentPlayer = Player.Two;
                } else {
                    this.currentPlayer = Player.One;
                }

                if (move.selectedPiece.getType() == Piece.Type.King) {
                    if (move.currentPlayer == Player.One) {
                        board.whiteMovedKing();
                    } else if (move.currentPlayer == Player.Two) {
                        board.blackMovedKing();
                    }
                }

                // Checks if a rook is moved from its starting location
                if (move.currentPlayer == Player.One && move.selectedPiece.getType() == Piece.Type.Rook) {
                    if (move.startX == 0 && move.startY == 7) {
                        board.whiteMovedLeftRook();
                    } else if (move.startX == 7 && move.startY == 7) {
                        board.whiteMovedRightRook();
                    }
                }

                if (move.currentPlayer == Player.Two && move.selectedPiece.getType() == Piece.Type.Rook) {
                    if (move.startX == 0 && move.startY == 0) {
                        board.blackMovedLeftRook();
                    } else if (move.startX == 7 && move.startY == 0) {
                        board.blackMovedRightRook();
                    }
                }
            } else {
                System.out.println("failed to move");
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
