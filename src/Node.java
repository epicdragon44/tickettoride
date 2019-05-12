import java.awt.*;
import java.util.ArrayList;

/**
 * Simple Data Structure class to represent a node in a graph, with an arraylist of connections, coordinates, a radius, and a name, as well as helper methods
 */
public class Node {
	private ArrayList<Track> connections;
	private int x, y;
	private static final int RADIUS = 7;
	private String name;

	public Node(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
		connections = new ArrayList<>();
	}

	public boolean equals(Object o) {
		Node node=((Node) (o));
		return node!=null&&this.name.equals(node.name);
	}

	public ArrayList<Track> getConnections() {
		return connections;
	}

	public void addConnection(Node n, Color color, int cost, int x1, int y1, int x2, int y2) {
		this.connections.add(new Track(this, n, color, cost, x1, y1, x2, y2));
	}

	public boolean contains(int x, int y) {
		return (Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2)) <= RADIUS);
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
