import java.util.ArrayList;

public class Node {
    private ArrayList<Track> connections;
    private int x, y;
    private static final int RADIUS = 7;
    private String name;

    public Node(String name, int x, int y) {

    }

    public boolean equals(Object o) {
        return this.name.equals(((Node) (o)).name);
    }

    public int connectionCost(String start, String end) {

    }

    public ArrayList<Track> getConnections() {
        return connections;
    }

    public boolean contains(int x, int y) {
        return (Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2)) <= RADIUS);
    }
}
