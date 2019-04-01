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
    
    //consider moving to GamePanel
    public void initGame() {
    	
    }

    public void nextPlayer() {
    	if(currentPlayer==3)
    		currentPlayer=0;
    	else
    		currentPlayer++;
    }

    public void placeTrain(Node nodeOne, Node nodeTwo, Color c) {
    	if(!checkEligibilty(nodeOne, nodeTwo, c))
    		return;
    	if(gBoard.placeTrains(currentPlayer, c, nodeOne, nodeTwo))
    		players[currentPlayer].placeTrains(gBoard.connectionCost(nodeOne.toString(), nodeTwo.toString()), c);
    }

    private boolean checkEligibility(Node nodeOne, Node nodeTwo, Color c) {
    	return players[currentPlayer].getTrainCards().get(new TrainCard(null,true));
    }

    public Player[] getPlayers() {
        return players;
    }

    public void drawContract() {
    	
    }

    public boolean checkWildLim() {
    	
    }

    public Player getLongestTrain() {
    	
    }

    private void resetVisitedTracks() {
    	
    }

    public void updateTable() {
    	
    }

    public boolean gameEnded() {
    	
    }

    public TrainCard drawTrainCard(int pos) {
    	
    }

    public void replacetDeck() {
    	
    }

    public boolean hasThreeWildCards() {
    	
    }

    private void clearAndRepopulateTableDeck() {
    	
    }

    public void endGame() {
    	
    }

    public void lastRound() {
    	
    }

    public Node findNode(int x, int y) {
    	return gBoard.findNode(x, y);
    }
}

