import java.util.ArrayList;
import java.util.Collections;

/**
 * Simple Data Structure class to hold a list of Train Cards and its helper methods to help manage the deck
 */
public class TrainCardDeck {
	private ArrayList<TrainCard> deck;

	public TrainCardDeck() {
		
		deck = new ArrayList<>();
		
		for (int i = 0; i < 14; i++) {
			
			deck.add(new TrainCard(null, true));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(ColorType.ORANGE, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(ColorType.WHITE, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(ColorType.BLUE, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(ColorType.GREEN, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(ColorType.BLACK, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(ColorType.PINK, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(ColorType.YELLOW, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(ColorType.RED, false));
		}
		
		Collections.shuffle(deck);
	}

	 ArrayList<TrainCard> getDeck()
	{
		return deck;
	}

	 void restartDeck(ArrayList<TrainCard> rep) {
		if(rep.size()==0)
			return;
		deck.addAll(rep);
		Collections.shuffle(deck);
	}

	 void replace(TrainCard t) {
		deck.add(t);
		Collections.shuffle(deck);
	}

	 TrainCard draw() {
		if(!needsReset())
			return deck.remove(0);
		return null;
	}

	 boolean needsReset() {
		return deck.size()==0;
	}
	
	public String toString()
	{
		return deck.toString();
	}
}
