/**
 * This class is a simple data structure that holds Strings indicating the start and end nodes of the contract, the contract's value, and whether it's complete
 */
public class Contract {
    private String start, end;
    private int value;
    private boolean complete;

    public Contract(int v,String s, String e) 
    {
        start = s;
        end = e;
        value = v;
        complete=false;
    }
    
    protected String getStart() {
        return start;
    }

    protected String getEnd() {
        return end;
    }

    public boolean isComplete()
    {
    	return complete;
    }
    
    public void checkComplete(Board b)
    {
    	if(!complete)
    		complete=b.isComplete(this);
    }
    
    public void setStart(String start) {
		this.start = start;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
        return value;
    }
	
	public boolean equals(Object o)
	{
		Contract c=(Contract)o;
		return start.equals(c.getStart())&&end.equals(c.getEnd());
	}
	
	public String toString()
	{
		return "gay";
	}
}
