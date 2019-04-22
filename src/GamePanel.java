import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GamePanel extends JPanel implements MouseListener {
	private GameEngine game;
	private Color red, blue, yellow, green, dgreen, gray, gold;
	private Font f;
	private ArrayList<Contract> contracts;
	private int lastRoundCount, stage;
	private Node[] citySelect;

	public GamePanel() throws Exception {
		blue = new Color(98, 151, 255);
		red = new Color(255, 88, 88);
		yellow = Color.YELLOW;
		green = new Color(105, 242, 105);
		dgreen = new Color(67, 216, 67);
		gray = new Color(205, 208, 205);
		gold = new Color(218, 218, 4);
		game = new GameEngine();
		f = new Font("Brush Script MT", Font.BOLD, 30);
		setLayout(null);
		setPreferredSize(new Dimension(1900, 1000));
		setVisible(true);
		lastRoundCount=0;
		stage=0;
		citySelect=new Node[2];
		contracts=game.drawContract();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1000, 1000);
		//if game not over
		if(stage!=6) 
		{
			drawBackground(g);
			//draw scoreboard(whose turn included) and hand
			//if contract deck has cards
			if(game.getNumContracts()!=0)
			{
				//draw contract deck
			}
			//if train deck has cards
			if(game.haveTrainCards())
			{
				//draw train deck
			}
			//if contract mode or init game
			if(stage==0||stage==3)
			{
				//draw contract selections(those that are not null)
			}
			//else
			else
			{
				//draw face ups(that are not null)
			}
			//if last round > 1
			if(lastRoundCount>1)
			{
				//warn that it is the last round(text above score board)
			}
			//if we want, we can highlight selected cities
		}
		//else
		else
		{
			//draw game end background and fill shit in
		}
		//draw connections
	}

	public void drawConnection(Node n1, Node n2, Graphics g, Color c) {
		for (Track t : n1.getConnections()) {
			if (t.getOtherNode(n1).equals(n2)) {
				// TODO: check for a double track and implement differentiation.
				if (containsDuple(t, n1.getConnections())!=null) {
					Track orig = t;
					Track newT = containsDuple(t, n1.getConnections());
					if (orig.getTime()<newT.getTime()) {
						if (orig.getColor().equals(c))
							drawShiftedConnection(orig.getNode1(), orig.getNode2(), g, -7, -7);
						else
							drawShiftedConnection(newT.getNode1(), newT.getNode2(), g, 7, 7);
					} else {
						if (orig.getColor().equals(c))
							drawShiftedConnection(orig.getNode1(), orig.getNode2(), g, 7, 7);
						else
							drawShiftedConnection(newT.getNode1(), newT.getNode2(), g, -7, -7);
					}
					return;
				}

				g.setColor(t.getColor());
				int baseX1 = n1.getX();
				int baseX2 = n2.getX();
				int baseY1 = n1.getY();
				int baseY2 = n2.getY();

				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

				g2.drawLine(baseX1, baseY1, baseX2, baseY2);
			}
		}
	}
	private void drawShiftedConnection(Node n1, Node n2, Graphics g, int yShift, int xShift) {
		for (Track t : n1.getConnections()) {
			if (t.getOtherNode(n1).equals(n2)) {
				g.setColor(t.getColor());
				int baseX1 = n1.getX()+xShift;
				int baseX2 = n2.getX()+xShift;
				int baseY1 = n1.getY()+yShift;
				int baseY2 = n2.getY()+yShift;

				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

				g2.drawLine(baseX1, baseY1, baseX2, baseY2);
			}
		}
	}
	private Track containsDuple(Track t, ArrayList<Track> tracks) {
		ArrayList<Track> duplicate = new ArrayList<>();
		duplicate.addAll(tracks);
		duplicate.remove(t);
		if (duplicate.contains(t))
			return duplicate.get(duplicate.indexOf(t));
		else
			return null;
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
		try {
			BufferedImage backgroundImg = ImageIO.read(new File("Background.png"));
			g.drawImage(backgroundImg, 0, 0, new ImageObserver() {
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					return false;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		//if game over
		if(stage==6)
		{
			//return
			return;
		}
		//else if init game
		else if(stage==0)
		{
			//if contract selected and index is not greater than size and contract at index is not null
				//give contract at index to player
				//turn index to null in array list(determine with coord bash)
			//if done clicked and at least one null in list
				//if current player is 3
					//change to default stage
				//else
					//reset the contracts list
				//next player
		}
		//else if default stage
		else if(stage==1)
		{
			//if train deck clicked and have traincards left
				//give card to player
				//change to one train card stage
			//else if face up clicked and chosen card isn't null
				//give them card
				//if given card is wild
					//next player
					//if last round > 0
						//decrement last round
				//else
					//change to one train card stage
			//else if contract deck clicked and have contracts left
				//set contracts to draw contracts
				//change to contract selection stage
			//else if city clicked
				//set 0th pos of Node arr to city clicked
				//change to 1 city picked stage
		}
		//else if 1 train card stage
		else if(stage==2)
		{
			//if no train cards left and table deck only has wild and nulls
				//change to default stage
				//next player
				//if last round > 0
					//decrement last round
			//else if train deck clicked and have traincards left
				//give card
				//change to default stage
				//next player
				//if last round > 0
					//decrement last round
			//else if face up clicked and chosen card isn't null
				//give them card(method won't give card if invalid)
				//if given card is wild
					//alert of illegal action
				//else
					//change to default stage
					//next player
					//if last round > 0
						//decrement last round
		}
		//else if contract selection stage
		else if(stage==3)
		{
			//if contract selected and index is not greater than size and contract at index is not null
				//give contract at index to player
				//turn index to null in array list(determine with coord bash)
			//if done clicked and at least one null in list
				//change to default stage
				//next player
				//if last round > 0
					//decrement last round
		}
		//else if 1 city chosen stage
		else if(stage==4)
		{
			//if city clicked on
				//set pos 1 of node array to clicked on city
				//change to 2 cities chosen stage
		}
		//else if 2 cities selected stage
		else if(stage==5)
		{
			//if color stack clicked
				//if stack is wild stack
					//alert invalid input(must click on actual color)
				//else
					//try to claim track
					//if track not claimed
						//alert for invalid input(must restart)
					//else
						//do animation thingy
						//next player
						//if last round > 0
							//decrement last round
					//change to default stage
		}
		//if gamestate says it is last round and last round is 0
		if(game.lastRound()&&lastRoundCount==0)
		{
			//set last round to 5
			lastRoundCount=5;
		}
		//if last round is 1
		if(lastRoundCount==1)
		{
			//change to end game stage
			stage=6;
		}
		//repaint
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
