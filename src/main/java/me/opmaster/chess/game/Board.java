package me.opmaster.chess.game;


import me.opmaster.chess.game.pieces.*;

import java.util.Arrays;

public class Board {
    private Piece[][] board = new Piece[8][8];
    private boolean isWhitesTurn = true;
    public int[] whiteEnPassantField;
    public int[] blackEnPassantField;

    public void initialize() {
        board[4][0] = new King(true, this);
        board[4][7] = new King(false, this);

        board[3][0] = new Queen(true, this);
        board[3][7] = new Queen(false, this);

        board[2][0] = new Bishop(true, this);
        board[5][0] = new Bishop(true, this);
        board[2][7] = new Bishop(false, this);
        board[5][7] = new Bishop(false, this);

        board[1][0] = new Knight(true, this);
        board[6][0] = new Knight(true, this);
        board[1][7] = new Knight(false, this);
        board[6][7] = new Knight(false, this);

        board[0][0] = new Rook(true, this);
        board[7][0] = new Rook(true, this);
        board[0][7] = new Rook(false, this);
        board[7][7] = new Rook(false, this);

        for (int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(true, this);
            board[i][6] = new Pawn(false, this);
        }
    }

    public void movePiece(int originalX, int originalY, int targetX, int targetY) {
        if (isWhitesTurn) {
            whiteEnPassantField = null;
        } else {
            blackEnPassantField = null;
        }
        System.out.println(Arrays.toString(whiteEnPassantField) + " " + Arrays.toString(blackEnPassantField));

        if (board[originalX][originalY] == null) {
            System.out.println("No piece at the selected position.");
            return;
        }
        boolean isWhite = board[originalX][originalY].isWhite();
        if (board[originalX][originalY].isWhite() != isWhitesTurn) {
            System.out.println("Enemy Piece.");
            return;
        }
        if (board[originalX][originalY].isLegal(originalX, originalY, targetX, targetY)) {
            board[targetX][targetY] = board[originalX][originalY];
            board[originalX][originalY] = null;
            board[targetX][targetY].setHasMoved(true);
            System.out.println("Move successful.");
            nextTurn();
        } else {
            System.out.println("Illegal move.");
        }
    }

    public void nextTurn() {
        isWhitesTurn = !isWhitesTurn;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setBoard(Piece[][] board) {
        this.board = board;
    }

    public boolean isFieldEmpty(int x, int y) {
        return board[x][y] == null;
    }

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public void setPiece(Piece piece, int x, int y) {
        board[x][y] = piece;
    }

    public void setWhitesTurn(boolean whitesTurn) {
        this.isWhitesTurn = whitesTurn;
    }
}