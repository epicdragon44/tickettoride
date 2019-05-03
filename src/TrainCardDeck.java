import java.util.ArrayList;
import java.util.Collections;

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

	public void restartDeck(ArrayList<TrainCard> rep) {
		deck.addAll(rep);
		Collections.shuffle(deck);
		rep=new ArrayList<TrainCard>();
	}

	public TrainCard draw() {
		if(!needsReset())
			return deck.remove(0);
		return null;
	}

	public boolean needsReset() {
		return deck.size()==0;
	}
	
	public String toString()
	{
		return deck.toString();
	}
}
