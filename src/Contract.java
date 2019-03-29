public class Contract {
    private String start, end;
    private int value;

    public Contract(int v,String s, String e) 
    {
        start = s;
        end = e;
        value = v;
    }
    
    protected String getStart() {
        return start;
    }

    protected String getEnd() {
        return end;
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
}
