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
	private int[] endData;
	private boolean hoverT,hoverC;

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
		hoverT=false;
		hoverC=false;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (stage != 6) {
			drawBackground(g);
			if (gg != null) {
				if (stage==1&&game.isNodeEligible(gg.getX(), gg.getY()) != null) {
					if (game.isNodeEligible(gg.getX(), gg.getY()))
						g.setColor(lgreen);
					else
						g.setColor(lred);
					g.fillOval(gg.getX()-8, gg.getY()-8, 19, 19);
				}
				else if (stage==4&&game.isNodeEligible(gg.getX(), gg.getY(), citySelect[0]) != null) {
					if (game.isNodeEligible(gg.getX(), gg.getY(),citySelect[0]))
						g.setColor(lgreen);
					else
						g.setColor(lred);
					g.fillOval(gg.getX()-8, gg.getY()-8, 19, 19);
				}
			}
			drawRankings(g);
			drawContracts(g);
			drawHand(g);
			if (game.getNumContracts() != 0)
			{
				if(hoverC&&stage==1)
				{
					g.setColor(lgreen);
					g.fillRoundRect(1462, 506, 236, 153, 10, 10);
				}
				drawCDeck(g);
			}
			if (game.haveTrainCards())
			{
				if(hoverT&&(stage==1||stage==2))
				{
					g.setColor(lgreen);
					g.fillRoundRect(1208, 503, 249, 156, 10, 10);
				}
				drawTDeck(g);
			}
			if (stage == 0 || stage == 3) {
				drawContractSelect(g);
			}
			else
				drawTable(g);
			if(stage==4||stage==5){
				if(citySelect[0]!=null)
				{
					g.setColor(new Color(249,204,22));
					g.fillOval(citySelect[0].getX()-8, citySelect[0].getY()-8, 21, 21);
					g.setColor(Color.BLACK);
					g.drawOval(citySelect[0].getX()-8, citySelect[0].getY()-8, 21, 21);
				}
				if(citySelect[1]!=null)
				{
					g.setColor(new Color(249,204,22));
					g.fillOval(citySelect[1].getX()-8, citySelect[1].getY()-8, 21, 21);
					g.setColor(Color.BLACK);
					g.drawOval(citySelect[1].getX()-8, citySelect[1].getY()-8, 21, 21);
				}
			}
			if (lastRoundCount > 1) {
				g.setColor(Color.RED);
				g.setFont(f);
				g.drawString("IT IS THE LAST ROUND!", 1285, 45);
			}
		}
		else {
			drawEndGame(g);
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
					drawShiftedConnection(newT.getNode1(), newT.getNode2(), g, 7, 7);
				} else {
					drawShiftedConnection(orig.getNode1(), orig.getNode2(), g, 7, 7);
					drawShiftedConnection(newT.getNode1(), newT.getNode2(), g, -7, -7);
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
	
	public void drawContractSelect(Graphics g)
	{
		Font font=new Font("Arial Narrow", Font.ITALIC, 20);
		int xA=1300;
		int yA=700;
		for(int i=0;i<contracts.size();i++)
		{
			if(contracts.get(i)==null)
				continue;
			g.drawRect(xA, yA+i*80, 350, 60);
			String text=contracts.get(i).getStart()+" to "+contracts.get(i).getEnd();
			FontMetrics metrics = g.getFontMetrics(font);
		    int x = xA + (350 - metrics.stringWidth(text)) / 2;
		    int y = (yA+i*80) + ((30 - metrics.getHeight()) / 2) + metrics.getAscent();
		    g.setColor(Color.LIGHT_GRAY);
		    g.setFont(font);
		    g.drawString(text, x, y);
		    x = xA + (350 - metrics.stringWidth(text)) / 2;
		    y = (yA+30+i*80) + ((30 - metrics.getHeight()) / 2) + metrics.getAscent();
		    g.setColor(Color.LIGHT_GRAY);
		    g.setFont(font);
		    g.drawString(text, x, y);
		}
		g.setColor(new Color(57,229,109));
		g.fillRoundRect(1435, 980, 80, 40, 10, 10);
		g.setColor(Color.black);
		g.drawRoundRect(1435, 980, 80, 40, 10, 10);
		font=new Font("Arial Narrow", Font.BOLD+Font.ITALIC, 20);
		FontMetrics metrics = g.getFontMetrics(font);
	    int x = 1435 + (80 - metrics.stringWidth("Done")) / 2;
	    int y = 980 + ((40 - metrics.getHeight()) / 2) + metrics.getAscent();
	    g.setColor(Color.LIGHT_GRAY);
	    g.setFont(font);
	    g.drawString("Done", x, y);
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

	public void drawEndGame(Graphics g)
	{
		
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
		try {
			BufferedImage img = ImageIO.read(new File(getCardPath(game.getTable()[0])));
			g.drawImage(img, 1252, 705, new ImageObserver() {
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					return false;
				}
			});
		} catch (IOException e) {
			System.out.println("Error on drawing traincards");
			e.printStackTrace();
		}
		try {
			BufferedImage img = ImageIO.read(new File(getCardPath(game.getTable()[1])));
			g.drawImage(img, 1519, 705, new ImageObserver() {
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					return false;
				}
			});
		} catch (IOException e) {
			System.out.println("Error on drawing traincards");
			e.printStackTrace();
		}
		try {
			BufferedImage img = ImageIO.read(new File(getCardPath(game.getTable()[2])));
			g.drawImage(img, 1385, 825, new ImageObserver() {
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					return false;
				}
			});
		} catch (IOException e) {
			System.out.println("Error on drawing traincards");
			e.printStackTrace();
		}
		try {
			BufferedImage img = ImageIO.read(new File(getCardPath(game.getTable()[3])));
			g.drawImage(img, 1252, 952, new ImageObserver() {
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					return false;
				}
			});
		} catch (IOException e) {
			System.out.println("Error on drawing traincards");
			e.printStackTrace();
		}
		try {
			BufferedImage img = ImageIO.read(new File(getCardPath(game.getTable()[4])));
			g.drawImage(img, 1519, 952, new ImageObserver() {
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
	
	private String getCardPath(TrainCard t)
	{
		if (t == null)
			return null;
		else if(t.getColor() == null)
			return "rainbowtrain2.png";
		else
			return t.getColor().toString() + "train2.png";
	}

	public void drawContracts(Graphics g) {
		int modfactor = 7;

		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font("Arial Narrow", Font.ITALIC, 10));

		ArrayList<Contract> deck = game.players[game.currentPlayer].getContract();
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
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		/*if (game.getgBoard().findNode(e.getX(), e.getY()) != null) {
			gg = game.getgBoard().findNode(e.getX(), e.getY());
		}
		if (stage == 6)
			return;
		else if (stage == 0) {
			int ind=-1;
			if(e.getX()>=1300&&e.getX()<=1650) 
			{
				if(e.getY()>=700&&e.getY()<=760)
					ind=0;
				else if(e.getY()>=780&&e.getY()<=840)
					ind=1;
				else if(e.getY()>=860&&e.getY()<=920)
					ind=2;
			}
			if(ind!=-1&&ind<contracts.size()&&contracts.get(ind)!=null)
			{
				game.takeContract(contracts.get(ind));
				contracts.set(ind,null);
			}
			else if(e.getX()>=1435&&e.getX()<=1515&&e.getY()>=980&&e.getY()<=1020&&contracts.contains(null))
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
			if(e.getX()>=1218&&e.getX()<=1447&&e.getY()>=513&&e.getY()<=649&&game.haveTrainCards())
			{
				game.drawTrainCard(-1, false);
				stage=2;
			}
			else if(e.getX()>=1472&&e.getX()<=1688&&e.getY()>=516&&e.getY()<=649&&game.getNumContracts()>0)
			{
				contracts=game.drawContract();
				stage=3;
			}
			Node n=game.findNode(e.getX(),e.getY());
			if(n!=null&&game.isNodeEligible(e.getX(), e.getY()))
			{
				citySelect[0]=n;
				stage=4;
			}
			int ind=-1;
			if(e.getX()>=1252&&e.getX()<=1430&&e.getY()>=705&&e.getY()<=818)
				ind=0;
			else if(e.getX()>=1519&&e.getX()<=1697&&e.getY()>=705&&e.getY()<=818)
				ind=1;
			else if(e.getX()>=1385&&e.getX()<=1563&&e.getY()>=825&&e.getY()<=938)
				ind=2;
			else if(e.getX()>=1252&&e.getX()<=1430&&e.getY()>=952&&e.getY()<=1065)
				ind=3;
			else if(e.getX()>=1519&&e.getX()<=1697&&e.getY()>=952&&e.getY()<=1065)
				ind=4;
			if(ind!=-1&&game.getTable()[ind]!=null)
			{
				if(game.drawTrainCard(ind, false))
				{
					game.nextPlayer();
					if(lastRoundCount > 0)
						lastRoundCount--;
				}
				else
					stage=2;
			}
		}
		else if (stage == 2) {
			if(!(game.haveTable()||game.haveTrainCards()))
			{
				game.nextPlayer();
				stage=1;
				if(lastRoundCount>0)
					lastRoundCount--;
			}
			else if(e.getX()>=1218&&e.getX()<=1447&&e.getY()>=513&&e.getY()<=649&&game.haveTrainCards())
			{
				game.drawTrainCard(-1, true);
				game.nextPlayer();
				stage=1;
				if(lastRoundCount>0)
					lastRoundCount--;
			}
			int ind=-1;
			if(e.getX()>=1252&&e.getX()<=1430&&e.getY()>=705&&e.getY()<=818)
				ind=0;
			else if(e.getX()>=1519&&e.getX()<=1697&&e.getY()>=705&&e.getY()<=818)
				ind=1;
			else if(e.getX()>=1385&&e.getX()<=1563&&e.getY()>=825&&e.getY()<=938)
				ind=2;
			else if(e.getX()>=1252&&e.getX()<=1430&&e.getY()>=952&&e.getY()<=1065)
				ind=3;
			else if(e.getX()>=1519&&e.getX()<=1697&&e.getY()>=952&&e.getY()<=1065)
				ind=4;
			if(ind!=-1&&game.getTable()[ind]!=null)
			{
				if(game.drawTrainCard(ind, true))
					JOptionPane.showMessageDialog(null, "MOVE INVALID. PLEASE PICK A NON-WILD CARD.", "Input Error", JOptionPane.INFORMATION_MESSAGE);
				else
				{
					game.nextPlayer();
					stage=1;
					if(lastRoundCount>0)
						lastRoundCount--;
				}
			}
		}
		else if (stage == 3) {
			int ind=-1;
			if(e.getX()>=1300&&e.getX()<=1650) 
			{
				if(e.getY()>=700&&e.getY()<=760)
					ind=0;
				else if(e.getY()>=780&&e.getY()<=840)
					ind=1;
				else if(e.getY()>=860&&e.getY()<=920)
					ind=2;
			}
			if(ind!=-1&&ind<contracts.size()&&contracts.get(ind)!=null)
			{
				game.takeContract(contracts.get(ind));
				contracts.set(ind,null);
			}
			else if(e.getX()>=1435&&e.getX()<=1515&&e.getY()>=980&&e.getY()<=1020&&contracts.contains(null))
			{
				game.nextPlayer();
				stage=1;
				if(lastRoundCount>0)
					lastRoundCount--;
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
			if(e.getY()>=810&&e.getY()>=1081)
			{
				if(e.getX()>=75&&e.getX()>=117)
					stack=ColorType.BLACK;
				else if(e.getX()>=119&&e.getX()>=161)
					stack=ColorType.GREEN;
				else if(e.getX()>=163&&e.getX()>=205)
					stack=ColorType.PINK;
				else if(e.getX()>=207&&e.getX()>=249)
					stack=ColorType.WHITE;
				else if(e.getX()>=295&&e.getX()>=337)
					stack=ColorType.ORANGE;
				else if(e.getX()>=339&&e.getX()>=381)
					stack=ColorType.RED;
				else if(e.getX()>=383&&e.getX()>=425)
					stack=ColorType.BLUE;
				else if(e.getX()>=427&&e.getX()>=469)
					stack=ColorType.YELLOW;
			}
			if(stack!=null)
			{
				if(!game.placeTrain(citySelect[0], citySelect[1], stack))
				{
					JOptionPane.showMessageDialog(null, "MOVE INVALID. PLEASE RESTART TURN.", "Input Error", JOptionPane.INFORMATION_MESSAGE);
					stage=1;
				}
				else
				{
					game.nextPlayer();
					stage=1;
					if(lastRoundCount>0)
						lastRoundCount--;
				}
			}
			if(e.getX()>=251&&e.getX()<=293&&e.getY()>=810&&e.getY()<=1081)
				JOptionPane.showMessageDialog(null, "MOVE INVALID. PLEASE CLICK ON ACTUAL COLOR.", "Input Error", JOptionPane.INFORMATION_MESSAGE);
		}
		if (game.lastRound() && lastRoundCount == 0)
			lastRoundCount = 5;
		if (lastRoundCount == 1)
		{
			stage = 6;
			endData=game.endGame();
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		if (game.getgBoard().findNode(e.getX(), e.getY()) != null)
			gg = game.getgBoard().findNode(e.getX(), e.getY());
		else
			gg=null;
		if(e.getX()>=1218&&e.getX()<=1447&&e.getY()>=513&&e.getY()<=649)
			hoverT=true;
		else
			hoverT=false;
		if(e.getX()>=1472&&e.getX()<=1688&&e.getY()>=516&&e.getY()<=649)
			hoverC=true;
		else
			hoverC=false;
		//add contract selection(including done)(not null)(stage 0 and stage 3)
		//add color stacks(only stage 5)
		//add table deck(not null)(stage 1 and stage [red if wild]2)
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