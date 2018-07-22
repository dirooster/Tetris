import java.awt.*;

/**
 * Created by Dimasik on 20.07.2018.
 */
public class Block {
    private int x, y;

    public Block(int x, int y) {
        setX(x);
        setY(y);
    }

    void setX(int x) {
        this.x = x;
    }

    void setY(int y) {
        this.y = y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    void paint(Graphics g, int color) {
        g.setColor(new Color(color));

        g.drawRoundRect(x * Main.blockSize + 1, y * Main.blockSize + 1, Main.blockSize - 2, Main.blockSize - 2, Main.arcRadius, Main.arcRadius);

    }

}
