import java.awt.Color;

public class TrainCard {
	private Color color;
	private boolean isWild;

	public TrainCard(Color col, boolean wild) {
		color = col;
		isWild = wild;
	}

	public boolean getWild() {
		return isWild;
	}

	public Color getColor() {
		return color;
	}

	public boolean equals(TrainCard b) {
		if (isWild == b.getWild())
			return true;
		if (color.equals(b.getColor()))
			return true;
		return false;
	}
}