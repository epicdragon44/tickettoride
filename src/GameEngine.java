import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class GameEngine {
	public Player[] players;
	private ContractDeck cDeck;
	private TrainCardDeck tDeck;
	private ArrayList<TrainCard> trashDeck;
	private Board gBoard;
	private TrainCard[] tableDeck;
	public int currentPlayer;
	
	public static final int[] PTS_PER_TRACK = {0, 1, 2, 4, 7, 10, 15};
	
	public GameEngine() throws IOException {
		 players=new Player[4];
		 players[0]=new Player("Red");
		 players[1]=new Player("Green");
		 players[2]=new Player("Blue");
		 players[3]=new Player("Yellow");
		 cDeck=new ContractDeck();
		 tDeck=new TrainCardDeck();
		 trashDeck=new ArrayList<>();
		 gBoard=new Board();
		 tableDeck=new TrainCard[5];
		 currentPlayer=0;
		 for(Player p:players)
	  		for(int i=0;i<4;i++)
	  			p.drawTrainCards(tDeck.draw());
		 for(int i=0;i<5;i++)
	  		tableDeck[i]=tDeck.draw();
	}
	
	public void nextPlayer() {
		if(currentPlayer==3)
			currentPlayer=0;
	  	else
	  		currentPlayer++;
	}
	
	public void placeTrain(Node nodeOne, Node nodeTwo, Color c) {
		if(!checkEligibility(nodeOne, nodeTwo, c))
			return;
	  	if(gBoard.placeTrains(currentPlayer, c, nodeOne, nodeTwo))
	  		trashDeck.addAll(players[currentPlayer].placeTrains(gBoard.connectionCost(nodeOne.toString(), nodeTwo.toString()), c));
	}
	
	private boolean checkEligibility(Node nodeOne, Node nodeTwo, Color c) {
		int rand=players[currentPlayer].getTrainCards().get(new TrainCard(null,true)),col=players[currentPlayer].getTrainCards().get(new TrainCard(c,false)),cost=gBoard.connectionCost(nodeOne.toString(), nodeTwo.toString());
		return rand-(cost-col)>0&&players[currentPlayer].trainsLeft()>cost-1;
	}
	
	public Player[] getPlayers() {
	    return players;
	}
	
	public ArrayList<Contract> drawContract(int num) {
		if(cDeck.hasCards(num))
			return cDeck.draw(num);
		return null;
	}
	
	public boolean checkWildLim() {
		int c=0;
		for(TrainCard t:tableDeck)
			if(t.getwild())
				if(++c==3)
					return true;
	  return false;
	}
	
	public Player getLongestTrain() {
		return gBoard.findLongestTrainPlayer(players);
	}
  
	public void updateTable() {
    	for(int i=0;i<tableDeck.length;i++)
    	{
    		if(tableDeck[i].getwild())
    		{
    			trashDeck.add(tableDeck[i]);
    			tableDeck[i]=null;
    		}
    	}
    	if(tDeck.needsReset())
			tDeck.restartDeck(trashDeck);
    	for(int i=0;i<tableDeck.length;i++)
    	{
    		if(tableDeck[i]==null)
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
    		if(p.trainsLeft()<3)
    			return true;
    	return false;
    }
  
	public TrainCard drawTrainCard(int pos) {
		TrainCard rtn;
		if(pos==-1)
			rtn=tDeck.draw();
		else
		{
			rtn=tableDeck[pos];
			tableDeck[pos]=tDeck.draw();
			if(checkWildLim())
				updateTable();
		}
		if(tDeck.needsReset())
			tDeck.restartDeck(trashDeck);
		return rtn;
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
}
