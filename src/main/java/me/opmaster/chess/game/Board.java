package me.opmaster.chess.game;


import me.opmaster.chess.app.page.promotion;
import me.opmaster.chess.game.pieces.*;

import java.util.Objects;

public class Board {
    private Piece[][] board = new Piece[8][8];
    private boolean isWhitesTurn = true;
    public int[] whiteEnPassantField;
    public int[] blackEnPassantField;
    boolean isWhiteHuman = true;
    boolean isBlackHuman = true;

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

        Piece movingPiece = board[originalX][originalY];

        if (movingPiece == null) {
            System.out.println("No piece at the selected position.");
            return;
        }

        if (movingPiece.isWhite() != isWhitesTurn) {
            System.out.println("Enemy Piece.");
            return;
        }

        if (!movingPiece.isLegal(originalX, originalY, targetX, targetY)) {
            System.out.println("Illegal move.");
            return;
        }

        if (moveIsLegal(originalX, originalY, targetX, targetY, isWhitesTurn)) {
            System.out.println("Illegal move.");
            return;
        }

        boolean isEnPassantMove = false;
        boolean isCastlingMove = false;

        //En passant
        if (movingPiece instanceof Pawn) {
            boolean movedDiagonally = Math.abs(targetX - originalX) == 1;
            boolean movedForwardOne = targetY - originalY == (movingPiece.isWhite() ? 1 : -1);
            boolean targetEmpty = board[targetX][targetY] == null;
            int[] enPassantTarget = movingPiece.isWhite() ? blackEnPassantField : whiteEnPassantField;
            if (movedDiagonally && movedForwardOne && targetEmpty && enPassantTarget != null &&
                    targetX == enPassantTarget[0] && targetY == enPassantTarget[1]) {
                isEnPassantMove = true;
            }
        }

        //Castling
        if (movingPiece instanceof King && Math.abs(targetX - originalX) == 2 && targetY == originalY) {
            isCastlingMove = true;

            int rookStartX = targetX == 6 ? 7 : 0;
            int rookEndX = targetX == 6 ? 5 : 3;
            int rookY = originalY;

            Piece rook = board[rookStartX][rookY];
            if (rook == null || !(rook instanceof Rook)) {
                System.out.println("Castling failed: Rook missing.");
                return;
            }

            board[rookEndX][rookY] = rook;
            board[rookStartX][rookY] = null;
            rook.setHasMoved(true);

            System.out.println("Castling executed.");
        }

        //En passant capture
        if (isEnPassantMove) {
            int capturedPawnY = isWhitesTurn ? targetY - 1 : targetY + 1;
            board[targetX][capturedPawnY] = null;
            System.out.println("En passant executed.");
        }

        //Move piece
        board[targetX][targetY] = board[originalX][originalY];
        board[originalX][originalY] = null;
        board[targetX][targetY].setHasMoved(true);

        //Set en passant field
        if (movingPiece instanceof Pawn) {
            int deltaY = targetY - originalY;
            if (Math.abs(deltaY) == 2) {
                int[] enPassantField = {originalX, originalY + deltaY / 2};
                if (isWhitesTurn) {
                    whiteEnPassantField = enPassantField;
                } else {
                    blackEnPassantField = enPassantField;
                }
            }
        }

        //Promotions
        if (movingPiece instanceof Pawn) {
            boolean reachedPromotionRow = movingPiece.isWhite() ? targetY == 7 : targetY == 0;
            if (reachedPromotionRow) {
                board[targetX][targetY] = promotion.promotion(isHuman(movingPiece.isWhite()), movingPiece.isWhite(), this); // or let player choose
                System.out.println("Pawn promoted to Queen.");
            }
        }

        System.out.println("Move successful.");
        nextTurn();

    }

    public boolean moveIsLegal(int originalX, int originalY, int targetX, int targetY, boolean isWhite) {
        Piece captured = board[targetX][targetY];
        Piece movingPiece = board[originalX][originalY];

        board[targetX][targetY] = movingPiece;
        board[originalX][originalY] = null;

        boolean isInCheck = inCheck(isWhite, findKing(isWhite)[0], findKing(isWhite)[1]);

        board[originalX][originalY] = movingPiece;
        board[targetX][targetY] = captured;

        return isInCheck;
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

    public boolean inCheck(boolean isWhite, int x, int y) {

        int direction;
        if (isWhite) {
            direction = -1;
        } else {
            direction = 1;
        }
        int[][] pawnMoves = {
                {direction, -1}, {direction, 1}
        };
        for (int[] move : pawnMoves) {
            int desX = x + move[0];
            int desY = y + move[1];

            if (desX < 0 || desX >= 8 || desY < 0 || desY >= 8) {
                continue;
            }

            Piece currentField = board[desX][desY];

            if (currentField == null) {
                continue;
            }

            if (currentField.isWhite() != isWhite && currentField instanceof Pawn) {
                return true;
            }
        }

        int[][] knightMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
        for (int[] move : knightMoves) {
            int desX = x + move[0];
            int desY = y + move[1];

            if (desX < 0 || desX >= 8 || desY < 0 || desY >= 8) {
                continue;
            }

            Piece currentField = board[desX][desY];

            if (currentField == null) {
                continue;
            }

            if (currentField.isWhite() != isWhite && currentField instanceof Knight) {
                return true;
            }
        }

        int[][] bishopDeltas = {
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1},   //Bishop deltas
        };
        outerloop:
        for (int[] delta : bishopDeltas) {
            int[] currentPos = {x, y};

            do {

                currentPos[0] += delta[0];
                currentPos[1] += delta[1];

                if (currentPos[0] < 0 || currentPos[0] >= 8 || currentPos[1] < 0 || currentPos[1] >= 8) {
                    continue outerloop;
                }

            } while (board[currentPos[0]][currentPos[1]] == null);
            if (board[currentPos[0]][currentPos[1]].isWhite() != isWhite && (board[currentPos[0]][currentPos[1]] instanceof Bishop || board[currentPos[0]][currentPos[1]] instanceof Queen)) {

                return true;
            }
        }

        int[][] rookDeltas = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0}      //Rook deltas
        };
        outerloop2:
        for (int[] delta : rookDeltas) {
            int[] currentPos = {x, y};

            do {

                currentPos[0] += delta[0];
                currentPos[1] += delta[1];

                if (currentPos[0] < 0 || currentPos[0] >= 8 || currentPos[1] < 0 || currentPos[1] >= 8) {
                    continue outerloop2;
                }

            } while (board[currentPos[0]][currentPos[1]] == null);
            if (board[currentPos[0]][currentPos[1]].isWhite() != isWhite && (board[currentPos[0]][currentPos[1]] instanceof Rook || board[currentPos[0]][currentPos[1]] instanceof Queen)) {

                return true;
            }

        }

        int[][] kingMoves = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };
        for (int[] move : kingMoves) {
            int desX = x + move[0];
            int desY = y + move[1];

            if (desX < 0 || desX >= 8 || desY < 0 || desY >= 8) {
                continue;
            }

            Piece currentField = board[desX][desY];

            if (currentField == null) {
                continue;
            }

            if (currentField.isWhite() != isWhite && currentField instanceof King) {
                return true;
            }
        }
        System.out.println("no check");
        return false;
    }

    public int[] findKing(boolean isWhite) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == null) {
                    continue;
                }

                if (board[i][j].isWhite() == isWhite && board[i][j] instanceof King) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public boolean isHuman(boolean isWhite) {
        if (isWhite) return isWhiteHuman;
        else return isBlackHuman;
    }

    public boolean isWhitesTurn() {return isWhitesTurn;}
}