package me.opmaster.chess;

import me.opmaster.chess.app.page.GUI;
import me.opmaster.chess.game.Board;

public class Main {
    public static void main(String[] Args){
        Board board = new Board();
        board.initialize();

        GUI gui = new GUI(board);
        gui.GUI();
        gui.refresh();

    }
}
