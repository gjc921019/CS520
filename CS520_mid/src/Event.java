public class Event {
    public String type;
    public int processNum;
    public int RT;
    public int BT;

    public Event(String type,int processNum,int RT,int BT){
        this.type = type;
        this.RT = RT;
        this.processNum = processNum;
        this.BT = BT;
    }
}
