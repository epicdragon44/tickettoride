import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
	private GamePanel gamePanel;

	public GameFrame(String str) throws Exception {
		super(str);
		getContentPane().setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		gamePanel = new GamePanel();
		add(gamePanel);
		setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		GameFrame game = new GameFrame("Ticket to Ride");
	}
}

class BackgroundPanel extends JComponent {
    private Image image;
    private int x, y, length, height;
    public BackgroundPanel(Image im) {
        image = im;
        x = 0;
        y = 225;
        
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
