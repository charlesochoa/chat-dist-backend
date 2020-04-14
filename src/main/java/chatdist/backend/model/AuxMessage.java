package chatdist.backend.model;

public class AuxMessage {

    private String from;
    private String to;
    private String msg;

    protected AuxMessage() {}

    public AuxMessage(String from, String to, String msg) {
        this.from = from;
        this.to = to;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, name='%s', email='%s']",
                from, to, msg);
    }

    public String getFrom() { return from; }

    public String getTo() { return to; }

    public String getMsg() { return msg; }


}
