import java.io.IOException;
import java.util.ArrayList;

/**
 * This is a complex class that manages all the internal state and mechanics of the game in one area, Full JavaDoc annotations are available for each method.
 */
public class GameEngine {
	public Player[] players;

	private ContractDeck cDeck;
	private TrainCardDeck tDeck;
	private ArrayList<TrainCard> trashDeck;
	private Board gBoard;
	private TrainCard[] tableDeck;
	public int currentPlayer;
	
	public static final int[] PTS_PER_TRACK = {0, 1, 2, 4, 7, 10, 15};

	/**
	 * Constructs a default GameEngine
	 * @throws IOException Problems with reading input from files
	 */
	public GameEngine() throws IOException {
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

	/**
	 * Moves onto the next player
	 */
	public void nextPlayer() {
		currentPlayer = (currentPlayer+1)%4;
	}

	/**
	 * Gets the card count for a certain Color
	 * @param col ColorType of interest
	 * @return The card count for that Color
	 */
	public int getCardCount(ColorType col)
	{
		return players[currentPlayer].getTrainCards().get(col);
	}

	/**
	 * Gets the most recently placed Track
	 * @return The most recently placed Track
	 */
	public Track getLastPlaced()
	{
		return gBoard.getLastPlaced();
	}

	/**
	 * Places trains between two nodes of a certain color
	 * @param nodeOne The First Node
	 * @param nodeTwo The Second Node
	 * @param c The Color to make the Train
	 * @return Boolean indicating whether the operation occurred successfully
	 */
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

	/**
	 * Checks the eligibility of an attempted train laying
	 * @param nodeOne The first node
	 * @param nodeTwo The second node
	 * @param c The color of the train
	 * @return Whether it is eligible
	 */
	private boolean checkEligibility(Node nodeOne, Node nodeTwo, ColorType c) {
		int rand=players[currentPlayer].getTrainCards().get(null);
		int col=0;
		if(c!=null)
			col=players[currentPlayer].getTrainCards().get(c);
		int cost=gBoard.connectionCost(nodeOne.toString(), nodeTwo.toString());
		return rand-(cost-col)>-1&&players[currentPlayer].getTrainsLeft()>cost-1;
	}

	/**
	 * Checks whether the table is needed
	 * @return Whether the table is needed
	 */
	private boolean needTable()
	{
		for(TrainCard t:tableDeck)
			if(t==null)
				return true;
		return checkWildLim();
	}

	/**
	 * Resets the table
	 */
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

	/**
	 * Gets the non wild table card count
	 * @return The non wild table card count
	 */
	public int getNonWildTable()
	{
		int cnt=0;
		for(TrainCard t:tableDeck)
			if(t!=null&&!t.getwild())
				cnt++;
		return cnt;
	}

	/**
	 * Gets the non wild table card number
	 * @return The non wild table card number
	 */
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

	/**
	 * Get the wild table count
	 * @return Wild table count
	 */
	public int getWildTable()
	{
		int cnt=0;
		for(TrainCard t:tableDeck)
			if(t!=null&&t.getwild())
				cnt++;
		return cnt;
	}

	/**
	 * Get number of wild cards
	 * @return Number of wild cards
	 */
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

	/**
	 * Get the Board
	 * @return The Board
	 */
	public Board getgBoard() {
		return gBoard;
	}

	/**
	 * Get the number of Contracts
	 * @return The number of Contracts
 	 */
	public int getNumContracts()
	{
		return cDeck.size();
	}

	/**
	 * Checks if there are train cards
	 * @return Boolean indicating whether there are train cards
	 */
	public boolean haveTrainCards()
	{
		if(tDeck.needsReset())
			return trashDeck.size()!=0;
		return true;
	}

	/**
	 * Checks if there is a table
	 * @return Boolean indicating whether there is a table
	 */
	public boolean haveTable()
	{
		for(TrainCard t:tableDeck)
			if(t!=null&&t.getColor()!=null)
				return true;
		return false;
	}

	/**
	 * Get the Table
	 * @return Array of TrainCards comprising the table
	 */
	public TrainCard[] getTable()
	{
		return tableDeck;
	}

	/**
	 * Draws the number of passed in contracts
	 * @param num The number of contracts
	 * @return Array of contracts
	 */
	public ArrayList<Contract> drawContract(int num) {
		if(cDeck.size()>0)
			return cDeck.draw(Math.min(cDeck.size(), num));
		return null;
	}

	/**
	 * Gives the current player a certain Contract c
	 * @param c The contract to give the player
	 */
	public void takeContract(Contract c)
	{
		players[currentPlayer].addContract(c);
	}

	/**
	 * Check whether the wild card limit has been exceeded
	 * @return Boolean indicating whether the wild card limit has been exceeded
	 */
	public boolean checkWildLim() {
		int c=0;
		for(TrainCard t:tableDeck)
			if(t!=null&&t.getwild())
				if(++c==3)
					return true;
	  return false;
	}

	/**
	 * Method to update the table
	 */
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
    	if(checkWildLim()&&getNonWildNum()+getNonWildTable()>2)
    		updateTable();
    }

	/**
	 * Method to check if it is the last round
	 * @return Boolena indicating whether it is the last round
	 */
	public boolean lastRound() {
    	for(Player p:players)
    		if(p.getTrainsLeft()<3)
    			return true;
    	return false;
    }

	/**
	 * Draws a train card
	 * @param pos Position of drawn wild
	 * @param oneDrawn Boolean of the drawn card
	 * @return Boolean indicating if face up wild drawn and takes if 1 card already drawn
	 */
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

	/**
	 *  Gets the end game results
	 * @return returns contract payouts,then longest train,then globetrotter
	 */
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

	/**
	 * Gets the longest train player ints
	 * @return The longest train player ints
	 */
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

	/**
	 * Finds a node that contains the given x and y coordinates
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return The Node that contains the coordinates
	 */
	public Node findNode(int x, int y) {
		return gBoard.findNode(x, y);
	}

	/**
	 * Replace the passed in contracts
	 * @param c Contracts of interest
	 */
	public void replaceContracts(ArrayList<Contract> c)
	{
		cDeck.replace(c);
	}
	
	//for stage 1;draw green circle if true, draw red circle if false , don't draw anything if null

	/**
	 * Checks if node at given x and y is eligible
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return Boolean indicating elibility
	 */
	public Boolean isNodeEligible(int x,int y)
	{
		return gBoard.isNodeEligible(x,y);
	}
	
	//for stage 4;draw green circle if true, draw red circle if false , don't draw anything if null
	/**
	 * Checks if node at given x and y is eligible
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param n Node in question
	 * @return Boolean indicating elibility
	 */
	public Boolean isNodeEligible(int x,int y,Node n)
	{
		return gBoard.isNodeEligible(x,y,n);
	}
}
