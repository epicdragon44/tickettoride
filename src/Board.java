import java.util.ArrayList;
import java.util.List;

public class Board {
	private Node[] cities;

	private boolean found;

	private int maxLen;
	private Player bestPlayer;
	
	public Board() {
		
	}

	public int connectionCost(String start, String end) {
		
	}
	
	public boolean isComplete(Contract c) {
		Node startNode = findNode(c.getStart());
		Node endNode = findNode(c.getEnd());
		searchFrom(startNode, new ArrayList<>(), endNode);
		return found;
	}
	
	private void searchFrom(Node n, ArrayList<Track> visited, Node target) {
		if (n.equals(target)) {
			found = true;
			return;
		}
		for (Track t : n.getConnections()) {
			if (!visited.contains(t)) {
				visited.add(t);
				searchFrom(t.getOtherNode(n), visited, target);
			}
		}
	}

	public Node findNode(String name) {
		for (Node n : cities) {
			if (n.toString().equals(name))
				return n;
		}
		return null;
	}

	public Node findNode(int x, int y) {
		for (Node n : cities) {
			if (n.contains(x, y))
				return n;
		}
		return null;
	}

	public Player findLongestTrainPlayer(Node[] nodes, Player[] players) {
		maxLen = Integer.MIN_VALUE;
		for (Player p : players)
			for (Node n : nodes)
				visit(n, 0, new ArrayList<>(), p);
		return bestPlayer;
	}

	private void visit(Node n, int cnt, List<Track> visited, Player p) {
		if (cnt > maxLen) {
			maxLen = cnt;
			bestPlayer = p;
		}

		for (Track t : n.getConnections())
		if (p.equals(t.getPlayer()) && visited.contains(t)) {
			visited.add(t);
			visit(t.getOtherNode(n), cnt + 1, visited, p);
		}
	}

	public boolean placeTrains(int player, String start, String end) {

	}
}
