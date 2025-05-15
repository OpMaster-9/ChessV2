package me.opmaster.chess.app.page;

import me.opmaster.chess.game.Board;
import me.opmaster.chess.game.pieces.*;

import javax.swing.*;

public class promotion {
    public static Piece promotion(boolean isHuman, boolean isWhite, Board board) {
        if (!isHuman) return new Queen(isWhite, board);

        int output = 0;
        JOptionPane.showMessageDialog(null, "Promotion!", "Chess-Promotion", JOptionPane.INFORMATION_MESSAGE);
        String piece = (String) JOptionPane.showInputDialog(
                null,
                "What do you want to promote to?",
                "Chess-Promotion",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Queen", "Rook", "Bishop", "Knight"},
                "Queen"
        );
        switch (piece) {
            case "Queen":
                return new Queen(isWhite, board);
            case "Rook":
                return new Rook(isWhite, board);
            case "Bishop":
                return new Bishop(isWhite, board);
            case "Knight":
                return new Knight(isWhite, board);
        }

        return null;

    }
}
