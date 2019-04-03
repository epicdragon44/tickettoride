import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class GamePanel extends JPanel implements MouseListener {
	private GameEngine game;
	private Color red, blue, yellow, green, dgreen, gray, gold;

	public GamePanel() throws IOException {
		blue = new Color(98, 151, 255);
		red = new Color(255, 88, 88);
		yellow = Color.yellow;
		green = new Color(105, 242, 105);
		dgreen = new Color(67, 216, 67);
		gray = new Color(205, 208, 205);
		gold = new Color(218, 218, 4);
		 game = new GameEngine();
		setLayout(null);
		setPreferredSize(new Dimension(1000, 1000));
		setVisible(true);
	}

	public void play() {

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(gold);
		g.fillRect(100, 100, 100, 100);

	}

	public void drawConnection(Node n1, Node n2) {

	}

	public void drawBoard(Graphics g) {

	}

	public void drawTracks(Graphics g) {

	}

	public void drawDecks(Graphics g) {

	}

	public void drawCities(Graphics g) {

	}

	public void drawNumbers(Graphics g) {

	}

	public void drawBackground(Graphics g) {

	}

	public void drawHand(Graphics g) {

	}

	public void drawRankings(Graphics g) {

	}

	public void drawCards(Graphics g) {

	}

	public void drawContracts(Graphics g) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}