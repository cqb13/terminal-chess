package game;

import chessNotation.ChessNotation;
import chessNotation.Move;
import game.board.Board;
import game.board.Color;
import game.board.Piece;
import utils.Result;

import java.util.Scanner;

public class Game {
    private Player currentPlayer = Player.One;

    private final Board board;
    private final Scanner input;

    public Game(String[] testBoard) {
        this.board = new Board(testBoard);
        this.input = new Scanner(System.in);
    }


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
                this.board.isStalemate(this.currentPlayer);
                if (this.currentPlayer == Player.One) {
                    this.currentPlayer = Player.Two;
                } else {
                    this.currentPlayer = Player.One;
                }

                if (move.selectedPiece.getType() == Piece.Type.Pawn && (move.end.y == 7 || move.end.y == 0)) {
                    Piece piece = promotePiece((move.currentPlayer == Player.One) ? Color.White : Color.Black);
                    this.board.setPiece(move.end.x, move.end.y, piece);
                    this.board.printBoard();
                }

                if (move.selectedPiece.getType() == Piece.Type.King) {
                    if (move.currentPlayer == Player.One) {
                        this.board.whiteMovedKing();
                    } else if (move.currentPlayer == Player.Two) {
                        this.board.blackMovedKing();
                    }
                }

                if (move.selectedPiece.getType() == Piece.Type.Pawn && Math.abs(move.end.y - move.start.y) == 2) {
                    this.board.setEnPassantPiece(move.end.x, move.end.y);
                }

                // Checks if a rook is moved from its starting location
                if (move.currentPlayer == Player.One && move.selectedPiece.getType() == Piece.Type.Rook) {
                    if (move.start.x == 0 && move.start.y == 7) {
                        this.board.whiteMovedLeftRook();
                    } else if (move.start.x == 7 && move.start.y == 7) {
                        this.board.whiteMovedRightRook();
                    }
                }

                if (move.currentPlayer == Player.Two && move.selectedPiece.getType() == Piece.Type.Rook) {
                    if (move.start.x == 0 && move.start.y == 0) {
                        this.board.blackMovedLeftRook();
                    } else if (move.start.x == 7 && move.start.y == 0) {
                        this.board.blackMovedRightRook();
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

    public Piece promotePiece(Color color) {
        System.out.println("Select a piece to replace the pawn (Q, R, B, N): ");
        String userInput = input.nextLine();
        Piece piece = ChessNotation.determinePiece(userInput, color);

        if (piece == null || piece.getType() == Piece.Type.King || piece.getType() == Piece.Type.Pawn) {
            piece = promotePiece(color);
        }

        return piece;
    }

    public boolean playerWon() {
        return this.board.playerWon();
    }

    public void display() {
        this.board.printBoard();
    }
}
