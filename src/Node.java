import java.awt.*;
import java.util.ArrayList;

public class Node {
	private ArrayList<Track> connections;
	private int x, y;
	private static final int RADIUS = 7;
	private String name;

	public Node(String name, int x, int y) {
		this.name = name;
		this.x = x - 7;
		this.y = y - 7;
		connections = new ArrayList<>();
	}

	public boolean equals(Object o) {
		return this.name.equals(((Node) (o)).name);
	}

	public ArrayList<Track> getConnections() {
		return connections;
	}

	public void addConnection(Node n, Color color, int cost) {
		this.connections.add(new Track(this, n, color, cost, System.nanoTime()));
		n.connections.add(new Track(this, n, color, cost, System.nanoTime()));
	}

	public boolean contains(int x, int y) {
		return (Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2)) <= 3 * RADIUS);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String toString() {
		return name;
	}
}
