import java.util.ArrayList;
import java.util.Collections;

public class TrainCardDeck {
	private ArrayList<TrainCard> deck;
	private TrainCard[] faceup;

	public TrainCardDeck() {
		deck = new ArrayList<TrainCard>();
		restartDeck();
		faceup = new TrainCard[5];
	}

	public void restartDeck() {
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard("", true));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard("Orange", false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard("White", false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard("Red", false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard("Green", false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard("Black", false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard("Blue", false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard("Yellow", false));
		}
		for (int i = 0; i < 12; i++) {
			deck.add(new TrainCard("Purple", false));
		}
		Collections.shuffle(deck);
	}

	public TrainCard draw(int index) {
		return deck.remove(index);
	}

	public boolean checkWildLim() {

	}
}
