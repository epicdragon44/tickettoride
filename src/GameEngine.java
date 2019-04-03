import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class GameEngine {

    private Player[] players;
    private ContractDeck cDeck;
    private TrainCardDeck tDeck;
    private ArrayList<TrainCard> trashDeck;
    private Board gBoard;
    private TrainCard[] tableDeck;
    private int currentPlayer;

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

    //add to trash
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
    	return cDeck.draw(num);
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

    }

    public void updateTable() {
    	for(int i=0;i<tableDeck.length;i++)
    	{
    		if(tableDeck[i].getwild())
    		{
    			trashDeck
    		}
    	}
    }

    public boolean gameEnded() {
    	
    }
    
    public boolean lastRound() {
    	for(Player p:players)
    		if(p.trainsLeft()<3)
    			return true;
    	return false;
    }

    public TrainCard drawTrainCard(int pos) {
    	
    }

    public void replacetDeck() {
    	
    }

    public boolean hasThreeWildCards() {
    	
    }

    private void clearAndRepopulateTableDeck() {
    	
    }

    public Player[] endGame() {
    	
    }

    public Node findNode(int x, int y) {
    	return gBoard.findNode(x, y);
    }
}

