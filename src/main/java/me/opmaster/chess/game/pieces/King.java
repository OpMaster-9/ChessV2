package me.opmaster.chess.game.pieces;

import me.opmaster.chess.game.Board;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    public King(boolean isWhite, Board board) {
        super(isWhite, board);
    }

    private Board board = getBoard();
    private boolean hasMoved = false;

    @Override
    public boolean isLegal(int startX, int startY, int destinationX, int destinationY) {
        int dx = Math.abs(destinationX - startX);
        int dy = Math.abs(destinationY - startY);

        boolean destinationIsEmpty = board.isFieldEmpty(destinationX, destinationY);
        boolean destinationHasEnemy = !board.isFieldEmpty(destinationX, destinationY) && board.getPiece(destinationX, destinationY).isWhite() != isWhite();

        if (dx <= 1 && dy <= 1) { // Normal Move (one square in any direction)
            return destinationIsEmpty || destinationHasEnemy;
        }

        if (dy != 0 || dx != 2 || hasMoved) return false;

        // Can't castle out of or through check
        if (board.inCheck(isWhite(), startX, startY)) return false;

        int direction = (destinationX - startX) / 2; // +1 (kingside) or -1 (queenside)
        int rookX = destinationX > startX ? 7 : 0;
        int rookY = startY;

        Piece rook = board.getPiece(rookX, rookY);
        if (!(rook instanceof Rook) || rook.hasMoved()) return false;

        // Ensure all squares between king and rook are empty
        int step = destinationX > startX ? 1 : -1;
        for (int x = startX + step; x != rookX; x += step) {
            if (!board.isFieldEmpty(x, startY)) return false;
        }

        // Check that king does not move through or into check
        for (int x = startX; x != destinationX + step; x += step) {
            if (board.moveIsLegal(startX, startY, x, startY, isWhite())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<int[]> possibleMoves(int startX, int startY) {
        List<int[]> allMoves = new ArrayList<>();
        int[][] deltas = {
                {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
        };
        for (int[] delta : deltas) {
            if (board.isFieldEmpty(delta[0], delta[1]) || board.getPiece(delta[0], delta[1]).isWhite() != isWhite()){
                int[] currentPos = {startX += delta[0], startY += delta[1]};
                allMoves.add(currentPos);
            }
        }
        return allMoves;
    }

    @Override
    public char getChar() {
        if (isWhite()) {
            return '♔';
        } else {
            return '♚';
        }
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
