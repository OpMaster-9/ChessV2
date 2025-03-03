package me.opmaster.chess.game.pieces;

import me.opmaster.chess.game.Board;

import java.util.List;

public abstract class Piece {
    private boolean isWhite;
    private Board board;

    public Piece(boolean isWhite, Board board) {
        this.isWhite = isWhite;
        this.board = board;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public Board getBoard() {
        return board;
    }

    private boolean hasMoved;

    public void setHasMoved(boolean hasMoved) {

    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public abstract boolean isLegal(int startX, int startY, int destinationX, int destinationY);

    public abstract List<int[]> possibleMoves(int startX, int startY);

    public abstract char getChar();
}
