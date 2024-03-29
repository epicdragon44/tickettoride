import java.awt.Color;
import java.util.*;

/**
 * Simple Data Structure class that represents a Player in the game, with a name, number of points, color, count of trains left, list of contracts, map of traincards, and helper methods
 */
public class Player implements Comparable<Player>
{
	private String name;
	private int points;
	private Color c;

	private int trainsLeft;
	private ArrayList<Contract> contracts;
	private HashMap<ColorType, Integer> trainCards;
	
	public Player(Color n)
	{
		this.c = n;
		name = n.toString();
		points=0;
		trainsLeft=45;
		contracts=new ArrayList<Contract>();
		trainCards=new HashMap<ColorType, Integer>();
		trainCards.put(ColorType.BLACK, 0);
		trainCards.put(ColorType.ORANGE, 0);
		trainCards.put(ColorType.RED, 0);
		trainCards.put(ColorType.BLUE, 0);
		trainCards.put(ColorType.GREEN, 0);
		trainCards.put(ColorType.PINK, 0);
		trainCards.put(ColorType.WHITE, 0);
		trainCards.put(ColorType.YELLOW, 0);
		trainCards.put(null, 0);
	}
	
	public String getName()
	{
		return name;
	}

	 int getPoints()
	{
		return points;
	}

	 void addPoints(int num)
	{
		points+=num;
	}

	 int getTrainsLeft() {
		return trainsLeft;
	}

	 Color getColor() {
		return c;
	}

	public ArrayList<Contract> getContract()
	{
		return contracts;
	}

	 void addContract(Contract c)
	{
		contracts.add(c);
	}
	
	public void deleteContract(Contract c)
	{
		for(int i=0;i<contracts.size();i++)
		{
			Contract cs=contracts.get(i);
			if(cs.equals(c))
			{
				contracts.remove(i);
				return;
			}
		}
	}

	 HashMap<ColorType, Integer> getTrainCards()
	{
		return trainCards;
	}

	 void drawTrainCards(TrainCard train)
	{
		trainCards.put(train.getColor(),trainCards.get(train.getColor())+1);
	}

	 ArrayList<TrainCard> placeTrains(int num, ColorType col)
	{
		if(num==1||num==2)
			addPoints(num);
		else if(num==3)
			addPoints(4);
		else if(num==4)
			addPoints(7);
		else if(num==5)
			addPoints(10);
		else if(num==6)
			addPoints(15);
		decrementTrainsLeft(num);
		ArrayList<TrainCard> rtn=new ArrayList<TrainCard>();
		if(col!=null)
		{
			while(num!=0&&trainCards.get(col)!=0)
			{
				trainCards.put(col,trainCards.get(col)-1);
				rtn.add(new TrainCard(col,false));
				num--;
			}
		}
		while(num!=0)
		{
			trainCards.put(null,trainCards.get(null)-1);
			rtn.add(new TrainCard(null,true));
			num--;
		}
		return rtn;
	}

	@Override
	public boolean equals(Object obj) 
	{
		return name.equals(((Player)(obj)).getName());
	}

	private void decrementTrainsLeft(int num)
	{
		trainsLeft-=num;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Player o) {
		return o.points - this.points;
	}
}
