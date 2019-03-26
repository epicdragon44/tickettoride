import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class ContractDeck {
	private Queue<Contract> deck;

	public ContractDeck() {
		deck = new LinkedList<Contract>();
	}

	public ArrayList<Contract> draw(int num) {
		ArrayList<Contract> c = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			c.add(deck.remove());
		}
		return c;
	}

	public void replace(ArrayList<Contract> c) {
		deck.addAll(c);
	}
}
