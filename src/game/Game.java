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

    public GameEnding takeTurn() {
        String userInput = input.nextLine();

        Result<Move> result = ChessNotation.getMovePosition(userInput, currentPlayer);

        if (result.isSuccess()) {
            Move move = result.getValue();
            boolean possible = move.possibleMovement();
            if(this.board.movePiece(move)) {
                this.display(true);
                if (this.currentPlayer == Player.One) {
                    this.currentPlayer = Player.Two;
                } else {
                    this.currentPlayer = Player.One;
                }
                boolean stalemate = this.board.isStalemate(this.currentPlayer);
                boolean checkmate = this.board.isCheckMate(this.currentPlayer);

                if (stalemate) {
                    return GameEnding.Draw;
                }

                if (checkmate) {
                    if (this.currentPlayer == Player.Two) {
                        return GameEnding.WhiteWon;
                    } else {
                        return GameEnding.BlackWon;
                    }
                }

                if (move.selectedPiece.getType() == Piece.Type.Pawn && (move.end.y == 7 || move.end.y == 0)) {
                    Piece piece = promotePiece((move.currentPlayer == Player.One) ? Color.White : Color.Black);
                    this.board.setPiece(move.end.x, move.end.y, piece);
                    this.display(true);
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
                if(!this.board.validMove(move) || !possible){
                    System.out.println("invalid move");
                }
            }
        } else {
            System.out.println("Error: " + result.getErrorMessage());
        }

        return GameEnding.None;
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

    public void display(boolean clearConsole) {
        if(clearConsole) {
//            System.out.print("\033[H\033[2J");
//            System.out.flush();
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
        this.board.printBoard();
    }
}
