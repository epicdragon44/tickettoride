import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

public class TrainCardDeck {
	private ArrayList<TrainCard> deck;
	private TrainCard[] faceup;

	public TrainCardDeck() {
		
		deck = new ArrayList<>();
		
		for (int i = 0; i < 12; i++) {
			
			deck.add(new TrainCard(null, true));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(Color.orange, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(Color.white, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(Color.blue, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(Color.green, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(Color.black, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(Color.pink, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(Color.yellow, false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard(Color.red, false));
		}
		
		Collections.shuffle(deck);
		faceup = new TrainCard[5];
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
}
