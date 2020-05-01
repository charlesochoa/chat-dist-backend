package chatdist.backend.model;

public class AuxMessage {

    private String from;
    private String to;
    private String msg;

    public AuxMessage() {
        this.from = "";
        this.to = "";
        this.msg = "";
    }

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


    public void setFrom(String f) { from=f; }

    public void setTo(String t) { to=t; }

    public void setMsg(String m) { msg=m; }


}
