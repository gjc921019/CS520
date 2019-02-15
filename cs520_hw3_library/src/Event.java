public class Event {
    private double clock;
     String type;
    private int stop;
    private int bus;

    public Event(double clock, String type,int stop,int bus){
        this.clock = clock;
        this.type = type;
        this.stop = stop;
        this.bus = bus;
    }

    public String toString(){
        return type +" " + stop + " " +bus;
    }
}
