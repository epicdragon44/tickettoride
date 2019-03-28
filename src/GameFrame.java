import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
	private GamePanel gamePanel;

	public GameFrame(String str) {
		super(str);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gamePanel = new GamePanel();
		add(gamePanel);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}

	public static void main(String[] args) {
		GameFrame game = new GameFrame("Ticket to Ride");
	}
}
