import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    public GameFrame() {
        super();
        setSize(new Dimension(1000, 1000));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        gamePanel = new GamePanel();
        add(gamePanel);

        setVisible(true);
    }
    public static void main(String[] args) {

    }
}
