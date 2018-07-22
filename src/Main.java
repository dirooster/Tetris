/**
 * Created by Dimasik on 19.07.2018.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Main {

    final String titleProgram = "Tetris";
    static final int blockSize = 25;
    static final int arcRadius = 6;
    static final int fieldWidth = 10;
    static final int fieldHeight = 18;
    final int startLocation = 180;
    final int fieldDx = 7;//для начального окна
    final int fieldDy = 26;
    static final int left = 37;
    final int up = 38;
    static final int right = 39;
    final int down = 40;
    final int showDelay = 400;//задержка анимации
    static final int[][][] shapes = {
            {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {4, 0x00f0f0}}, // I длинная палка
            {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {4, 0xf0f000}}, // O квадратик
            {{1, 0, 0, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0x0000f0}}, // J
            {{0, 0, 1, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xf0a000}}, // L
            {{0, 1, 1, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0x00f000}}, // S
            {{1, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xa000f0}}, // T
            {{1, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xf00000}}  // Z
    };//трехмерный массив заготовок фигур. двоичная заготовка фигуры. каждая палка имеет свой буквенный эквивалент
    //этот массив содержит внутренний размер фигурки, и цвет
    final int[] SCORES = {100, 300, 700, 1500};
    int gameScores = 0;
    static int[][] mine = new int[fieldHeight + 1][fieldWidth];
    JFrame frame;
    Canvas canvasPanel = new Canvas();
    static Random random = new Random();
    static Figure figure = new Figure();
    static boolean gameOver = false;
    static final int[][] gameOverMsg = {
            {0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0},//обеспечивает сообщение в конце игры
            {1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
            {1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0},
            {1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0}};

    public static void main(String[] args) {
        new Main().go();
    }


    void go() {
        frame = new JFrame(titleProgram);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(fieldWidth * blockSize + fieldDx, fieldHeight * blockSize + fieldDy);
        frame.setLocation(startLocation, startLocation);
        frame.setResizable(false);
        canvasPanel.setBackground(Color.black);

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    if (e.getKeyCode() == down) figure.drop();
                    if (e.getKeyCode() == up) figure.rotate();
                    if (e.getKeyCode() == left || e.getKeyCode() == right) figure.move(e.getKeyCode());
                }
                canvasPanel.repaint();
            }
        });
        frame.getContentPane().add(BorderLayout.CENTER, canvasPanel);

        frame.setVisible(true);

        Arrays.fill(mine[fieldHeight], 1);//иницеализируем дно стакана

        while (!gameOver) {
            try {
                Thread.sleep(showDelay);
            } catch (Exception e) {
                e.printStackTrace();
            }
            canvasPanel.repaint();
            checkFilling();
            if (figure.isTouchGround()) {
                figure.leaveOnTheGround();
                figure = new Figure();
                gameOver = figure.isCrossGround();
            } else
                figure.stepDown();
        }
    }


    void checkFilling() {
        int row = fieldHeight - 1;
        int countFillRows = 0;
        while (row > 0) {
            int filled = 1;
            for (int col = 0; col < fieldWidth; col++)
                filled *= Integer.signum(mine[row][col]);
            if (filled > 0) {
                countFillRows++;
                for (int i = row; i > 0; i--) System.arraycopy(mine[i - 1], 0, mine[i], 0, fieldWidth);
            } else
                row--;
        }
        if (countFillRows > 0) {
            gameScores += SCORES[countFillRows - 1];
            frame.setTitle(titleProgram + " : " + gameScores);
        }
    }
}
