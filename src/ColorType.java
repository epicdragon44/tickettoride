import java.awt.*;

public class ColorType extends java.awt.Color {
	private static final long serialVersionUID = 1L;
	public static final ColorType RED = new ColorType(255, 0, 0);
	public static final ColorType GREEN = new ColorType(0, 255, 0);
	public static final ColorType BLUE = new ColorType(0, 0, 255);
	public static final ColorType YELLOW = new ColorType(255, 255, 0);
	public static final ColorType MAGENTA = new ColorType(255, 0, 255);
	public static final ColorType CYAN = new ColorType(0, 255, 255);
	public static final ColorType WHITE = new ColorType(255, 255, 255);
	public static final ColorType BLACK = new ColorType(0, 0, 0);
	public static final ColorType GRAY = new ColorType(128, 128, 128);
	public static final ColorType LIGHT_GRAY = new ColorType(192, 192, 192);
	public static final ColorType DARK_GRAY = new ColorType(64, 64, 64);
	public static final ColorType PINK = new ColorType(255, 175, 175);
	public static final ColorType ORANGE = new ColorType(255, 200, 0);


	public ColorType(int r, int g, int b) {
		super(r, g, b);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean equals(Object o) {
		ColorType other = getColorType((Color)(o));
		return this.getRed()==other.getRed() && this.getBlue()==other.getBlue() && this.getGreen()==other.getGreen();
	}
	private ColorType getColorType(Color o) {
		return new ColorType(o.getRed(), o.getGreen(), o.getBlue());
	}
	
	public static ColorType getColor(String n) {
		if (n.equals("RED"))
			return RED;
		else if (n.equals("GREEN"))
			return GREEN;
		else if (n.equals("BLUE"))
			return BLUE;
		else if (n.equals("YELLOW"))
			return YELLOW;
		else if (n.equals("MAGENTA"))
			return MAGENTA;
		else if (n.equals("CYAN"))
			return CYAN;
		else if (n.equals("WHITE"))
			return WHITE;
		else if (n.equals("BLACK"))
			return BLACK;
		else if (n.equals("GRAY"))
			return GRAY;
		else if (n.equals("LIGHT_GRAY"))
			return LIGHT_GRAY;
		else if (n.equals("DARK_GRAY"))
			return DARK_GRAY;
		else if (n.equals("PINK"))
			return PINK;
		else if (n.equals("ORANGE"))
			return ORANGE;
		return new ColorType(100, 100, 100);
	}

	public String toString() {
		ColorType n = this;
		if (n.equals(ColorType.getColor("RED")))
			return "red";
		else if (n.equals(ColorType.getColor("GREEN")))
			return "green";
		else if (n.equals(ColorType.getColor("BLUE")))
			return "blue";
		else if (n.equals(ColorType.getColor("YELLOW")))
			return "yellow";
		else if (n.equals(ColorType.getColor("MAGENTA")))
			return "magenta";
		else if (n.equals(ColorType.getColor("CYAN")))
			return "cyan";
		else if (n.equals(ColorType.getColor("WHITE")))
			return "white";
		else if (n.equals(ColorType.getColor("BLACK")))
			return "black";
		else if (n.equals(ColorType.getColor("GRAY")))
			return "gray";
		else if (n.equals(ColorType.getColor("LIGHT_GRAY")))
			return "lightgray";
		else if (n.equals(ColorType.getColor("DARK_GRAY")))
			return "darkgray";
		else if (n.equals(ColorType.getColor("PINK")))
			return "pink";
		else if (n.equals(ColorType.getColor("ORANGE")))
			return "orange";
		return super.toString();
	}
}
