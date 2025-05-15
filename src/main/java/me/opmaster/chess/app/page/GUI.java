package me.opmaster.chess.app.page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import me.opmaster.chess.app.timer.ChessTimer;
import me.opmaster.chess.game.Board;


public class GUI{

    private Board board;

    public GUI(Board board){
        this.board = board;
    }

    private static JTextField timer_b = new JTextField();
    private static JTextField timer_w  = new JTextField();
    private static ChessTimer white = new ChessTimer(10, 0);
    private static ChessTimer black = new ChessTimer(10, 0);
    private static JButton[][] boardButtons = new JButton[8][8];
    private static JTextField indicator = new JTextField(" White's turn.");

    private int[] feld1 = new int[2];
    private int[] feld2 = new int[2];

    private boolean isFirstSquare = true;
    public void GUI(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Chess");
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setSize(900,1000);
        frame.getContentPane().setBackground(new Color(50,50,50));

        Font font = new Font("Arial", Font.PLAIN, 20);

        indicator.setVisible(true);
        indicator.setEditable(false);
        indicator.setFocusable(false);
        indicator.setFont(font);
        indicator.setBounds(50,25,400,50);
        indicator.setBorder(null);
        indicator.setBackground(new Color(100,100,100));
        indicator.setForeground(new Color(200,200,200));
        frame.add(indicator);

        timer_w.setText(" White: " + white.getFormattedTime());
        timer_w.setVisible(true);
        timer_w.setEditable(false);
        timer_w.setFocusable(false);
        timer_w.setFont(font);
        timer_w.setBackground(new Color(100,100,100));
        timer_w.setForeground(new Color(200,200,200));
        timer_w.setBounds(475,25,175,50);
        timer_w.setBorder(null);
        frame.add(timer_w);

        timer_b.setText(" Black: " + white.getFormattedTime());
        timer_b.setVisible(true);
        timer_b.setEditable(false);
        timer_b.setFocusable(false);
        timer_b.setFont(font);
        timer_b.setBackground(new Color(100,100,100));
        timer_b.setForeground(new Color(200,200,200));
        timer_b.setBounds(675,25,175,50);
        timer_b.setBorder(null);
        frame.add(timer_b);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8,8));
        panel.setBounds(50,100,800,800);
        frame.add(panel);


        int l = 0;
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].setFont(new Font("Monospaced", Font.PLAIN, 65));
                int pos = l;
                if ((pos >= 8 && pos < 16) || (pos >= 24 && pos < 32) || (pos >= 40 && pos < 48) || pos >= 56){
                    pos++;
                }
                if (pos % 2 == 0){
                    boardButtons[i][j].setBackground(new Color(240,236,212));
                }else {
                    boardButtons[i][j].setBackground(new Color(120,148,84));
                }
                boardButtons[i][j].setFocusable(false);
                panel.add(boardButtons[i][j]);
                final int[] buttonPostition = {i,j};
                boardButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isFirstSquare) {
                            if (board.getPiece(buttonPostition[1], buttonPostition[0]) == null) {
                                System.out.println("Square is empty.");
                                return;
                            }
                            feld1 = buttonPostition;
                            isFirstSquare = false;
                        } else {
                            feld2 = buttonPostition;
                            isFirstSquare = true;

                            System.out.println(feld1[1] + " " + feld1[0] + " " + feld2[1] + " " + feld2[0]);
                            board.movePiece(feld1[1], feld1[0], feld2[1], feld2[0]);
                        }
                        refresh();
                        white.stop();
                        black.stop();
                        if (board.isWhitesTurn()) {
                            indicator.setText("Weiß am Zug.");
                            white.start();
                        } else {
                            indicator.setText("Schwarz am Zug.");
                            black.start();
                        }
                    }
                });
                l += 8;
            }
            l -= 63;
        }
        frame.setVisible(true);
        new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (white.getRemainingTime() > 0) {
                    timer_w.setText(" White: " + white.getFormattedTime());
                }else {
                    //Main.isRunning = false;
                    timer_w.setText(" White: " + white.getFormattedTime());
                    indicator.setText(" Black won.");
                }
                if (black.getRemainingTime() > 0) {
                    timer_b.setText(" Black: " + black.getFormattedTime());
                }else {
                    //Main.isRunning = false;
                    timer_b.setText(" Black: " + black.getFormattedTime());
                    indicator.setText(" White won.");
                }
            }
        }).start();
    }
    public void refresh(){
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                try {
                    boardButtons[j][i].setText(String.valueOf(board.getPiece(i, j).getChar()));
                } catch (NullPointerException e) {
                    boardButtons[j][i].setText(null);
                }
            }
        }
    }
}
