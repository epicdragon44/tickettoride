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
		if(checkWildLim())
			updateTable();
	}

	public void nextPlayer() {
		currentPlayer = (currentPlayer+1)%4;
		daddyPanel.startAnimationTimer();
	}
	
	public int getCardCount(ColorType col)
	{
		return players[currentPlayer].getTrainCards().get(col);
	}
	
	public boolean placeTrain(Node nodeOne, Node nodeTwo, ColorType c) {
		if(players[currentPlayer].getTrainCards().get(c)==0)
			c=null;
		if(checkEligibility(nodeOne, nodeTwo, c)&&gBoard.placeTrains(currentPlayer, c, nodeOne, nodeTwo))
		{
			trashDeck.addAll(players[currentPlayer].placeTrains(gBoard.connectionCost(nodeOne.toString(), nodeTwo.toString()), c));
			for(Contract ct:players[currentPlayer].getContract())
				ct.checkComplete(gBoard);
			if(tDeck.needsReset())
			{
				tDeck.restartDeck(trashDeck);
				trashDeck=new ArrayList<TrainCard>();
			}
			if(needTable())
				resetTable();
			if(checkWildLim())
				updateTable();
			return true;
		}
	  	else
	  		return false;
	}
	
	private boolean checkEligibility(Node nodeOne, Node nodeTwo, ColorType c) {
		int rand=players[currentPlayer].getTrainCards().get(null);
		int col=0;
		if(c!=null)
			col=players[currentPlayer].getTrainCards().get(c);
		int cost=gBoard.connectionCost(nodeOne.toString(), nodeTwo.toString());
		return rand-(cost-col)>-1&&players[currentPlayer].getTrainsLeft()>cost-1;
	}
	
	private boolean needTable()
	{
		for(TrainCard t:tableDeck)
			if(t==null)
				return true;
		return checkWildLim();
	}
	
	private void resetTable()
	{
		for(int i=0;i<5;i++)
		{
			if(!tDeck.needsReset()&&(tableDeck[i]==null||tableDeck[i].getwild()))
			{
				if(tableDeck[i]!=null)
					tDeck.replace(tableDeck[i]);
				tableDeck[i]=tDeck.draw();
			}
		}
		if(checkWildLim())
			updateTable();
	}
	
	public int getNonWildTable()
	{
		int cnt=0;
		for(TrainCard t:tableDeck)
			if(t!=null&&!t.getwild())
				cnt++;
		return cnt;
	}
	
	public int getNonWildNum()
	{
		int count=0;
		for(TrainCard t:tDeck.getDeck())
			if(!t.getwild())
				count++;
		for(TrainCard t:trashDeck)
			if(!t.getwild())
				count++;
		return count;
	}
	
	public int getWildTable()
	{
		int cnt=0;
		for(TrainCard t:tableDeck)
			if(t!=null&&t.getwild())
				cnt++;
		return cnt;
	}
	
	public int getWildNum()
	{
		int count=0;
		for(TrainCard t:tDeck.getDeck())
			if(t.getwild())
				count++;
		for(TrainCard t:trashDeck)
			if(t.getwild())
				count++;
		return count;
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
				return true;
		return false;
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
	
	public ArrayList<Contract> drawContract(int num) {
		if(cDeck.size()>0)
			return cDeck.draw(Math.min(cDeck.size(), num));
		return null;
	}
	
	public void takeContract(Contract c)
	{
		players[currentPlayer].addContract(c);
	}
	
	public Track findTrack(Node n1, Node n2)
	{
		return gBoard.findTrack(n1,n2);
	}
	
	public boolean checkWildLim() {
		int c=0;
		for(TrainCard t:tableDeck)
			if(t!=null&&t.getwild())
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
    	if(trashDeck.size()!=0)
    	{
			tDeck.restartDeck(trashDeck);
			trashDeck=new ArrayList<TrainCard>();
    	}
    	for(int i=0;i<tableDeck.length;i++)
    	{
    		if(tableDeck[i]==null&&haveTrainCards())
    		{
    			tableDeck[i]=tDeck.draw();
    			if(tDeck.needsReset())
    			{
    				tDeck.restartDeck(trashDeck);
    				trashDeck=new ArrayList<TrainCard>();
    			}
    		}
    	}
    	if(checkWildLim()&&(getNonWildNum()+getNonWildTable()<3)&&(getWildNum()+getWildTable()>2))
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
				if(checkWildLim())
					updateTable();
			}
		}
		if(tDeck.needsReset())
		{
			tDeck.restartDeck(trashDeck);
			trashDeck=new ArrayList<TrainCard>();
		}
		return rtn.getwild();
	}

	//returns contract payouts,then longest train,then globetrotter
	public int[][] endGame() {
		for(int i=0;i<players.length;i++)
			for(Contract ct:players[i].getContract())
				ct.checkComplete(gBoard);
		int[][] contractCount= {{0,0,0,0},{0,0,0,0}};
		for(int i=0;i<players.length;i++)
		{
			for(Contract c:players[i].getContract())
			{
				if(c.isComplete())
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
		ArrayList<Integer> cnt=new ArrayList<Integer>();
		cnt.add(0);
		int val=contractCount[0][0];
		for(int i=1;i<players.length;i++)
		{
			if(contractCount[0][i]==val)
			{
				cnt.add(i);
			}
			else if(contractCount[0][i]>val)
			{
				val=contractCount[0][i];
				cnt=new ArrayList<Integer>();
				cnt.add(i);
			}
		}
		int[] place=new int[cnt.size()];
		for(int i=0;i<cnt.size();i++)
			place[i]=cnt.get(i);
		int[][] rtn={contractCount[1],findLongest(),place};
		for(Integer i:cnt)
			players[i].addPoints(15);
		return rtn;
	}
	
	private int[] findLongest()
	{
		ArrayList<Player> player=gBoard.findLongestTrainPlayer(players);
		int[] rtn=new int[player.size()];
		int count=0;
		for(Player play:player)
		{
			for(int i=0;i<players.length;i++)
			{
				if(players[i].getName().equals(play.getName()))
				{
					rtn[count++]=i;
					players[i].addPoints(10);
				}
			}
		}
		return rtn;
	}

	public Node findNode(int x, int y) {
		return gBoard.findNode(x, y);
	}
	
	public void replaceContracts(ArrayList<Contract> c)
	{
		cDeck.replace(c);
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
