package me.opmaster.chess.game.pieces;


import me.opmaster.chess.app.page.promotion;
import me.opmaster.chess.game.Board;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite, Board board) {
        super(isWhite, board);
    }

    private Board board = getBoard();

    private boolean isDestinationEnPassantField(Board board, int destinationX, int destinationY) {
        int[] enPassantField = isWhite() ? board.blackEnPassantField : board.whiteEnPassantField;
        return enPassantField != null && enPassantField[0] == destinationX && enPassantField[1] == destinationY;
    }

    @Override
    public boolean isLegal(int startX, int startY, int destinationX, int destinationY) {
        int direction;
        int startRow;
        if (isWhite()) {
            direction = 1;
            startRow = 1;
        } else {
            direction = -1;
            startRow = 6;
        }

        boolean destinationIsEmpty = board.isFieldEmpty(destinationX, destinationY);
        boolean destinationHasEnemy = !board.isFieldEmpty(destinationX, destinationY) && board.getPiece(destinationX, destinationY).isWhite() != isWhite();

        boolean destinationIsEnPassantField = isDestinationEnPassantField(board, destinationX, destinationY);

        if (destinationX == startX && destinationY == startY  + direction && destinationIsEmpty) {
            return true; // Single step forward
        }
        if (startY == startRow && destinationX == startX && destinationY == startY  + 2 * direction && destinationIsEmpty && board.isFieldEmpty(startX, startY + direction)) {
            return true; // Double step forward
        }
        if (Math.abs(destinationX - startX) == 1 && destinationY == startY + direction && (destinationHasEnemy || destinationIsEnPassantField)) {
            return true; // Diagonal capture
        }
        return false;
    }

    @Override
    public List<int[]> possibleMoves(int startX, int startY) {
        List<int[]> allMoves = new ArrayList<>();

        int direction;
        int startRow;
        if (isWhite()) {
            direction = 1;
            startRow = 1;
        }
        else {
            direction = -1;
            startRow = 6;
        }

        if (board.isFieldEmpty(startX, startY  + direction) || board.getPiece(startX, startY  + direction).isWhite() != isWhite()) {
            int[] singleForward = {startX + direction, startY};
            allMoves.add(singleForward);
        }
        if (startY == startRow && board.isFieldEmpty(startX, startY  + direction) && board.isFieldEmpty(startX, startY  + 2 * direction) || board.getPiece(startX, startY  + 2 * direction).isWhite() != isWhite()) {
            int[] doubleForward = {startX, startY + 2 * direction};
            allMoves.add(doubleForward); // Double step forward
        }
        if (board.getPiece(startX + 1, startY  + direction).isWhite() != isWhite()) {
            int[] captureRight = {startX + 1, startY + direction};
            allMoves.add(captureRight); // Diagonal capture right
        }
        if (board.getPiece(startX - 1, startY  + direction).isWhite() != isWhite()) {
            int[] captureLeft = {startX - 1, startY + direction};
            allMoves.add(captureLeft); // Diagonal capture left
        }
        return allMoves;
    }

    @Override
    public char getChar() {
        if (isWhite()) {
            return '♙';
        } else {
            return '♟';
        }
    }
}
