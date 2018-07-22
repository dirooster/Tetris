import javax.swing.*;
import java.awt.*;

/**
 * Created by Dimasik on 20.07.2018.
 */
public class Canvas extends JPanel {
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int x = 0; x < Main.fieldWidth; x++)
            for (int y = 0; y < Main.fieldHeight; y++)
                if (Main.mine[y][x] > 0) {
                    g.setColor(new Color(Main.mine[y][x]));
                    g.fill3DRect(x * Main.blockSize + 1, y * Main.blockSize + 1, Main.blockSize - 1, Main.blockSize - 1, true);
                }
        if (Main.gameOver) {
            g.setColor(Color.white);
            for (int y = 0; y < Main.gameOverMsg.length; y++)
                for (int x = 0; x < Main.gameOverMsg[y].length; x++)
                    if (Main.gameOverMsg[y][x] == 1) g.fill3DRect(x * 11 + 18, y * 11 + 160, 10, 10, true);
        } else
            Main.figure.paint(g);
    }
}
