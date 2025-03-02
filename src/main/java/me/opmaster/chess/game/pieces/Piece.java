package me.opmaster.chess.game.pieces;

import me.opmaster.chess.game.Board;

import java.util.List;

public abstract class Piece {
    private boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract boolean isLegal(Board board, int startX, int startY, int destinationX, int destinationY);

    public abstract List<int[]> possibleMoves(Board board, int startX, int startY);

    public abstract char getChar();
}
