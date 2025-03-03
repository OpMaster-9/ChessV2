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
            return board.getPiece(destinationX, destinationY) == null || board.getPiece(destinationX, destinationY).isWhite() != isWhite();
        }

        int cornerRookX = destinationX < startX ? 0 : 7;
        int cornerRookY = isWhite() ? 0 : 7;
        boolean isThirdFieldEmpty = destinationX > startX || board.isFieldEmpty(destinationX - 1, startY);

        if (destinationY == startY && dx == 2 && destinationIsEmpty && board.isFieldEmpty(((destinationX - startX) / 2) + startX, startY) && !hasMoved && !board.getPiece(cornerRookX, cornerRookY).hasMoved() && isThirdFieldEmpty) {
            board.setPiece(board.getPiece(cornerRookX, cornerRookY), ((destinationX - startX) / 2) + startX, cornerRookY);
            board.setPiece(null, cornerRookX, cornerRookY);
            board.getPiece(((destinationX - startX) / 2) + startX, cornerRookY).setHasMoved(true);
            return true;
        }
        return false;
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
