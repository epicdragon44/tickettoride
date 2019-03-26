public class Contract {
    private String start, end;
    private int value;

    public Contract(String s, String e, int v) {
        start = s;
        end = e;
        value = v;
    }

    private String getStart() {
        return start;
    }

    private String getEnd() {
        return end;
    }

    private int getValue() {
        return value;
    }
}
