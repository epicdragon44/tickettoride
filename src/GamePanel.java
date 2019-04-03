import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel implements MouseListener {
	private GameEngine g;

	public GamePanel() {
		setLayout(null);
		setPreferredSize(new Dimension(1000, 1000));
		setVisible(true);
	}

	public void play() {

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

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