package tickettoride;
import java.awt.Color;

public class Track {
	private int cost, player;
	private Node n1,n2;
	private Color color;
	
	public Track(Node s,Node f,Color col,int c)
	{
		n1=s;
		n2=f;
		color=col;
		cost=c;
		player=-1;
	}
	
	public boolean setPlayer(int i)
	{
		if(player!=-1)
			return false;
		player=i;
		return true;
	}
	
	public int getPlayer()
	{
		return player;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public int getCost()
	{
		return cost;
	}
	
	public Node getNode1()
	{
		return n1;
	}
	
	public Node getNode2()
	{
		return n2;
	}
	
	public boolean equals(Object o)
	{
		Track t=(Track)o;
		return n1.equals(t.getNode1())&&n2.equals(t.getNode2());
	}
	
	public Node getOtherNode(Node from)
	{
		if(from.equals(n1))
			return n2;
		return n1;
	}
}
