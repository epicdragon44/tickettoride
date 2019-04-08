import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Board {
	private Node[] cities;
	private boolean found;
	private int maxLen;
	private Player bestPlayer;
	
	public Board() throws IOException {
		Scanner sc = new Scanner(new File("Nodes.txt"));
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine());
			Node node = new Node(st.nextToken(), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
			String[] connections = sc.nextLine().split(",");
			for (String s : connections) {
				StringTokenizer yeet = new StringTokenizer(s);
				Node connex = null;
				if (findNode(yeet.nextToken())!=null) {
					connex = findNode(yeet.nextToken());
					node.addConnection(connex, Color.getColor(yeet.nextToken()), Integer.parseInt(yeet.nextToken()));
				}
			}
		}
		sc.close();

		found = false;
		maxLen = Integer.MIN_VALUE;
		bestPlayer = null;
	}

	public int connectionCost(String s, String e) {
		Node start = findNode(s);
		Node end = findNode(e);
		ArrayList<Track> tracks = start.getConnections();
		for (Track t : tracks) {
			if (t.getOtherNode(start).equals(end)) {
				return t.getCost();
			}
		}
		return -1;
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
	
	public Player findLongestTrainPlayer(Player[] players) {
		maxLen = Integer.MIN_VALUE;
		for (Player p : players)
			for (Node n : cities)
				visit(n, 0, new ArrayList<>(), p);
		return bestPlayer;
	}

	private void resetVisitedTracks() {

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
	
	public boolean placeTrains(int player, Color c, Node start, Node end) {
		ArrayList<Track> tracks = start.getConnections();
		boolean available=false;
		for (Track t : tracks) {
			if (t.getOtherNode(start).equals(end)&&(t.getColor().equals(Color.GRAY)||t.getColor().equals(c))) {
				if (t.getPlayer()==-1) {
					t.setPlayer(player);
					available=true;
				}
			}
		}
		if(available)
		{
			tracks=end.getConnections();
			for (Track t : tracks) {
				if (t.getOtherNode(start).equals(end)&&(t.getColor().equals(Color.GRAY)||t.getColor().equals(c))) {
					if (t.getPlayer()==-1) {
						t.setPlayer(player);
						return true;
					}
				}
			}
		}
		return false;
	}
}
