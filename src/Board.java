import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Board {
	public Node[] cities;
	protected int maxLen;
	private Player bestPlayer;
	private GameEngine daddyEngine;

	public Board(GameEngine game) throws Exception {
		this.daddyEngine = game;

		Scanner sc = new Scanner(new File("Nodes.txt"));
		cities = new Node[36];
		int cnt = 0;
		ArrayList<String[]> cons=new ArrayList<String[]>();
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine());
			Node node = new Node(st.nextToken(), (int) (Integer.parseInt(st.nextToken()) ), (int) (Integer.parseInt(st.nextToken())));
			cons.add(sc.nextLine().split(","));
			cities[cnt++] = node;
		}
		for (int i=0;i<cons.size();i++) {
			for(int j=0;j<cons.get(i).length;j++)
			{
				StringTokenizer yeet = new StringTokenizer(cons.get(i)[j]);
				Node connex = findNode(yeet.nextToken());
				if (connex != null)
					cities[i].addConnection(connex, ColorType.getColor(yeet.nextToken().toUpperCase()), Integer.parseInt(yeet.nextToken()), Integer.parseInt(yeet.nextToken()), Integer.parseInt(yeet.nextToken()), Integer.parseInt(yeet.nextToken()), Integer.parseInt(yeet.nextToken()));
			}
		}
		sc.close();
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

	//beginning of contract completeness algorithm ...
	public boolean isComplete(Contract c) {
		Node startNode = findNode(c.getStart());
		Node endNode = findNode(c.getEnd());
		return searchFrom(startNode, new ArrayList<>(), endNode);
	}

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

	public Node findNode(String name) {
		for (Node n : cities) {
			if(n.toString().equals(name))
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
	
	public Track findTrack(Node n, Node node)
	{
		for(Track t:n.getConnections())
			if(t.getOtherNode(n).equals(node)&&t.getPlayer()==-1)
				return t;
		return null;
	}
	
	public Boolean isNodeEligible(int x,int y)
	{
		Node n=findNode(x,y);
		if(n==null)
			return null;
		for(Track t:n.getConnections())
			if(t.getPlayer()==-1)
				return true;
		return false;
	}
	
	public Boolean isNodeEligible(int x,int y,Node n)
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
	
	private Track containsDuple(Track t, Node n) {
		for(Track tr:n.getConnections())
			if (t.getOtherNode(n).equals(tr.getOtherNode(n))&&(tr.getX1()!=t.getX1()||tr.getY1()!=t.getY1()))
				return tr;
		return null;
	}

	//beginning of longest train algorithm ...

	public Player findLongestTrainPlayer(Player[] players) {
		maxLen = Integer.MIN_VALUE;
		for (Player p : players)
			for (Node n : cities)
				visit(n, 0, new ArrayList<>(), p);
		return bestPlayer;
	}

	private void visit(Node n, int cnt, List<Track> visited, Player p) {
		if (cnt > maxLen) {
			maxLen = cnt;
			bestPlayer = p;
		}

		for (Track t : n.getConnections()) {
			if (t.getPlayer() != -1) { //check first to make sure someone actually owns the track lmfao
				if (p.equals(daddyEngine.players[t.getPlayer()]) && visited.contains(t)) {
					visited.add(t);
					visit(t.getOtherNode(n), cnt + 1, visited, p);
				}
			}
		}
	}
	//... end of longest train algorithm

	public boolean placeTrains(int player, ColorType c, Node start, Node end) {
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
