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
		trainCards.put(new TrainCard(Color.black,false), 0);
		trainCards.put(new TrainCard(Color.orange,false), 0);
		trainCards.put(new TrainCard(Color.red,false), 0);
		trainCards.put(new TrainCard(Color.blue,false), 0);
		trainCards.put(new TrainCard(Color.green,false), 0);
		trainCards.put(new TrainCard(Color.pink,false), 0);
		trainCards.put(new TrainCard(Color.white,false), 0);
		trainCards.put(new TrainCard(null,true), 0);
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
		trainCards.put(train,trainCards.get(train)+1);
	}
	
	public void placeTrains(int num, Color col)
	{
		TrainCard temp=new TrainCard(col,false);
		decrementtrainsLeft(num);
		while(num!=0&&trainCards.get(temp)!=0)
		{
			trainCards.put(temp,trainCards.get(temp)-1);
			num--;
		}
		temp=new TrainCard(null,true);
		while(num!=0)
		{
			trainCards.put(temp,trainCards.get(temp)-1);
			num--;
		}
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
}
