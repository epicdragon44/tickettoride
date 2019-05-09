import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	private GameEngine game;
	private Color red, blue, yellow, green, dgreen, gray, gold, lblue, lred, lgreen;
	private ColorType hoverStack;
	private HashMap<ColorType,ColorType> cMap;
	private Font f;
	private Node gg;
	private double lineX,lineY;
	private ArrayList<Contract> contracts;
	private int lastRoundCount, hoverConStart, hoverCon, numCalled, numLooped, numMoved;
	protected int stage;
	private Node[] citySelect;
	private int[][] endData;
	private boolean hoverT,hoverC, drawDirections;
	private Track lastPlaced;
	private Timer animateTimer, animateTimer2;
	private BufferedImage bg;

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
		game = new GameEngine();
		cMap=new HashMap<ColorType,ColorType>();
		cMap.put(new ColorType(92,97,92,230),ColorType.BLACK);
		cMap.put(new ColorType(154,196,70,230),ColorType.GREEN);
		cMap.put(new ColorType(205,135,173,230),ColorType.PINK);
		cMap.put(new ColorType(255,255,255,230),ColorType.WHITE);
		cMap.put(new ColorType(210,158,53,230),ColorType.ORANGE);
		cMap.put(new ColorType(206,66,49,230),ColorType.RED);
		cMap.put(new ColorType(4,160,211,230),ColorType.BLUE);
		cMap.put(new ColorType(230,230,77,230),ColorType.YELLOW);
		cMap.put(null, null);
		f = new Font("Brush Script MT", Font.BOLD, 30);
		setLayout(null);
		setBackground(Color.lightGray);
		setPreferredSize(new Dimension(1754, 1000));
		setVisible(true);
		lastRoundCount = 0;
		stage = 1;
		numCalled=0;
		numLooped=0;
		numMoved=-1;
		citySelect = new Node[2];
		contracts = game.drawContract(5);
		hoverT=false;
		hoverC=false;
		drawDirections = true;
		hoverStack=ColorType.BLACK;
		hoverConStart=-1;
		hoverCon=-1;
		bg=null;
		lineX=0;
		lineY=0;
		lastPlaced=null;
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}

	public void addNotify() {
		super.addNotify();
		requestFocus();
	}

	@Override
	public void paint(Graphics g) {
		if(lastPlaced==null||numLooped==0||moving)
			super.paint(g);
		if(numMoved==1)
		{
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(bg, 0, 0, this);
			drawBox(g);
			if(lastPlaced==null)
				return;
		}
		if(lastPlaced!=null&&numLooped>0)
		{
			animateLine(g);
			return;
		}
		if (stage != 6) {
			if ((game.getNonWildNum()+game.getNonWildTable()<3)&&(game.getWildNum()+game.getWildTable()>2))
			{
				stage = 6;
				endData=game.endGame();
				JOptionPane.showMessageDialog(null, "Due to the impossibilty of a proper table, the game is over.", "Input Error", JOptionPane.INFORMATION_MESSAGE);
				repaint();
			}
			drawBackground(g);
			if (gg != null) {
				if ((stage==1)&&(game.isNodeEligible(gg.getX(), gg.getY()) != null)) {
					if (game.isNodeEligible(gg.getX(), gg.getY()))
						g.setColor(lgreen);
					else
						g.setColor(lred);
					g.fillOval(gg.getX()-8, gg.getY()-8, 19, 19);
				}
				else if ((stage==4)&&(game.isNodeEligible(gg.getX(), gg.getY(), citySelect[0]) != null)) {
					if (game.isNodeEligible(gg.getX(), gg.getY(),citySelect[0]))
						g.setColor(lgreen);
					else
						g.setColor(lred);
					g.fillOval(gg.getX()-9, gg.getY()-9, 19, 19);
				}
			}
			drawRankings(g);
			drawContracts(g);
			drawHand(g);
			if (game.getNumContracts() != 0)
			{
				if(hoverC&&stage==1&&numMoved!=0)
				{
					g.setColor(lgreen);
					g.fillRoundRect(1462, 506, 236, 153, 10, 10);
				}
				drawCDeck(g);
			}
			if (game.haveTrainCards())
			{
				if(hoverT&&(stage==1||stage==2)&&numMoved!=0)
				{
					g.setColor(lgreen);
					g.fillRoundRect(1208, 503, 249, 156, 10, 10);
				}
				drawTDeck(g);
			}
			if (stage == 0) 
			{
				if(hoverConStart!=-1&&hoverConStart<contracts.size()&&contracts.get(hoverConStart)!=null&&numMoved!=0)
				{
					g.setColor(lgreen);
					g.fillRoundRect(1290, 650+hoverConStart*60, 370, 55, 10, 10);
				}
				drawContractSelectStart(g);
			}
			else if (stage == 3) 
			{
				if(hoverCon!=-1&&hoverCon<contracts.size()&&contracts.get(hoverCon)!=null)
				{
					g.setColor(lgreen);
					g.fillRoundRect(1290, 690+hoverCon*80, 370, 80, 10, 10);
				}
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
				drawLastRoundNotice(g);
			}
			if(stage==5)
				if((hoverStack==null||!hoverStack.equals(ColorType.BLACK))&&game.getCardCount(cMap.get(hoverStack))>0)
					drawConnection(g);
		}
		else {
			drawEndGame(g);
		}

		//draw instructions
		if (drawDirections) {
			g.setColor(Color.BLACK);
			g.fillRect(8, 698, 570, 750 - 695);
			try {
				BufferedImage backgroundImg = ImageIO.read(new File("screenshot-terminal.png"));
				g.drawImage(backgroundImg, 7, 695, new ImageObserver() {
					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						return false;
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.setColor(Color.WHITE);
			g.fillOval(35, 700, 13, 13);
			g.fillOval(52, 700, 13, 13);

			g.setColor(Color.GREEN);
			if (stage == 0) {
				g.setFont(new Font("Consolas", Font.PLAIN, 15));
				g.drawString("Select at least three contracts, then hit done.", 15, 738);
			} else if (stage == 1) {
				g.setFont(new Font("Consolas", Font.PLAIN, 10));
				g.drawString("Click on two nodes to select the track in between. ", 15, 735);
				g.drawString("Or, select a contract or train card from the right.", 15, 745);
			} else if (stage == 2) {
				g.setFont(new Font("Consolas", Font.PLAIN, 10));
				g.drawString("Select a train card from the deck. ", 15, 735);
				g.drawString("Or, select a non-wild face-up card.", 15, 745);
			} else if (stage == 3) {
				g.setFont(new Font("Consolas", Font.PLAIN, 15));
				g.drawString("Select at least one contract, then hit done.", 15, 738);
			} else if (stage == 4) {
				g.setFont(new Font("Consolas", Font.PLAIN, 10));
				g.drawString("Select a 2nd node. ", 15, 735);
				g.drawString("Or, hit Esc to cancel selected node.", 15, 745);
			} else if (stage == 5) {
				g.setFont(new Font("Consolas", Font.PLAIN, 10));
				g.drawString("Select which card in your hand to use on the track. ", 15, 735);
				g.drawString("Or, hit Esc to cancel selected nodes.", 15, 745);
			}
			if(lastRoundCount>1)
				drawLastRoundNotice(g);
		}
		else {
			g.setColor(Color.GRAY);
			g.fillRect(7, 715, 260, 25);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Consolas", Font.PLAIN, 15));
			g.drawString("Click here to see Instructions", 15, 730);
		}

		ArrayList<Track> visited=new ArrayList<Track>();
		for(Node city:game.getgBoard().cities)
			drawConnections(city,visited,g);
		if(numMoved==0)
		{
			try
			{
				bg=new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_INT_RGB);
				File outputfile = new File("saved.png");
			    ImageIO.write(bg, "png", outputfile);
				System.out.println("gay");
			} catch(Exception e) {}
		}
		else if(numMoved==-1)
			drawBox(g);
	}

	public void drawLastRoundNotice(Graphics g) {
		g.setColor(Color.GREEN);
		g.setFont(new Font("Consolas", Font.PLAIN, 15));
		g.drawString("IT IS THE LAST ROUND!", 350, 740);
	}

	public void drawConnections(Node n1, ArrayList<Track> visited, Graphics g) {
		for (Track t : n1.getConnections()) {
			if (t.getPlayer() == -1||contains(t,visited))
				continue;
			if(lastPlaced!=null&&lastPlaced.animateEquals(t))
				continue;
			g.setColor(game.players[t.getPlayer()].getColor());
			float z=(float)Math.sqrt(Math.pow(t.getX2()-t.getX1(), 2)+Math.pow(t.getY2()-t.getY1(), 2))/t.getCost();
			float[] dash=new float[2];
			dash[0]=(9.5f*z)/10;
			dash[1]=(0.65f*z)/10;
			int baseX1 = t.getX1();
			int baseX2 = t.getX2();
			int baseY1 = t.getY1();
			int baseY2 = t.getY2();

			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(13, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0,dash,0));

			g2.drawLine(baseX1, baseY1, baseX2, baseY2);
			visited.add(t);
		}
	}
	
	private boolean contains(Track t,ArrayList<Track> v)
	{
		for(Track tr:v)
			if(tr.animateEquals(t))
				return true;
		return false;
	}
	
	public void drawConnection(Graphics g) {
		Track t=game.findTrack(citySelect[0],citySelect[1]);
		if(t==null)
			return;
		Graphics2D g2 = (Graphics2D) g;
		if(hoverStack==null||hoverStack.equals(Color.BLACK)) {
			float[] f={0.14285714f,(float)(0.14285714*2),(float)(0.14285714*3),(float)(0.14285714*4),(float)(0.14285714*5),(float)(0.14285714*6),1.0f};
			Color[] col={new Color(255,0,0,230),new Color(13,139,242,230),new Color(0,0,255,230),
					new Color(0,255,255,230),new Color(0,255,64,230),new Color(255,255,0,230),new Color(255,0,0,230)};
			g2.setPaint(new LinearGradientPaint(t.getX1(),t.getY1(),t.getX2(),t.getY2(),f,col));
		}
		else
			g2.setColor(hoverStack);

		int baseX1 = t.getX1();
		int baseX2 = t.getX2();
		int baseY1 = t.getY1();
		int baseY2 = t.getY2();

		g2.setStroke(new BasicStroke(13, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

		g2.drawLine(baseX1, baseY1, baseX2, baseY2);
	}
	
	public void drawContractSelect(Graphics g)
	{
		Font font=new Font("Arial Narrow", Font.ITALIC, 25);
		int xA=1300;
		int yA=700;
		for(int i=0;i<contracts.size();i++)
		{
			if(contracts.get(i)==null)
				continue;
			if(i==hoverCon)
				g.setColor(Color.DARK_GRAY);
			else
				g.setColor(Color.LIGHT_GRAY);
			g.drawRect(xA, yA+i*80, 350, 60);
			String text=contracts.get(i).getStart()+" to "+contracts.get(i).getEnd();
			FontMetrics metrics = g.getFontMetrics(font);
		    int x = xA + (350 - metrics.stringWidth(text)) / 2;
		    int y = (yA+i*80) + ((30 - metrics.getHeight()) / 2) + metrics.getAscent();
		    g.setFont(font);
		    g.drawString(text, x, y);
		    text="Value : "+contracts.get(i).getValue();
		    x = xA + (350 - metrics.stringWidth(text)) / 2;
		    y = (yA+30+i*80) + ((30 - metrics.getHeight()) / 2) + metrics.getAscent();
		    g.setFont(font);
		    g.drawString(text, x, y);
		}
		g.setColor(new Color(57,229,109));
		g.fillRoundRect(1435, 940, 80, 40, 10, 10);
		g.setColor(Color.BLACK);
		g.drawRoundRect(1435, 940, 80, 40, 10, 10);
		font=new Font("Arial Narrow", Font.BOLD+Font.ITALIC, 20);
		FontMetrics metrics = g.getFontMetrics(font);
	    int x = 1435 + (80 - metrics.stringWidth("Done")) / 2;
	    int y = 940 + ((40 - metrics.getHeight()) / 2) + metrics.getAscent();
	    g.setColor(Color.GRAY);
	    g.setFont(font);
	    g.drawString("Done", x, y);
	}
	
	public void drawContractSelectStart(Graphics g)
	{
		Font font=new Font("Arial Narrow", Font.ITALIC, 20);
		int xA=1300;
		int yA=660;
		for(int i=0;i<contracts.size();i++)
		{
			if(contracts.get(i)==null)
				continue;
			if(i==hoverConStart)
				g.setColor(Color.DARK_GRAY);
			else
				g.setColor(Color.LIGHT_GRAY);
			g.drawRect(xA, yA+i*60, 350, 35);
			String text=contracts.get(i).getStart()+" to "+contracts.get(i).getEnd()+"  Value : "+contracts.get(i).getValue();
			FontMetrics metrics = g.getFontMetrics(font);
		    int x = xA + (350 - metrics.stringWidth(text)) / 2;
		    int y = (yA+i*60) + ((30 - metrics.getHeight()) / 2) + metrics.getAscent();
		    g.setFont(font);
		    g.drawString(text, x, y);
		}
		g.setColor(new Color(57,229,109));
		g.fillRoundRect(1435, 950, 80, 40, 10, 10);
		g.setColor(Color.BLACK);
		g.drawRoundRect(1435, 950, 80, 40, 10, 10);
		font=new Font("Arial Narrow", Font.BOLD+Font.ITALIC, 20);
		FontMetrics metrics = g.getFontMetrics(font);
	    int x = 1435 + (80 - metrics.stringWidth("Done")) / 2;
	    int y = 950 + ((40 - metrics.getHeight()) / 2) + metrics.getAscent();
	    g.setColor(Color.GRAY);
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

		int bound = 575; //screw around with this to get content drawn to fit

		g2.drawRect(3, 758, bound, 240);

		g2.drawRect(bound+5, 758, 1168-bound, 240);

		g2.drawRect(1175, 2, 575, 995);
	}

	public void drawEndGame(Graphics g)
	{
		g.clearRect(1185, 12, 1740-1185, 983-12);
		g.clearRect(9, 764, 1742-9, 987-764);
		try {
			BufferedImage img = ImageIO.read(new File("endgamebase.png"));;
			img = resize(img, (int)(img.getWidth()*1.5), (int)(img.getHeight()*1.5));
			g.drawImage(img, 5, 5, new ImageObserver() {
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					return false;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		int[][] results = this.endData;
		Graphics2D g2 = (Graphics2D) g;

		//draw contract payouts
		int contractPayoutX = 75;
		int contractPayoutY = 860;
		int contractPayoutXShift = 100;
		for (int i = 0; i < game.players.length; i++) {
			g.setColor(game.players[i].getColor());
			g.fillRoundRect(contractPayoutX + i * contractPayoutXShift, contractPayoutY, 80, 80, 15, 15);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 35));
			String toDraw = results[0][i]+"";
			if (results[0][i] > -1)
				toDraw = "+"+toDraw;
			g.drawString(toDraw, contractPayoutX + i * contractPayoutXShift + 10, contractPayoutY + 50);
			g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			g2.setColor(Color.BLACK);
			g2.drawRoundRect(contractPayoutX + i * contractPayoutXShift, contractPayoutY, 80, 80, 15, 15);
		}
		//end drawing contract payouts

		g.setFont(new Font("Times new Roman", Font.BOLD, 40));
		//draw longest path
		if(results[1].length>1)
		{
			Color[] col=new Color[results[1].length];
			float[] f=new float[results[1].length];
			for(int i=0;i<results[1].length;i++)
			{
				col[i]=game.players[results[1][i]].getColor();
				f[i]=(float)((1.0/results[1].length)*(i+1));
			}
			g2.setPaint(new LinearGradientPaint(600f,860f,725f,985f,f,col));
			g2.fillRoundRect(600, 860, 125, 125, 25, 25);
		}
		else
		{
			g.setColor(game.players[results[1][0]].getColor());
			g.fillRoundRect(600, 860, 125, 125, 25, 25);
		}
		g.setColor(Color.BLACK);
		g.drawString("+10", 630, 930);
		g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(600, 860, 125, 125, 25, 25);
		//end drawing longest path

		//draw globetrotter
		if(results[2].length>1)
		{
			Color[] col=new Color[results[2].length];
			float[] f=new float[results[2].length];
			for(int i=0;i<results[2].length;i++)
			{
				col[i]=game.players[results[2][i]].getColor();
				f[i]=(float)((1.0/results[2].length)*(i+1));
			}
			g2.setPaint(new LinearGradientPaint(970f,860f,1095f,985f,f,col));
			g2.fillRoundRect(970, 860, 125, 125, 25, 25);
		}
		else
		{
			g.setColor(game.players[results[2][0]].getColor());
			g.fillRoundRect(970, 860, 125, 125, 25, 25);
		}
		g.setColor(Color.BLACK);
		g.drawString("+15", 1000, 930);
		g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(970, 860, 125, 125, 25, 25);
		//end draw globetrotter

		//draw scoreboard
		Player[] playerCopy = new Player[game.players.length];
		for (int i = 0; i < game.players.length; i++)
			playerCopy[i] = game.players[i];
		Arrays.sort(playerCopy);
		int scoreBoardX = 1364;
		int scoreBoardY = 126;
		int	scoreBoardShift = 100;
		int width = 200;
		int height = 50;
		for (int i = 0; i < game.players.length; i++) {
			int x = scoreBoardX;
			int y = scoreBoardY + i * scoreBoardShift;
			g.setColor(playerCopy[i].getColor());
			g.fillRoundRect(x, y, width, height, 15, 15);
			String pts = playerCopy[i].getPoints()+"";
			g.setColor(Color.BLACK);
			g.drawString(pts, x+75, y+35);

			g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			g2.setColor(Color.BLACK);
			g2.drawRoundRect(x, y, width, height, 15, 15);
		}
		//end drawing scoreboard
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

		g.setColor(Color.black);
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

	private double xLeader = 1237,yLeader = 110,targety,change;
	protected boolean moving = false;
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
	private void moveBox(double change) {
		yLeader+=change;
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
			if(game.getTable()[0]!=null)
			{
				BufferedImage img = ImageIO.read(new File(getCardPath(game.getTable()[0])));
				img = resize(img, (int)(img.getWidth()*0.5), (int)(img.getHeight()*0.5));
				g.drawImage(img, 1300, 700, new ImageObserver() {
					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						return false;
					}
				});
			}
		} catch (IOException e) {
			System.out.println("Error on drawing traincards");
			e.printStackTrace();
		}
		try {
			if(game.getTable()[1]!=null)
			{
				BufferedImage img = ImageIO.read(new File(getCardPath(game.getTable()[1])));
				img = resize(img, (int)(img.getWidth()*0.5), (int)(img.getHeight()*0.5));
				g.drawImage(img, 1500, 700, new ImageObserver() {
					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						return false;
					}
				});
			}
		} catch (IOException e) {
			System.out.println("Error on drawing traincards");
			e.printStackTrace();
		}
		try {
			if(game.getTable()[2]!=null)
			{
				BufferedImage img = ImageIO.read(new File(getCardPath(game.getTable()[2])));
				img = resize(img, (int)(img.getWidth()*0.5), (int)(img.getHeight()*0.5));
				g.drawImage(img, 1400, 750, new ImageObserver() {
					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						return false;
					}
				});
			}
		} catch (IOException e) {
			System.out.println("Error on drawing traincards");
			e.printStackTrace();
		}
		try {
			if(game.getTable()[3]!=null)
			{
				BufferedImage img = ImageIO.read(new File(getCardPath(game.getTable()[3])));
				img = resize(img, (int)(img.getWidth()*0.5), (int)(img.getHeight()*0.5));
				g.drawImage(img, 1300, 800, new ImageObserver() {
					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						return false;
					}
				});
			}
		} catch (IOException e) {
			System.out.println("Error on drawing traincards");
			e.printStackTrace();
		}
		try {
			if(game.getTable()[4]!=null)
			{
				BufferedImage img = ImageIO.read(new File(getCardPath(game.getTable()[4])));
				img = resize(img, (int)(img.getWidth()*0.5), (int)(img.getHeight()*0.5));
				g.drawImage(img, 1500, 800, new ImageObserver() {
					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						return false;
					}
				});
			}
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
		int topLeftY = 810;
		int maxWidth = 550;
		int numOfCols = size / modfactor + 1;
		int widthOfBox = (int) (maxWidth / (numOfCols + 0.0));
		int heightOfBox = 25;

		int staggerXCnt = 0;
		for (int i = 0; i < size; i++) {
			if (i != 0 && i % modfactor == 0)
				staggerXCnt++;

			Contract c = (Contract) iterator.next();

			int x = topLeftX + staggerXCnt * ((maxWidth / (numOfCols)));
			int y = topLeftY + ((i % modfactor) * heightOfBox);
			
			
			if(c.isComplete())
			{
				g.setColor(lgreen);
				g.fillRect(x, y, widthOfBox, heightOfBox);
				g.setColor(Color.LIGHT_GRAY);
				g.drawRect(x, y, widthOfBox, heightOfBox);
				g.setColor(Color.DARK_GRAY);
			}
			else
			{
				g.setColor(Color.LIGHT_GRAY);
				g.drawRect(x, y, widthOfBox, heightOfBox);
			}
			g.drawString(c.getStart() + " to " + c.getEnd(), x + 5, y + heightOfBox / 2 + 3);
			g.drawString(c.getValue() + "", (x + widthOfBox) - 15, y + heightOfBox / 2 + 3);
		}
	}
	
	public void animateLine(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		float z=(float)Math.sqrt(Math.pow(lastPlaced.getX2()-lastPlaced.getX1(), 2)+Math.pow(lastPlaced.getY2()-lastPlaced.getY1(), 2))/lastPlaced.getCost();
		float[] dash=new float[2];
		dash[0]=(9.5f*z)/10;
		dash[1]=(0.65f*z)/10;
		g2.setStroke(new BasicStroke(13, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0,dash,0));
		g2.setColor(game.players[lastPlaced.getPlayer()].getColor());
		g2.draw(new Line2D.Double(lastPlaced.getX1(),lastPlaced.getY1(),lineX,lineY));
	}
	
	public void drawBox(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(255, 255, 255, 125));
		g2.fill(new RoundRectangle2D.Double(xLeader-25, yLeader, 500, 75, 25, 25));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println(e.getX() + " " + e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (stage == 6||moving)
			return;
		else if (stage == 0) {
			int ind=-1;
			if(e.getX()>=1300&&e.getX()<=1650) 
			{
				if(e.getY()>=660&&e.getY()<=695)
					ind=0;
				else if(e.getY()>=720&&e.getY()<=755)
					ind=1;
				else if(e.getY()>=780&&e.getY()<=815)
					ind=2;
				else if(e.getY()>=840&&e.getY()<=875)
					ind=3;
				else if(e.getY()>=900&&e.getY()<=935)
					ind=4;
			}
			if(ind!=-1&&ind<contracts.size()&&contracts.get(ind)!=null)
			{
				game.takeContract(contracts.get(ind));
				contracts.set(ind,null);
			}
			int count=0;
			for(Contract c:contracts)
				if(c==null)
					count++;
			if(e.getX()>=1435&&e.getX()<=1515&&e.getY()>=950&&e.getY()<=990&&count>2)
			{
				game.replaceContracts(contracts);
				if(game.currentPlayer==3)
					stage=1;
				else
					contracts=game.drawContract(5);
				game.nextPlayer();
				startAnimationTimer();
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
				contracts=game.drawContract(3);
				stage=3;
			}
			Node n=game.findNode(e.getX(),e.getY());
			if(n!=null&&game.isNodeEligible(e.getX(), e.getY()))
			{
				citySelect[0]=n;
				stage=4;
			}
			boolean proceed=false;
			for(TrainCard t:game.getTable())
				if(t!=null)
					proceed=true;
			int ind=-1;
			if(e.getX()>=1300&&e.getX()<=1390&&e.getY()>=700&&e.getY()<=755)
				ind=0;
			else if(e.getX()>=1500&&e.getX()<=1590&&e.getY()>=700&&e.getY()<=755)
				ind=1;
			else if(e.getX()>=1400&&e.getX()<=1490&&e.getY()>=750&&e.getY()<=800)
				ind=2;
			else if(e.getX()>=1300&&e.getX()<=1390&&e.getY()>=800&&e.getY()<=855)
				ind=3;
			else if(e.getX()>=1500&&e.getX()<=1590&&e.getY()>=800&&e.getY()<=855)
				ind=4;
			if(proceed&&ind!=-1&&game.getTable()[ind]!=null)
			{
				if(game.drawTrainCard(ind, false))
				{
					game.nextPlayer();
					if(lastRoundCount > 0)
						lastRoundCount--;
					startAnimationTimer();
				}
				else if(!(game.haveTable()||game.haveTrainCards()))
				{
					JOptionPane.showMessageDialog(null, "NO VALID MOVES. IT IS THE NEXT PLAYER'S TURN.", "Error", JOptionPane.INFORMATION_MESSAGE);
					game.nextPlayer();
					if(lastRoundCount>0)
						lastRoundCount--;
					startAnimationTimer();
				}
				else
					stage=2;
			}
		}
		else if (stage == 2) {
			int ind=-1;
			if(e.getX()>=1300&&e.getX()<=1390&&e.getY()>=700&&e.getY()<=755)
				ind=0;
			else if(e.getX()>=1500&&e.getX()<=1590&&e.getY()>=700&&e.getY()<=755)
				ind=1;
			else if(e.getX()>=1400&&e.getX()<=1490&&e.getY()>=750&&e.getY()<=800)
				ind=2;
			else if(e.getX()>=1300&&e.getX()<=1390&&e.getY()>=800&&e.getY()<=855)
				ind=3;
			else if(e.getX()>=1500&&e.getX()<=1590&&e.getY()>=800&&e.getY()<=855)
				ind=4;
			if(e.getX()>=1218&&e.getX()<=1447&&e.getY()>=513&&e.getY()<=649&&game.haveTrainCards())
			{
				game.drawTrainCard(-1, true);
				game.nextPlayer();
				stage=1;
				if(lastRoundCount>0)
					lastRoundCount--;
				startAnimationTimer();
			}
			else if(ind!=-1&&game.getTable()[ind]!=null)
			{
				if(game.drawTrainCard(ind, true))
					JOptionPane.showMessageDialog(null, "MOVE INVALID. PLEASE PICK A NON-WILD CARD.", "Input Error", JOptionPane.INFORMATION_MESSAGE);
				else
				{
					game.nextPlayer();
					stage=1;
					if(lastRoundCount>0)
						lastRoundCount--;
					startAnimationTimer();
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
			else if(e.getX()>=1435&&e.getX()<=1515&&e.getY()>=940&&e.getY()<=980&&contracts.contains(null))
			{
				game.replaceContracts(contracts);
				game.nextPlayer();
				stage=1;
				if(lastRoundCount>0)
					lastRoundCount--;
				startAnimationTimer();
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
			ColorType stack=ColorType.CYAN;
			if(game.getCardCount(ColorType.BLACK)>0&&e.getX()>=75&&e.getX()<=117&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.BLACK)-1))
				stack=ColorType.BLACK;
			else if(game.getCardCount(ColorType.GREEN)>0&&e.getX()>=119&&e.getX()<=161&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.GREEN)-1))
				stack=ColorType.GREEN;
			else if(game.getCardCount(ColorType.PINK)>0&&e.getX()>=163&&e.getX()<=205&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.PINK)-1))
				stack=ColorType.PINK;
			else if(game.getCardCount(ColorType.WHITE)>0&&e.getX()>=207&&e.getX()<=249&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.WHITE)-1))
				stack=ColorType.WHITE;
			else if(game.getCardCount(ColorType.ORANGE)>0&&e.getX()>=295&&e.getX()<=337&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.ORANGE)-1))
				stack=ColorType.ORANGE;
			else if(game.getCardCount(ColorType.RED)>0&&e.getX()>=339&&e.getX()<=381&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.RED)-1))
				stack=ColorType.RED;
			else if(game.getCardCount(ColorType.BLUE)>0&&e.getX()>=383&&e.getX()<=425&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.BLUE)-1))
				stack=ColorType.BLUE;
			else if(game.getCardCount(ColorType.YELLOW)>0&&e.getX()>=427&&e.getX()<=469&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.YELLOW)-1))
				stack=ColorType.YELLOW;
			else if(game.getCardCount(null)>0&&e.getX()>=251&&e.getX()<=293&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(null)-1))
				stack=null;
			if(stack==null||!stack.equals(ColorType.CYAN))
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
					startAnimationTimer();
					startLineAnimation();
				}
				citySelect[0]=null;
				citySelect[1]=null;
			}
		}
		if (game.lastRound() && lastRoundCount == 0)
			lastRoundCount = 5;
		if (lastRoundCount == 1)
		{
			stage = 6;
			endData=game.endGame();
		}
		if ((game.getNonWildNum()+game.getNonWildTable()<3)&&(game.getWildNum()+game.getWildTable()>2))
		{
			stage = 6;
			endData=game.endGame();
			JOptionPane.showMessageDialog(null, "Due to the impossibilty of a proper table, the game is over.", "Input Error", JOptionPane.INFORMATION_MESSAGE);
		}

		if (Math.abs(e.getX() - 22)<10 && Math.abs(e.getY() - 710)<10 && drawDirections) {
			drawDirections = false;
		} else if (e.getX() > 10 && e.getX() < 266 && e.getY() > 719 && e.getY() < 738 && !drawDirections) {
			drawDirections = true;
		}
		
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		if(stage==6)
			return;
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
		if(game.getCardCount(ColorType.BLACK)>0&&e.getX()>=75&&e.getX()<=117&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.BLACK)-1))
			hoverStack=new ColorType(92,97,92,230);
		else if(game.getCardCount(ColorType.GREEN)>0&&e.getX()>=119&&e.getX()<=161&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.GREEN)-1))
			hoverStack=new ColorType(154,196,70,230);
		else if(game.getCardCount(ColorType.PINK)>0&&e.getX()>=163&&e.getX()<=205&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.PINK)-1))
			hoverStack=new ColorType(205,135,173,230);
		else if(game.getCardCount(ColorType.WHITE)>0&&e.getX()>=207&&e.getX()<=249&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.WHITE)-1))
			hoverStack=new ColorType(255,255,255,230);
		else if(game.getCardCount(ColorType.ORANGE)>0&&e.getX()>=295&&e.getX()<=337&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.ORANGE)-1))
			hoverStack=new ColorType(210,158,53,230);
		else if(game.getCardCount(ColorType.RED)>0&&e.getX()>=339&&e.getX()<=381&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.RED)-1))
			hoverStack=new ColorType(206,66,49,230);
		else if(game.getCardCount(ColorType.BLUE)>0&&e.getX()>=383&&e.getX()<=425&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.BLUE)-1))
			hoverStack=new ColorType(4,160,211,230);
		else if(game.getCardCount(ColorType.YELLOW)>0&&e.getX()>=427&&e.getX()<=469&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(ColorType.YELLOW)-1))
			hoverStack=new ColorType(230,230,77,230);
		else if(game.getCardCount(null)>0&&e.getX()>=251&&e.getX()<=293&&e.getY()>=810&&e.getY()<=875+10*(game.getCardCount(null)-1))
			hoverStack=null;
		else
			hoverStack=ColorType.BLACK;
		if(e.getX()>=1300&&e.getX()<=1650&&stage==0) 
		{
			if(e.getY()>=660&&e.getY()<=695)
				hoverConStart=0;
			else if(e.getY()>=720&&e.getY()<=755)
				hoverConStart=1;
			else if(e.getY()>=780&&e.getY()<=815)
				hoverConStart=2;
			else if(e.getY()>=840&&e.getY()<=875)
				hoverConStart=3;
			else if(e.getY()>=900&&e.getY()<=935)
				hoverConStart=4;
			else
				hoverConStart=-1;
		}
		else
			hoverConStart=-1;
		if(e.getX()>=1300&&e.getX()<=1650&&stage==3) 
		{
			if(e.getY()>=700&&e.getY()<=760)
				hoverCon=0;
			else if(e.getY()>=780&&e.getY()<=840)
				hoverCon=1;
			else if(e.getY()>=860&&e.getY()<=920)
				hoverCon=2;
			else
				hoverCon=-1;
		}
		else
			hoverCon=-1;
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
		this.moving = true;
		ArrayList<Player> playerCopy = new ArrayList<Player>();
		for (int i = 0; i < game.players.length; i++)
			playerCopy.add(game.players[i]);
		Collections.sort(playerCopy);
		targety = 110 + (playerCopy.indexOf(game.players[game.currentPlayer]) * 100);
		change=(targety-yLeader)/30;
		numMoved=0;
		animateTimer = new Timer(10, new MoveBox());
		animateTimer.start();
		this.repaint();
	}
	class MoveBox implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			numMoved=1;
			if (Math.abs(yLeader-targety)>3) 
			{
				moveBox(change);
				repaint();
			} 
			else 
			{
				yLeader=targety;
				moving = false;
				numMoved=-1;
				animateTimer.stop();
				repaint();
			}
		}
	}

	public void startLineAnimation() {
		lastPlaced=game.getLastPlaced();
		lineX=lastPlaced.getX1();
		lineY=lastPlaced.getY1();
		animateTimer2 = new Timer(10, new AnimateLine());
		animateTimer2.start();
		this.repaint();
	}
	class AnimateLine implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(lastPlaced==null)
				return;
			double targetx = lastPlaced.getX2();
			double targety = lastPlaced.getY2();
			double changeX=(targetx-lastPlaced.getX1())/(50);
			double changeY=(targety-lastPlaced.getY1())/(50);
			if (Math.abs(lineX-targetx)>0.005&&Math.abs(lineY-targety)>0.005) 
			{
					lineX+=changeX;
					lineY+=changeY;
					numLooped++;
					repaint();
			} 
			else 
			{
				lastPlaced=null;
				numLooped=0;
				animateTimer2.stop();
				repaint();
			}
		}
	}
	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) {
		if (stage!=0 && e.getKeyCode() == 0) {
			stage = 1;
			citySelect[0]=null;
			citySelect[1]=null;
			repaint();
		}
	}

}