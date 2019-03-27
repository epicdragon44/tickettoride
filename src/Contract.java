public class Contract {
    private String start, end;
    private int value;

    public Contract(String s, String e, int v) {
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

	private int getValue() {
        return value;
    }
}
