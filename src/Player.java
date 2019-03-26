import java.awt.Color;
import java.util.*;

public class Player 
{
	private String name;
	private int points;
	private int trainsLeft;
	private ArrayList<Contract> contracts;
	private HashMap<TrainCard, Integer> trainCards;
	
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
		//to be implemented
	}
	
	public HashMap<TrainCard, Integer>getTrainCards()
	{
		//to be implemented
	}
	
	public void drawTrainCards(TrainCard train)
	{
		//to be implemented
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
