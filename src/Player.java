import java.awt.Color;
import java.util.*;

public class Player 
{
	private String name;
	private int points;
	private int trainsLeft;
	private ArrayList<Contract> contracts;
	private HashMap<TrainCard, Integer> trainCards;
	
	public Player(String n)
	{
		name=n;
		points=0;
		trainsLeft=45;
		contracts=new ArrayList<Contract>();
		trainCards=new HashMap<TrainCard, Integer>();
		trainCards.put(key, 0);
		trainCards.put(key, 0);
		trainCards.put(key, 0);
		trainCards.put(key, 0);
		trainCards.put(key, 0);
		trainCards.put(key, 0);
		trainCards.put(key, 0);
		trainCards.put(key, 0);
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
		points += num;
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
	
	public HashMap<TrainCard, Integer> getTrainCards()
	{
		return trainCards;
	}
	
	public void drawTrainCards(TrainCard train)
	{
		trainCards.set(train,trainCards.get(train)+1);
	}
	
	public void placeTrains(int num, Color col)
	{
		//to be implemented
	}
	
	public int getScore()
	{
		return points;
	}

	@Override
	public boolean equals(Object obj) {
		return this.name.equals(((Player)(obj)).name);
	}

	private void decrementtrainsLeft(int num) {
		trainsLeft-=num;
	}
}
