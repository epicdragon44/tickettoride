import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

public class TrainCardDeck {
	private ArrayList<TrainCard> deck;
	private TrainCard[] faceup;

	public TrainCardDeck() {
		deck = new ArrayList<>();
		restartDeck();
		faceup = new TrainCard[5];
		for (int i = 0; i < 5; i++) {
			faceup[i] = deck.remove(0);
		}
	}
// add in more reset decks after the faceup clear
	public void restartDeck() {
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
	}

	public TrainCard draw(int index) {
		TrainCard f = null;
		if (index < 5) {
			TrainCard p = faceup[index];
			faceup[index] = draw(5);
			checkWildLim();
			return p;
		}
		if (deck.size() == 1) {
			f = deck.remove(0);
			restartDeck();
			return f;
		} else
			return deck.remove(0);

	}

	public void checkWildLim() {
		int count = 0;
		for (int i = 0; i < 5; i++) {
			if (faceup[i].getWild())
				count++;
		}
		if (count >= 3) {
			for (int i = 0; i < 5; i++) {
				faceup[i] = null;
			}
			resetFaceup();
			checkWildLim();
		}
	}

	private void resetFaceup() {
		faceup[0] = draw(5);
		faceup[1] = draw(5);
		faceup[2] = draw(5);
		faceup[3] = draw(5);
		faceup[4] = draw(5);
	}
}
