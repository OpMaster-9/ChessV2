package me.opmaster.chess.game.pieces;

import me.opmaster.chess.game.Board;

import java.util.Collections;
import java.util.List;

public class King extends Piece{
    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isLegal(Board board, int startX, int startY, int destinationX, int destinationY) {
        int dx = Math.abs(destinationX - startX);
        int dy = Math.abs(destinationY - startY);

        boolean destinationIsEmpty = board.isFieldEmpty(destinationX, destinationY);
        boolean destinationHasEnemy = !board.isFieldEmpty(destinationX, destinationY) && board.getPiece(destinationX, destinationY).isWhite() != isWhite();

        if ((dx * dy < 2) && (destinationHasEnemy || destinationIsEmpty)) {
            return true;
        }
        return false;
    }

    @Override
    public List<int[]> possibleMoves(Board board, int startX, int startY) {
        return Collections.emptyList();
    }

    @Override
    public char getChar() {
        if (isWhite()) {
            return '♚';
        } else {
            return '♔';
        }
    }
}
