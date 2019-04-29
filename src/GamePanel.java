import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GamePanel extends JPanel implements MouseListener {
	private GameEngine game;
	private Color red, blue, yellow, green, dgreen, gray, gold;
	private Font f;
	private ArrayList<Contract> contracts;
	private int lastRoundCount, stage;
	private HashMap<String,String> abrevs;
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
		abrevs=new HashMap<>();
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

		//DANIEL TEST CODE
	}

	public void drawConnection(Node n1, Node n2, Graphics g) {
		for (Track t : n1.getConnections()) {
			if (t.getOtherNode(n1).equals(n2)) {
				if (t.getPlayer()==-1) continue;

				g.setColor(game.players[t.getPlayer()].getColor());

				if (containsDuple(t, n1.getConnections())!=null) {
					Track orig = t;
					Track newT = containsDuple(t, n1.getConnections());
					if (orig.getTime()<newT.getTime()) {
						drawShiftedConnection(orig.getNode1(), orig.getNode2(), g, -7, -7);
					} else {
						drawShiftedConnection(orig.getNode1(), orig.getNode2(), g, 7, 7);
					}
					return;
				}

				int baseX1 = n1.getX();
				int baseX2 = n2.getX();
				int baseY1 = n1.getY();
				int baseY2 = n2.getY();

				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(9, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

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

	public void drawDecks(Graphics g) {
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
			int x = topLeftX+(xShift*i);
			Map.Entry entry = (Map.Entry)it.next();
			for (int j = 0; j < ((Integer)(entry.getValue())); j++) {
				int y = topLeftY+(yShift*j);

				String toAdd;
				if (entry.getKey()==null)
					toAdd = "rainbow";
				else
					toAdd = (entry.getKey()).toString();
				String path = (toAdd+"train.png");
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
		g.drawString(currentPlayer.getTrainsLeft()+"", 160, 805);
	}
	private static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

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
			int y = topLeftY+(i*yShift);

			g.setColor(playerCopy[i].getColor());
			g.fillRect(x, y+10, boxWidth, boxHeight);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Times New Roman", Font.BOLD, 35));
			g.drawString(playerCopy[i].getPoints()+"", x+30, y+40);
			g.setFont(new Font("Times New Roman", Font.BOLD, 30));
			g.drawString(playerCopy[i].getTrainCards().size()+"", trainCardX, y+40);
			g.drawString(playerCopy[i].getContract().size()+"", contractX, y+40);
			g.drawString(playerCopy[i].getTrainsLeft()+"", trainX, y+40);
		}
	}

	public void drawTable(Graphics g) {
		int topLeftX = 1190;
		int topLeftY = 775;
		int xShift = 100;

		for (int i = 0; i < game.getTable().length; i++) {
			int x = topLeftX+(xShift*i);
			int y = topLeftY;

			String toAdd;
			if (game.getTable()[i]==null)
				toAdd = "rainbow";
			else
				toAdd = game.getTable()[i].getColor().toString();
			String path = (toAdd+"train.png");
			try {
				BufferedImage img = ImageIO.read(new File(path));
				img = resize(img, img.getWidth()*2, img.getHeight()*2);
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
		int numOfCols = size/modfactor+1;
		int widthOfBox = (int)(maxWidth/(numOfCols+0.0));
		int heightOfBox = 27;

		int staggerXCnt = 0;
		for (int i = 0; i < size; i++) {
			if (i!=0 && i%modfactor==0)
				staggerXCnt++;

			Contract c = (Contract)iterator.next();

			int x = topLeftX + staggerXCnt*((maxWidth/(numOfCols)));
			int y = topLeftY + ((i%modfactor) * heightOfBox);
			g.drawRect(x, y, widthOfBox, heightOfBox);
			g.drawString(c.getStart()+" to "+c.getEnd(), x+5, y+heightOfBox/2+3);
			g.drawString(c.getValue()+"", (x+widthOfBox)-15, y+heightOfBox/2+3);
		}
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
