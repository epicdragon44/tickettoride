import java.util.ArrayList;

public class TrainCardDeck {
private ArrayList<TrainCard> deck;
public TrainCardDeck()
{
deck = new ArrayList<TrainCard>();	
}
public void restartDeck()
{
	
}
public TrainCard draw(int index)
{
	return deck.remove(index);
}
}
