import java.awt.Color;

public class Track {
	private int cost, player;
	private Node n1,n2;
	private Color color;
	private int x1,x2,y1,y2;

	public Track(Node s, Node f, Color col, int c)
	{
		n1=s;
		n2=f;
		color=col;
		cost=c;
		player=-1;
	}
	public Track(Node s,Node f,Color col,int c, int x,int y,int xx,int yy)
	{
		n1=s;
		n2=f;
		color=col;
		cost=c;
		player=-1;
		x1=x;
		x2=xx;
		y1=y;
		y2=yy;
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
	
	public boolean animateEquals(Object o)
	{
		Track t=(Track)o;
		return t.getPlayer()==player&&((n1.equals(t.getNode1())&&n2.equals(t.getNode2()))||(n2.equals(t.getNode1())&&n1.equals(t.getNode2())));
	}
	
	public Node getOtherNode(Node from)
	{
		if(from.equals(n1))
			return n2;
		return n1;
	}
	
	public int getY2() {
		return y2;
	}
	
	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getX1() {
		return x1;
	}
}
