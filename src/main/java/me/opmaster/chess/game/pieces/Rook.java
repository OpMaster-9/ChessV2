package me.opmaster.chess.game.pieces;

import me.opmaster.chess.game.Board;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{
    public Rook(boolean isWhite, Board board) {
        super(isWhite, board);
    }

    private Board board = getBoard();

    @Override
    public boolean isLegal(int startX, int startY, int destinationX, int destinationY) {
        if (startX == destinationX || startY == destinationY){ //move is a straight line
            boolean destinationIsEmpty = board.isFieldEmpty(destinationX, destinationY);
            boolean destinationHasEnemy = !board.isFieldEmpty(destinationX, destinationY) && board.getPiece(destinationX, destinationY).isWhite() != isWhite();

            return isPathClear(startX, startY, destinationX, destinationY) && (destinationIsEmpty || destinationHasEnemy);
        }
        return false;
    }

    private boolean isPathClear(int startX, int startY, int destinationX, int destinationY) {
        int dx = Integer.compare(destinationX, startX);
        int dy = Integer.compare(destinationY, startY);

        int x = startX + dx, y = startY + dy;
        while (x != destinationX || y != destinationY) {
            if (!board.isFieldEmpty(x, y)) {
                return false;
            }
            x += dx;
            y += dy;
        }
        return true;
    }


    @Override
    public List<int[]> possibleMoves(int startX, int startY) {
        List<int[]> allMoves = new ArrayList<>();
        int[][] deltas = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0}
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
            return '♖';
        } else {
            return '♜';
        }
    }
}
