import java.awt.Color;

public class TrainCard {
	private Color color;
	private boolean isWild;
<<<<<<< HEAD
	
	public TrainCard(Color col,boolean wild)
	{
		color=col;
		isWild=wild;
	}
	
	public boolean getwild()
	{
=======

	public boolean getwild() {
>>>>>>> bc9d9257efa490e2638c216d58200b71e8c7727d
		return isWild;
	}

	public Color getColor() {
		return color;
	}

	public boolean equals(TrainCard b) {
		if (color.equals(b.getColor()))
			return true;
		return false;
	}
}