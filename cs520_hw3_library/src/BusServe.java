import java.util.*;

public class BusServe {
    private int totalStop = 2;
    private int[] queue = new int[totalStop];
    private LinkedList<Event> eventList;

    private double randomTime(){
        double lamda = 1.0/12; // mean rate = 5 per/min --> 1/12 per/sec, thus mean arrival rate = 1/12;
        double a = Math.random();
        return -(1 / lamda) * Math.log(a);
    }

    public BusServe(){
        double time = 0;
        eventList = new LinkedList<>();

        double clock1 = time+randomTime();
        Event e= new Event(clock1,"person", 0, -1);
        eventList.add(e);
        queue[0] = 1;

        double clock2 = time+randomTime();
        e = new Event(clock2,"person", 1, -1);
        if (clock2>=clock1){
            eventList.add(e);
        }
        else {
            eventList.addFirst(e);
        }
        queue[1] = 1;

        e = new Event(time,"arrival",0,0);
        eventList.addFirst(e);

    }

    public void main(String args[]){
        Event e = eventList.get(0);
        queue[e.stop] ++;
    }







}
