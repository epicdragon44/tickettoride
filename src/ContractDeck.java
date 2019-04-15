import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

public class ContractDeck {
	private Queue<Contract> deck;

	public ContractDeck() throws IOException 
	{
		deck = new LinkedList<Contract>();
		ArrayList<Contract> temp=new ArrayList<Contract>();
		BufferedReader bf=new BufferedReader(new FileReader("tickets.txt"));
		for(int i=Integer.parseInt(bf.readLine());i>0;i--)
		{
			StringTokenizer st=new StringTokenizer(bf.readLine(),"|");
			temp.add(new Contract(Integer.parseInt(st.nextToken()),st.nextToken(),st.nextToken()));
		}
		Collections.shuffle(temp);
		for(Contract c:temp)
			deck.add(c);
	}

	public ArrayList<Contract> draw(int num) 
	{
		ArrayList<Contract> c = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			c.add(deck.remove());
		}
		return c;
	}

	public void replace(ArrayList<Contract> c) 
	{
		deck.addAll(c);
	}
	
	public boolean hasCards(int num)
	{
		return deck.size()>num-1;
	}
	
	public int numCards()
	{
		return deck.size();
	}
}
