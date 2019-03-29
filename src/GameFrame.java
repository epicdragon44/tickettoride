import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
	private GamePanel gamePanel;

	public GameFrame(String str) {
		super(str);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		gamePanel = new GamePanel();
		add(gamePanel);

		setVisible(true);
	}

	public static void main(String[] args) {
		GameFrame game = new GameFrame("Ticket to Ride");
	}
}
