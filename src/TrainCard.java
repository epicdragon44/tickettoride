import java.awt.Color;

public class TrainCard {
	private Color color;
	private boolean isWild;

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