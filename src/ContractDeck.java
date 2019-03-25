import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.LinkedList;
import java.io.*;

public class ContractDeck {
	private Queue<Contract> deck;

	public ContractDeck() throws IOException {
		Scanner s = new Scanner(new File("elleh"));
		deck = new LinkedList<Contract>();
	}

	public ArrayList<Contract> draw(int num) {
		ArrayList<Contract> c = new ArrayList<Contract>();
		for (int i = 0; i < num; i++) {
			c.add(deck.remove());
		}
		return c;
	}

	public void replace(ArrayList<Contract> c) {
		deck.addAll(c);
	}
}
