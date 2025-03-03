package me.opmaster.chess.game.pieces;

import me.opmaster.chess.game.Board;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece{
    public Knight(boolean isWhite, Board board) {
        super(isWhite, board);
    }

    private Board board = getBoard();

    @Override
    public boolean isLegal(int startX, int startY, int destinationX, int destinationY) {
        int dx = Math.abs(destinationX - startX);
        int dy = Math.abs(destinationY - startY);

        if (dx * dy == 2) { // L: (2, 1) or (1, 2)
            return board.isFieldEmpty(destinationX, destinationY) || board.getPiece(destinationX, destinationY).isWhite() != isWhite();
        }
        return false;
    }

    @Override
    public List<int[]> possibleMoves(int startX, int startY) {
        List<int[]> allMoves = new ArrayList<>();
        // Possible moves for a knight
        int[][] moves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
        for (int[] move : moves) {
            int desX = startX + move[0];
            int desY = startY + move[1];

            if (desX < 0 || desX >= 8 || desY < 0 || desY >= 8) {
                continue;
            }

            if (board.isFieldEmpty(desX, desY) || board.getPiece(desX, desY).isWhite() != isWhite()) {
                int[] knightMove = {desX, desY};
                allMoves.add(knightMove);
            }

        }
        return allMoves;
    }

    @Override
    public char getChar() {
        if (isWhite()) {
            return '♘';
        } else {
            return '♞';
        }
    }
}
