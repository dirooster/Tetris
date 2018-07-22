import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Dimasik on 20.07.2018.
 */
public class Figure {
    private ArrayList<Block> figure = new ArrayList<Block>();//массив обьектов
    int[][] shape = new int[4][4];//массив для вращения фигуры
    private int type, size, color;
    private int x = 3;
    private int y = 0;

    Figure() {
        type = Main.random.nextInt(Main.shapes.length);
        size = Main.shapes[type][4][0];
        color = Main.shapes[type][4][1];
        if (size == 4) y = -1;
        for (int i = 0; i < size; i++) //цикл заполняет массив shape кусочком из общей константы SHAPES
            System.arraycopy(Main.shapes[type][i], 0, shape[i], 0, Main.shapes[type][i].length);
        createFromShape();
    }

    void createFromShape() {
        for (int x = 0; x < size; x++) //двойной цикл проходит по массиву shape на основании этого массива создает фигуру
            for (int y = 0; y < size; y++)
                if (shape[y][x] == 1) figure.add(new Block(x + this.x, y + this.y));
    }

    boolean isTouchWall(int direction) {
        for (Block block : figure) {
            if (direction == Main.left && (block.getX() == 0 || Main.mine[block.getY()][block.getX() - 1] > 0))
                return true;
            if (direction == Main.right && (block.getX() == Main.fieldWidth - 1 || Main.mine[block.getY()][block.getX() + 1] > 0))
                return true;
        }
        return false;
    }

    void move(int direction) {
        if (!isTouchWall(direction)) {
            int dx = direction - 38;
            for (Block block : figure) block.setX(block.getX() + dx);
            x += dx;
        }
    }


    boolean isTouchGround() {
        for (Block block : figure)
            if (Main.mine[block.getY() + 1][block.getX()] > 0)
                return true;
        return false;
    }

    void leaveOnTheGround() {
        for (Block block : figure) Main.mine[block.getY()][block.getX()] = color;
    }

    boolean isCrossGround() {
        for (Block block : figure) if (Main.mine[block.getY()][block.getX()] > 0) return true;
        return false;
    }

    void stepDown() {
        for (Block block : figure) block.setY(block.getY() + 1);
        y++;
    }

    boolean isWrongPosition() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (shape[y][x] == 1) {
                    if (y + this.y < 0) return true;
                    if (x + this.x < 0 || x + this.x > Main.fieldWidth - 1) return true;
                    if (Main.mine[y + this.y][x + this.x] > 0) return true;
                }
        return false;
    }

    void drop() {
        while (!isTouchGround()) stepDown();
    }

    void rotateShape(int direction) {
        for (int i = 0; i < size / 2; i++)
            for (int j = i; j < size - 1 - i; j++)
                if (direction == Main.right) { // clockwise
                    int tmp = shape[size - 1 - j][i];
                    shape[size - 1 - j][i] = shape[size - 1 - i][size - 1 - j];
                    shape[size - 1 - i][size - 1 - j] = shape[j][size - 1 - i];
                    shape[j][size - 1 - i] = shape[i][j];
                    shape[i][j] = tmp;
                } else { // counterclockwise
                    int tmp = shape[i][j];
                    shape[i][j] = shape[j][size - 1 - i];
                    shape[j][size - 1 - i] = shape[size - 1 - i][size - 1 - j];
                    shape[size - 1 - i][size - 1 - j] = shape[size - 1 - j][i];
                    shape[size - 1 - j][i] = tmp;
                }
    }

    void rotate() {
        rotateShape(Main.right);
        if (!isWrongPosition()) {
            figure.clear();
            createFromShape();
        } else
            rotateShape(Main.left);
    }

    void paint(Graphics g) {
        for (Block block : figure) block.paint(g, color);// он проходит по массиву блоков в фигуре
        //при этом каждый обьект входящий в массив фигуры поподает в переменную block.paint и вызываем метод рисования для каждого блочка
    }
}
