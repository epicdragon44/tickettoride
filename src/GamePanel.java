import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {
	private GameEngine game;
	private Color red, blue, yellow, green, dgreen, gray, gold, lblue, lred, lgreen;
	private Font f;
	Node gg;
	private ArrayList<Contract> contracts;
	private int lastRoundCount, stage;
	private Node[] citySelect;

	public GamePanel() throws Exception {
		blue = new Color(98, 151, 255);
		red = new Color(255, 88, 88);
		yellow = Color.YELLOW;
		gg = null;
		int alpha = 127;
		lblue = new Color(90, 255, 178, alpha);
		lred = new Color(255, 65, 65, alpha);
		lgreen = new Color(102, 255, 108, alpha);
		green = new Color(105, 242, 105);
		dgreen = new Color(67, 216, 67);
		gray = new Color(205, 208, 205);
		gold = new Color(218, 218, 4);
		game = new GameEngine(this);
		f = new Font("Brush Script MT", Font.BOLD, 30);
		setLayout(null);
		setPreferredSize(new Dimension(1900, 1000));
		setVisible(true);
		lastRoundCount = 0;
		stage = 0;
		citySelect = new Node[2];
		contracts = game.drawContract();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (stage != 6) {
			drawBackground(g);
			if (gg != null) {
				if (game.isNodeEligible(gg.getX(), gg.getY()) != null) {
					if (game.isNodeEligible(gg.getX(), gg.getY()))
						g.setColor(lgreen);
					else
						g.setColor(lred);
					g.fillOval(gg.getX(), gg.getY(), 19, 19);
					gg = null;
				}
			}
			drawRankings(g);
			drawHand(g);
			if (game.getNumContracts() != 0)
				drawCDeck(g);
			if (game.haveTrainCards())
				drawTDeck(g);
			if (stage == 0 || stage == 3) {
				// draw contract selections(those that are not null)
			}
			else
				drawTable(g);
			if(stage==4||stage==5){
				// highlight none null cities
			}
			if (lastRoundCount > 1) {
				g.setColor(Color.RED);
				g.setFont(f);
				g.drawString("IT IS THE LAST ROUND!", 1285, 45);
			}
		}
		else {
			// draw game end background and fill shit in
		}
		for(Node city:game.getgBoard().cities)
			drawConnections(city,g);
	}

	public void drawConnections(Node n1, Graphics g) {
		for (Track t : n1.getConnections()) {
			if (t.getPlayer() == -1)
				continue;

			g.setColor(game.players[t.getPlayer()].getColor());

			if (containsDuple(t, n1.getConnections()) != null) {
				Track orig = t;
				Track newT = containsDuple(t, n1.getConnections());
				if (orig.getTime() < newT.getTime()) {
					drawShiftedConnection(orig.getNode1(), orig.getNode2(), g, -7, -7);
				} else {
					drawShiftedConnection(orig.getNode1(), orig.getNode2(), g, 7, 7);
				}
				return;
			}

			int baseX1 = n1.getX();
			int baseX2 = t.getOtherNode(n1).getX();
			int baseY1 = n1.getY();
			int baseY2 = t.getOtherNode(n1).getY();

			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(9, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

			g2.drawLine(baseX1, baseY1, baseX2, baseY2);
		}
	}

	private void drawShiftedConnection(Node n1, Node n2, Graphics g, int yShift, int xShift) {
		for (Track t : n1.getConnections()) {
			if (t.getOtherNode(n1).equals(n2)) {
				g.setColor(game.players[t.getPlayer()].getColor());
				int baseX1 = n1.getX() + xShift;
				int baseX2 = n2.getX() + xShift;
				int baseY1 = n1.getY() + yShift;
				int baseY2 = n2.getY() + yShift;

				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(9, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

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

	public void drawCDeck(Graphics g) {
		try {
			BufferedImage backgroundImg = ImageIO.read(new File("contractcard.png"));
			g.drawImage(backgroundImg, 1460, 500, new ImageObserver() {
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					return false;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void drawTDeck(Graphics g) {
		try {
			BufferedImage backgroundImg = ImageIO.read(new File("traincard.png"));
			g.drawImage(backgroundImg, 1210, 500, new ImageObserver() {
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					return false;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void drawBackground(Graphics g) {
		try {
			BufferedImage backgroundImg = ImageIO.read(new File("Background.png"));
			g.drawImage(backgroundImg, 5, 5, new ImageObserver() {
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					return false;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.setColor(Color.LIGHT_GRAY);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

		g2.drawRect(5, 5, 1165, 750);

		g2.setColor(Color.GRAY);
		g2.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

		g2.drawRect(3, 3, 1170, 755);

		int bound = 600; //screw around with this to get content drawn to fit

		g2.drawRect(3, 758, bound, 240);

		g2.drawRect(bound+5, 758, 1168-bound, 240);

		g2.drawRect(1175, 2, 575, 995);
	}

	public void drawHand(Graphics g) {
		Player currentPlayer = game.players[game.currentPlayer];

		int topLeftX = 75;
		int topLeftY = 810;
		int yShift = 10;
		int xShift = 44;

		HashMap<ColorType, Integer> map = currentPlayer.getTrainCards();

		int i = -1;
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			i++;
			int x = topLeftX + (xShift * i);
			Map.Entry entry = (Map.Entry) it.next();
			for (int j = 0; j < ((Integer) (entry.getValue())); j++) {
				int y = topLeftY + (yShift * j);

				String toAdd;
				if (entry.getKey() == null)
					toAdd = "rainbow";
				else
					toAdd = (entry.getKey()).toString();
				String path = (toAdd + "train.png");
				try {
					BufferedImage img = ImageIO.read(new File(path));
					g.drawImage(img, x, y, new ImageObserver() {
						@Override
						public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
							return false;
						}
					});
				} catch (IOException e) {
					System.out.println("Error on drawing traincards");
					e.printStackTrace();
				}
			}
		}

		g.setFont(new Font("Arial", Font.BOLD, 35));
		g.drawString(currentPlayer.getTrainsLeft() + "", 160, 805);
	}

	private static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	private int xLeader = 1237;
	private int yLeader = 110;
	protected boolean moving = true;
	public void drawRankings(Graphics g) {
		Player[] playerCopy = new Player[game.players.length];
		for (int i = 0; i < game.players.length; i++)
			playerCopy[i] = game.players[i];
		Arrays.sort(playerCopy);

		int topLeftX = 1235;
		int topLeftY = 110;

		int yShift = 100;

		int boxWidth = 122;
		int boxHeight = 39;

		int contractX = 1444;
		int trainCardX = 1545;
		int trainX = 1669;

		for (int i = 0; i < playerCopy.length; i++) {
			int x = topLeftX;
			int y = topLeftY + (i * yShift);

			if (i == game.currentPlayer) {
				startAnimationTimer();

				g.setColor(Color.WHITE);
				g.setColor(new Color(255, 255, 255, 125));

				g.fillRoundRect(xLeader-25, yLeader, 500, 75, 25, 25);
			}
			g.setColor(Color.DARK_GRAY);
			g.drawRoundRect(x, y + 15, boxWidth, boxHeight, 10, 10);
			g.setColor(playerCopy[i].getColor());
			g.fillRoundRect(x, y + 15, boxWidth, boxHeight, 10, 10);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Times New Roman", Font.BOLD, 35));
			g.drawString(playerCopy[i].getPoints() + "", x + 45, y + 45);
			g.setFont(new Font("Times New Roman", Font.BOLD, 30));
			g.drawString(getActualSize(playerCopy[i].getTrainCards()) + "", trainCardX, y + 45);
			g.drawString(playerCopy[i].getContract().size() + "", contractX, y + 45);
			g.drawString(playerCopy[i].getTrainsLeft() + "", trainX, y + 45);
		}
	}
	private void moveBox(boolean down) {
		if (down)
			yLeader+=1;
		else
			yLeader-=1;
	}
	private int getActualSize(HashMap<ColorType, Integer> map) {
		int count = 0;
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<ColorType, Integer> next = (Map.Entry<ColorType, Integer>) it.next();
			count+=next.getValue();
		}
		return count;
	}

	public void drawTable(Graphics g) {
		int topLeftX = 1190;
		int topLeftY = 775;
		int xShift = 100;

		for (int i = 0; i < game.getTable().length; i++) {
			int x = topLeftX + (xShift * i);
			int y = topLeftY;

			String toAdd;
			if (game.getTable()[i] == null)
				continue;
			else if(game.getTable()[i].getColor() == null)
				toAdd = "rainbow";
			else
				toAdd = game.getTable()[i].getColor().toString();
			String path = (toAdd + "train.png");
			try {
				BufferedImage img = ImageIO.read(new File(path));
				img = resize(img, img.getWidth() * 2, img.getHeight() * 2);
				g.drawImage(img, x, y, new ImageObserver() {
					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						return false;
					}
				});
			} catch (IOException e) {
				System.out.println("Error on drawing traincards");
				e.printStackTrace();
			}
		}
	}

	public void drawContracts(Graphics g) {
		int modfactor = 7;

		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font("Arial Narrow", Font.ITALIC, 10));

		ContractDeck deck = game.getcDeck();
		Iterator iterator = deck.iterator();
		int size = deck.size();
		int topLeftX = 600;
		int topLeftY = 815;
		int maxWidth = 550;
		int numOfCols = size / modfactor + 1;
		int widthOfBox = (int) (maxWidth / (numOfCols + 0.0));
		int heightOfBox = 27;

		int staggerXCnt = 0;
		for (int i = 0; i < size; i++) {
			if (i != 0 && i % modfactor == 0)
				staggerXCnt++;

			Contract c = (Contract) iterator.next();

			int x = topLeftX + staggerXCnt * ((maxWidth / (numOfCols)));
			int y = topLeftY + ((i % modfactor) * heightOfBox);
			g.drawRect(x, y, widthOfBox, heightOfBox);
			g.drawString(c.getStart() + " to " + c.getEnd(), x + 5, y + heightOfBox / 2 + 3);
			g.drawString(c.getValue() + "", (x + widthOfBox) - 15, y + heightOfBox / 2 + 3);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getX() + " " + e.getY());

		//DANIEL TEST CODE BEGINS
		//game.nextPlayer();
		//DANIEL TEST CODE ENDS
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	public void mouseReleased(MouseEvent e) {}
	/*
	@Override
	public void mouseReleased(MouseEvent e) {
		/*if (game.getgBoard().findNode(e.getX(), e.getY()) != null) {
			gg = game.getgBoard().findNode(e.getX(), e.getY());
		}
		if (stage == 6)
			return;
		else if (stage == 0) {
			// if contract selected
			if()
			{
				// find index
				// if index is not greater than size and contracts at index not null
					// give contract at index to player
					// turn index to null in array list(determine with coord bash)
			}
			// else if done clicked and at least one null in list
			else if()
			{
				if(game.currentPlayer==3)
					stage=1;
				else
					contracts=game.drawContract();
				game.nextPlayer();
			}
		}
		else if(stage==1)
		{
			//if train deck clicked and have traincards left
			if()
			{
				//give card to player
				//change to one train card stage
			}
			//else if face up clicked and chosen card isn't null
			else if()
			{
				//give them card
				//if given card is wild
				if()
				{
					game.nextPlayer();
					if(lastRoundCount > 0)
						lastRoundCount--;
				}
				else
					stage=2;
			}
			//else if contract deck clicked and have contracts left
			else if()
			{
				contracts=game.drawContract();
				stage=3;
			}
			Node n=game.findNode(e.getX(),e.getY());
			else if(n!=null&&game.isNodeEligible(e.getX(), e.getY()))
			{
				citySelect[0]=n;
				stage=4;
			}
		}
		else if (stage == 2) {
			// if no train cards left and table deck only has wild and nulls
			if()
			{
				// change to default stage
				// next player
				// if last round > 0
					// decrement last round
			}
			// else if train deck clicked and have traincards left
			else if()
			{
				// give card
				// change to default stage
				// next player
				// if last round > 0
					// decrement last round
			}
			// else if face up clicked and chosen card isn't null
			else if()
			{
				// give them card(method won't give card if invalid)
				// if given card is wild
					// alert of illegal action
				// else
					// change to default stage
					// next player
					// if last round > 0
						// decrement last round
			}
		}
		else if (stage == 3) {
			// if contract selected and index is not greater than size and contract at index
			if()
			{
				// if is not null
					// give contract at index to player
					// turn index to null in array list(determine with coord bash)
			}
			// if done clicked and at least one null in list
			if()
			{
				// change to default stage
				// next player
				// if last round > 0
					// decrement last round
			}
		}
		else if(stage==4)
		{
			Node n=game.findNode(e.getX(),e.getY());
			if(n!=null&&game.isNodeEligible(e.getX(), e.getY(), citySelect[0]))
			{
				citySelect[1]=n;
				stage=5;
			}
		}
		else if(stage==5)
		{
			ColorType stack=null;
			//if color stack clicked
			if(stack!=null)
			{
				//try to claim track
				//if track not claimed
				if(game.placeTrains())
				{
					//alert for invalid input(must restart)
				}
				else
				{
					//do animation thingy
					game.nextPlayer();
					stage=1;
					if(lastRoundCount>0)
						lastRoundCount--;
				}
			}
			//if stack is wild stack
			if()
			{
				//alert invalid input(must click on actual color)
			}
		}
		if (game.lastRound() && lastRoundCount == 0)
			lastRoundCount = 5;
		if (lastRoundCount == 1)
			stage = 6;
		repaint();*/
	}
	*/

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		if (game.getgBoard().findNode(e.getX(), e.getY()) != null) {
			gg = game.getgBoard().findNode(e.getX(), e.getY());
		}
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	public void startAnimationTimer() {
		Timer animateTimer = new Timer(70, new MoveBox());
		animateTimer.start();
		this.moving = true;
		this.repaint();
	}
	class MoveBox implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int targety = 110 + (game.currentPlayer * 100);
			if (moving) {
				if (yLeader < targety) {
					moveBox(true);
					repaint();
				} else if (yLeader > targety) {
					moveBox(false);
					repaint();
				} else {
					moving = false;
				}
			}
		}
	}
}