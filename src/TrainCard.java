import java.awt.Color;

public class TrainCard {
	private Color color;
	private boolean isWild;

	
	public TrainCard(Color col,boolean wild)
	{
		color=col;
		isWild=wild;
	}

	public boolean getwild() {
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