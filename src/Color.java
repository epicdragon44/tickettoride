public class Color extends java.awt.Color {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Color RED = new Color(255, 0, 0);
	public static final Color GREEN = new Color(0, 255, 0);
	public static final Color BLUE = new Color(0, 0, 255);
	public static final Color YELLOW = new Color(255, 255, 0);
	public static final Color MAGENTA = new Color(255, 0, 255);
	public static final Color CYAN = new Color(0, 255, 255);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color GRAY = new Color(128, 128, 128);
	public static final Color LIGHT_GRAY = new Color(192, 192, 192);
	public static final Color DARK_GRAY = new Color(64, 64, 64);
	public static final Color PINK = new Color(255, 175, 175);
	public static final Color ORANGE = new Color(255, 200, 0);


	public Color(int r, int g, int b) {
		super(r, g, b);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean equals(Object o) {
		Color other = (Color)(o);
		return this.getRed()==other.getRed() && this.getBlue()==other.getBlue() && this.getGreen()==other.getGreen();
	}
	
	public static Color getColor(String n) {
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
		return new Color(100, 100, 100);
	}
}
