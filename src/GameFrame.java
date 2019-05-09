import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
	private GamePanel gamePanel;

	public GameFrame(String str) throws Exception {
		super(str);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		gamePanel = new GamePanel();
		add(gamePanel);
		setPreferredSize(new Dimension(1760,1035));
		setResizable(false);
		pack();
	    setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		GameFrame game = new GameFrame("Ticket to Ride");
	}
}
