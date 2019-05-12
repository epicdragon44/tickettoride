/**
 * Simple Data Structure class to represent a Train Card, with a color and a boolean to check if it is wild
 */
public class TrainCard {
	private ColorType color;
	private boolean isWild;
	
	public TrainCard(ColorType col,boolean wild)
	{
		color=col;
		isWild=wild;
	}

	public boolean getwild() {
		return isWild;
	}

	public ColorType getColor()
	{
		return color;
	}

	public boolean equals(TrainCard b) {
		return (isWild&&b.getwild())||color.equals(b.getColor());
	}
	
	public String toString()
	{
		return ""+isWild;
	}
}
