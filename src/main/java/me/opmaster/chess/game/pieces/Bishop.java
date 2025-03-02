package me.opmaster.chess.game.pieces;

import me.opmaster.chess.game.Board;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{
    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isLegal(Board board, int startX, int startY, int destinationX, int destinationY) {
        int dx = Math.abs(destinationX - startX);
        int dy = Math.abs(destinationY - startY);

        if (dx == dy) { //diagonal move
            boolean destinationIsEmpty = board.isFieldEmpty(destinationX, destinationY);
            boolean destinationHasEnemy = !board.isFieldEmpty(destinationX, destinationY) && board.getPiece(destinationX, destinationY).isWhite() != isWhite();

            return isPathClear(board, startX, startY, destinationX, destinationY) && (destinationIsEmpty || destinationHasEnemy);

        }
        return false;
    }


    private boolean isPathClear(Board board, int startX, int startY, int destinationX, int destinationY) {
        int dx = Integer.compare(destinationX, startX);
        int dy = Integer.compare(destinationY, startY);

        int x = startX + dx;
        int y = startY + dy;

        while (x != destinationX || y != destinationY){
            if (!board.isFieldEmpty(x, y)){
                return false;
            }

            x += dx;
            y += dy;
        }

        return true;
    }

    @Override
    public List<int[]> possibleMoves(Board board, int startX, int startY) {
        List<int[]> allMoves = new ArrayList<>();
        int[][] deltas = {
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };
        for (int[] delta : deltas) {
            int[] currentPos = {startX, startY};
            while (board.isFieldEmpty(currentPos[0], currentPos[1])) {
                allMoves.add(currentPos);
                currentPos[0] += delta[0];
                currentPos[1] += delta[1];
            }
        }
        return allMoves;
    }


    @Override
    public char getChar() {
        if (isWhite()) {
            return '♗';
        } else {
            return '♝';
        }
    }
}
