import java.util.*;
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
		System.out.println(c);
		while(c.contains(null))
			c.remove(null);
		deck.addAll(c);
		System.out.println(c);
	}
	
	public int size()
	{
		return deck.size();
	}

	public Iterator<Contract> iterator() {
		return deck.iterator();
	}
}
