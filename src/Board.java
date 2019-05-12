import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This is a complex data structure class that manages the graph theory implementation of the game board. Full JavaDoc annotations are available for all of its methods
 */
public class Board {
	protected Node[] cities;
	private int maxLen;
	private ArrayList<Player> bestPlayer;
	private GameEngine daddyEngine;
	private Track last;

	/**
	 * Constructs a new Board
	 * @param game The overriding GameEngine
	 * @throws FileNotFoundException In case the file Nodes.txt isn't found
	 */
	public Board(GameEngine game) throws FileNotFoundException {
		this.daddyEngine = game;

		Scanner sc = new Scanner(new File("resources/Nodes.txt"));
		cities = new Node[36];
		int cnt = 0;
		ArrayList<String[]> cons=new ArrayList<String[]>();
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine());
			Node node = new Node(st.nextToken(), (Integer.parseInt(st.nextToken())), (Integer.parseInt(st.nextToken())));
			cons.add(sc.nextLine().split(","));
			cities[cnt++] = node;
		}
		for (int i=0;i<cons.size();i++) {
			for(int j=0;j<cons.get(i).length;j++)
			{
				StringTokenizer st = new StringTokenizer(cons.get(i)[j]);
				Node connex = findNode(st.nextToken());
				if (connex != null)
					cities[i].addConnection(connex, ColorType.getColor(st.nextToken().toUpperCase()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
			}
		}
		sc.close();
		maxLen = Integer.MIN_VALUE;
		bestPlayer = new ArrayList<>();
		last=null;
	}

	/**
	 * Calculates the cost of a connection between two nodes
	 * @param s Name of first node
	 * @param e Name of second node
	 * @return Cost of connection
	 */
	protected int connectionCost(String s, String e) {
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

	//beginning of contract completeness algorithm ...

	/**
	 * Determines if a Contract is completed
	 * @param c The Contract in question
	 * @return Boolean indicating completeness
	 */
	protected boolean isComplete(Contract c) {
		Node startNode = findNode(c.getStart());
		Node endNode = findNode(c.getEnd());
		if(startNode==null||endNode==null)
			return false;
		return searchFrom(startNode, new ArrayList<>(), endNode);
	}

	/**
	 * Helper method for isComplete to recursively search from a given node for a target
	 * @param n Starting node
	 * @param visited Empty Arraylist to record visited tracks
	 * @param target Ending node
	 * @return Boolean indicating if a path was found
	 */
	private boolean searchFrom(Node n, ArrayList<Track> visited, Node target) {
		if (n.equals(target)) {
			return true;
		}
		for (Track t : n.getConnections()) {
			if (!visited.contains(t) && t.getPlayer()==daddyEngine.currentPlayer) {
				visited.add(t);
				if(searchFrom(t.getOtherNode(n), visited, target))
					return true;
			}
		}
		return false;
	}
	//...end of contract completeness algorithm

	/**
	 * Get the most recently placed Track
	 * @return The most recently placed Track
	 */
	protected Track getLastPlaced()
	{
		return last;
	}

	/**
	 * Finds a node with the given name
	 * @param name String name of interest
	 * @return The Node with the given name, or null if none found
	 */
	protected Node findNode(String name) {
		for (Node n : cities) {
			if(n.toString().equals(name))
				return n;
		}
		return null;
	}

	/**
	 * Finds a node that contains the given coordinates
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @return The Node that contains the given coordinates, or null if none found
	 */
	protected Node findNode(int x, int y) {
		for (Node n : cities) {
			if (n.contains(x, y))
				return n;
		}
		return null;
	}

	/**
	 * Finds the track between two given nodes
	 * @param n First Node
	 * @param node Second Node
	 * @return The Track between the two nodes
	 */
	protected Track findTrack(Node n, Node node)
	{
		for(Track t:n.getConnections())
			if(t.getOtherNode(n).equals(node)&&t.getPlayer()==-1)
				return t;
		return null;
	}

	/**
	 * Checks if the node at the given coordinates is valid
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @return Boolean indicating validity
	 */
	protected Boolean isNodeEligible(int x,int y)
	{
		Node n=findNode(x,y);
		if(n==null)
			return null;
		for(Track t:n.getConnections())
			if(t.getPlayer()==-1)
				return true;
		return false;
	}

	/**
	 * Checks if the node at the given coordinates is valid
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @param n The Node to connect to
	 * @return Boolean indicating validity
	 */
	protected Boolean isNodeEligible(int x,int y,Node n)
	{
		Node node=findNode(x,y);
		if(node==null)
			return null;
		for(Track t:n.getConnections())
			if(t.getOtherNode(n).equals(node)&&t.getPlayer()==-1)
			{
				if(containsDuple(t, n)!=null&&containsDuple(t, n).getPlayer()==daddyEngine.currentPlayer)
					continue;
				return true;
			}
		return false;
	}

	/**
	 * Checks if there is a duplicate track
	 * @param t The Track
	 * @param n The Node whose tracks to check through for a duplicate
	 * @return Duplicate Track or null if none found
	 */
	private Track containsDuple(Track t, Node n) {
		for(Track tr:n.getConnections())
			if (t.getOtherNode(n).equals(tr.getOtherNode(n))&&(tr.getX1()!=t.getX1()||tr.getY1()!=t.getY1()))
				return tr;
		return null;
	}

	//beginning of longest train algorithm ...

	/**
	 * Find List of Player(s) with longest train(s)
	 * @param players Array of all players in game
	 * @return List of Player(s) with longest train(s)
	 */
	protected ArrayList<Player> findLongestTrainPlayer(Player[] players) {
		maxLen = Integer.MIN_VALUE;
		for (Player p : players)
			for (Node n : cities)
				visit(n, 0, new ArrayList<>(), p);
		return bestPlayer;
	}

	/**
	 * Helper method for findLongestTrainPlayer to visit nodes recursively searching for longest train
	 * @param n Starting node
	 * @param cnt Count of nodes; Pass in as zero
	 * @param visited List of Visited Tracks; Pass in empty
	 * @param p Player in question
	 */
	private void visit(Node n, int cnt, List<Track> visited, Player p) {
		if (cnt == maxLen&&!bestPlayer.contains(p))
			bestPlayer.add(p);
		if (cnt > maxLen) {
			maxLen = cnt;
			bestPlayer.clear();
			bestPlayer.add(p);
		}

		for (Track t : n.getConnections()) {
			if (t.getPlayer() != -1) { //check first to make sure someone actually owns the track lmfao
				if (p.equals(daddyEngine.players[t.getPlayer()]) && !contains(t,visited)) {
					visited.add(t);
					visit(t.getOtherNode(n), cnt + t.getCost(), visited, p);
				}
			}
		}
	}

	/**
	 * Checks if a List of tracks contains a certain track graphically
	 * @param t Track in question
	 * @param v List of Tracks to search
	 * @return Whether or not it is contained
	 */
	private boolean contains(Track t,List<Track> v)
	{
		for(Track tr:v)
			if(tr.animateEquals(t))
				return true;
		return false;
	}
	//... end of longest train algorithm

	/**
	 * Method to place trains
	 * @param player Player placing the trains
	 * @param c Color of the trains
	 * @param start Starting node
	 * @param end Ending node
	 * @return Whether placement was successful
	 */
	protected boolean placeTrains(int player, ColorType c, Node start, Node end) {
		ArrayList<Track> tracks = start.getConnections();
		int x=0,y=0;
		boolean available = false;
		for (Track t : tracks) {
			if (t.getOtherNode(start).equals(end) && ((c==null || t.getColor().equals(ColorType.GRAY) || t.getColor().equals(c)))) {
				if (t.getPlayer() == -1) {
					x=t.getX2();
					y=t.getY2();
					t.setPlayer(player);
					available = true;
					last=t;
					break;
				}
			}
		}
		if (available) {
			tracks = end.getConnections();
			for (Track t : tracks) {
				if (t.getOtherNode(end).equals(start) && ((c==null || t.getColor().equals(ColorType.GRAY) || t.getColor().equals(c))) && t.getX1()==x && t.getY1()==y) {
					if (t.getPlayer() == -1) {
						t.setPlayer(player);
						return true;
					}
				}
			}
			return true;
		}
		return false;
	}
}
