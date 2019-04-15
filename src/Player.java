import java.awt.Color;
import java.util.*;

public class Player 
{
	private String name;
	private int points;
	private int trainsLeft;
	private ArrayList<Contract> contracts;
	private HashMap<Color, Integer> trainCards;
	
	public Player(String n)
	{
		name=n;
		points=0;
		trainsLeft=45;
		contracts=new ArrayList<Contract>();
		trainCards=new HashMap<Color, Integer>();
		trainCards.put(Color.black, 0);
		trainCards.put(Color.orange, 0);
		trainCards.put(Color.red, 0);
		trainCards.put(Color.blue, 0);
		trainCards.put(Color.green, 0);
		trainCards.put(Color.pink, 0);
		trainCards.put(Color.white, 0);
		trainCards.put(Color.yellow, 0);
		trainCards.put(null, 0);
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public void addPoints(int num)
	{
		points+=num;
	}
	
	public int trainsLeft()
	{
		return trainsLeft;
	}
	
	public ArrayList<Contract> getContract()
	{
		return contracts;
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
	
	public HashMap<Color, Integer> getTrainCards()
	{
		return trainCards;
	}
	
	public void drawTrainCards(TrainCard train)
	{
		trainCards.put(train.getColor(),trainCards.get(train.getColor())+1);
	}
	
	public ArrayList<TrainCard> placeTrains(int num, Color col)
	{
		decrementtrainsLeft(num);
		ArrayList<TrainCard> rtn=new ArrayList<TrainCard>();
		while(num!=0&&trainCards.get(col)!=0)
		{
			trainCards.put(col,trainCards.get(col)-1);
			rtn.add(new TrainCard(col,false));
			num--;
		}
		while(num!=0)
		{
			trainCards.put(null,trainCards.get(null)-1);
			rtn.add(new TrainCard(null,true));
			num--;
		}
		return rtn;
	}
	
	public int getScore()
	{
		return points;
	}

	@Override
	public boolean equals(Object obj) 
	{
		return name.equals(((Player)(obj)).getName());
	}

	private void decrementtrainsLeft(int num)
	{
		trainsLeft-=num;
	}

	@Override
	public String toString() {
		return name;
	}
}
