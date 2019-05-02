import java.util.ArrayList;

public class GameEngine {
	private GamePanel daddyPanel;

	public Player[] players;

	private ContractDeck cDeck;
	private TrainCardDeck tDeck;
	private ArrayList<TrainCard> trashDeck;
	private Board gBoard;
	private TrainCard[] tableDeck;
	public int currentPlayer;
	
	public static final int[] PTS_PER_TRACK = {0, 1, 2, 4, 7, 10, 15};
	
	public GameEngine(GamePanel daddyPanel) throws Exception {
		this.daddyPanel = daddyPanel;

		players=new Player[4];
		players[0]=new Player(ColorType.RED);
		players[1]=new Player(ColorType.GREEN);
		players[2]=new Player(ColorType.BLUE);
		players[3]=new Player(ColorType.YELLOW);
		cDeck=new ContractDeck();
		tDeck=new TrainCardDeck();
		trashDeck=new ArrayList<>();
		gBoard=new Board(this);
		tableDeck=new TrainCard[5];
		currentPlayer=0;
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
				drawTrainCard(-1,false);
			nextPlayer();
		}
		for(int i=0;i<5;i++)
			tableDeck[i]=tDeck.draw();
	}

	public void nextPlayer() {
		currentPlayer = (currentPlayer+1)%4;
		daddyPanel.startAnimationTimer();
	}
	
	public boolean placeTrain(Node nodeOne, Node nodeTwo, ColorType c) {
		if(checkEligibility(nodeOne, nodeTwo, c)&&gBoard.placeTrains(currentPlayer, c, nodeOne, nodeTwo))
		{
			trashDeck.addAll(players[currentPlayer].placeTrains(gBoard.connectionCost(nodeOne.toString(), nodeTwo.toString()), c));
			return true;
		}
	  	else
	  		return false;
	}
	
	private boolean checkEligibility(Node nodeOne, Node nodeTwo, ColorType c) {
		int rand=players[currentPlayer].getTrainCards().get(null);
		int col=players[currentPlayer].getTrainCards().get(c);
		int cost=gBoard.connectionCost(nodeOne.toString(), nodeTwo.toString());
		return rand-(cost-col)>-1&&players[currentPlayer].getTrainsLeft()>cost-1;
	}
	
	public Board getgBoard() {
		return gBoard;
	}
	
	public int getNumContracts()
	{
		return cDeck.size();
	}
	
	public boolean haveTrainCards()
	{
		if(tDeck.needsReset())
			return trashDeck.size()!=0;
		return true;
	}
	
	public boolean haveTable()
	{
		for(TrainCard t:tableDeck)
			if(t!=null&&t.getColor()!=null)
				return false;
		return true;
	}
	
	public TrainCard[] getTable()
	{
		return tableDeck;
	}

	public ContractDeck getcDeck() {
		return cDeck;
	}

	public TrainCardDeck gettDeck() {
		return tDeck;
	}
	
	public ArrayList<Contract> drawContract() {
		if(cDeck.size()>0)
			return cDeck.draw(Math.min(cDeck.size(), 3));
		return null;
	}
	
	public void takeContract(Contract c)
	{
		players[currentPlayer].addContract(c);
	}
	
	public boolean checkWildLim() {
		int c=0;
		for(TrainCard t:tableDeck)
			if(t.getwild())
				if(++c==3)
					return true;
	  return false;
	}
  
	public void updateTable() {
    	for(int i=0;i<tableDeck.length;i++)
    	{
    		if(tableDeck[i]!=null)
    		{
    			trashDeck.add(tableDeck[i]);
    			tableDeck[i]=null;
    		}
    	}
    	if(tDeck.needsReset())
			tDeck.restartDeck(trashDeck);
    	for(int i=0;i<tableDeck.length;i++)
    	{
    		if(tableDeck[i]==null&&haveTrainCards())
    		{
    			tableDeck[i]=tDeck.draw();
    			if(tDeck.needsReset())
    				tDeck.restartDeck(trashDeck);
    		}
    	}
    	if(checkWildLim())
    		updateTable();
    }
    
    public boolean lastRound() {
    	for(Player p:players)
    		if(p.getTrainsLeft()<3)
    			return true;
    	return false;
    }
    
    //returns if face up wild drawn and takes if 1 card already drawn
	public boolean drawTrainCard(int pos, boolean oneDrawn) {
		TrainCard rtn=new TrainCard(null,false);
		if(pos==-1)
			players[currentPlayer].drawTrainCards(tDeck.draw());
		else
		{
			rtn=tableDeck[pos];
			if(!(rtn.getwild()&&oneDrawn))
			{
				players[currentPlayer].drawTrainCards(rtn);
				if(!haveTrainCards())
					tableDeck[pos]=null;
				else
					tableDeck[pos]=tDeck.draw();
			}
			if(checkWildLim())
				updateTable();
		}
		if(tDeck.needsReset())
			tDeck.restartDeck(trashDeck);
		return rtn.getwild();
	}

	//returns contract payouts,then longest train,then globetrotter
	public int[] endGame() {
		int[][] contractCount= {{0,0,0,0},{0,0,0,0}};
		for(int i=0;i<contractCount.length;i++)
		{
			for(Contract c:players[i].getContract())
			{
				if(gBoard.isComplete(c))
				{
					players[i].addPoints(c.getValue());
					contractCount[0][i]++;
					contractCount[1][i]+=c.getValue();
				}
				else
				{
					players[i].addPoints(-1*c.getValue());
					contractCount[1][i]-=c.getValue();
				}
			}
		}
		int val=-111,place=0;
		for(int i=0;i<contractCount.length;i++)
		{
			if(contractCount[0][i]>val)
			{
				val=contractCount[0][i];
				place=i;
			}
		}
		int[] rtn={contractCount[1][0],contractCount[1][1],contractCount[1][2],contractCount[1][3],findLongest(),place};
		players[rtn[4]].addPoints(10);
		players[place].addPoints(15);
		return rtn;
	}
	
	private int findLongest()
	{
		Player play=gBoard.findLongestTrainPlayer(players);
		for(int i=0;i<players.length;i++)
			if(players[i].getName().equals(play.getName()))
				return i;
		return -1;
	}

	public Node findNode(int x, int y) {
		return gBoard.findNode(x, y);
	}
	
	//for stage 1;draw green circle if true, draw red circle if false , don't draw anything if null
	public Boolean isNodeEligible(int x,int y)
	{
		return gBoard.isNodeEligible(x,y);
	}
	
	//for stage 4;draw green circle if true, draw red circle if false , don't draw anything if null
	public Boolean isNodeEligible(int x,int y,Node n)
	{
		return gBoard.isNodeEligible(x,y,n);
	}
}
