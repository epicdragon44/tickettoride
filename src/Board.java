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
		
	}
	
	private void searchFrom(Node n,ArrayList<Track> visited) {

	}

	public Node findNode(String start) {

	}

	public String findNode(int x, int y) {

	}

	public Player findLongestTrainPlayer(List<Node> nodes, Player[] players) {

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
