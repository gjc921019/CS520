import java.util.ArrayList;
import java.util.Arrays;

public class busServe {
    private static int totalStop = 15;   // total bus stop is 15
    private static int[] queueOfPeople = new int[totalStop];  // each element in the array represents the size of the queue in this specific bus stop
    private MyLinkedList eventList;  // a doubly linked list which stores event in the sort of clock


    private busServe(){
        double time = 0; // initialize the clock
        int totalBus = 5; // total bus is 5
        eventList = new MyLinkedList(); // initialize the list
        Event e; // using event class to represent the event, please check the event class
        double clock; // the clock for the event
        for (int i = 0; i<totalStop; i++){  // initialize the waiting queue for each bus stop to be 1
            clock = time + randomTime(); // get the clock for the next coming person
            e = new Event ("person", i, -1); // create the next person event at the bus stop No.i, there is no information about bus, so let it be -1
            eventList.addByClock(clock,e); // store the person event into the list at clock time
            queueOfPeople[i]++; // the waiting queue in this specific bus stop ++
        }

        for (int j = 0; j<totalBus; j++){ //initialize the arrival event for every 3 bus stop
            e = new Event("arrival",j*3,j); // create the next arrival event for every 3 bus stop. Btw, the corresponding bus number is j.
            eventList.addByClock(0,e); // store the arrival event into the list at clock time
        }
    }

    public static void main(String args[]){
        busServe bs = new busServe();
        double boardTime = 2; // boardTime
        double driveTime = 300; //driveTime
        int hour = 8;
        double endTime = 3600*hour; // total simulation time
        double clock;
        double curTime = 0; // current time
        double[] busPosition = new double[] {0,0,3,6,9,12};
        // initialize the bus position at the beginning. the first element represent the current time which now is 0, the other element represent the position of each bus.
        // For example, the first bus is at bus stop 0, the second bus is at bus stop 3
        double[] startPosition = {0,0,3,6,9,12};
        ArrayList<double[]> busPositionList = new ArrayList<>(); // this arrayList is to store the busPosition array when it has been updated
        busPositionList.add(startPosition); // store the original bus position
        Event e;
        int timeStone = 3600;// timeStone is used to get the snapShot of the busPosition hourly
        int[][] queueMatrix = new int[hour][totalStop];// Matrix to store the array of waiting queue by hour
        int row = 0; // each row represents one hour, hence at the beginning, the row is set to be 0

        MyLinkedList.MyNode cur = bs.eventList.getNode(0); // get the first node of eventList which represents the first event
        do{
            curTime = cur.clock; // set the current time to be the clock of the current event
            e = cur.event; // e is the current event
            if(curTime>timeStone){ // update the matrix of queue hourly
                queueMatrix[row] = queueOfPeople.clone();
                row++; // increase the row
                timeStone = timeStone+3600;//increase the hour
            }

            switch(e.type){
                case "person" : // if the current event is person type
                    queueOfPeople[e.stop] ++; // the waiting queue of this specific bus stop increases
                    clock = curTime + randomTime(); // get the clock for the next person event
                    e = new Event ("person",e.stop, e.bus); // create the person event, stop and bus won't change
                    bs.eventList.addByClock(clock,e); // put this newly created event into the correct position of eventList by its clock
                    cur = cur.next; // get the next event node
                    break;
                case "boarder" : // if the current event is boarder
                    if (queueOfPeople[e.stop] != 0){ // if the waiting queue at this bus stop is not empty
                        e = new Event("boarder",e.stop,e.bus); // create a new boarder event at this same bus stop. The bus number also won't change
                        clock = curTime + boardTime; // get the clock for the next boarder event
                        bs.eventList.addByClock(clock,e); //put this newly created event into the correct position of eventList by its clock
                        queueOfPeople[e.stop]--; // waiting queue of this specific bus stop --
                        cur = cur.next;// get the next event node
                        break;
                    }
                    else{ // if the waiting queue at this bus stop is empty
                        clock = curTime+ driveTime; // get the clock for the next arrival event
                        e = new Event("arrival", (e.stop+1)%totalStop, e.bus); // create a new arrival event at the next bus stop. The bus number is same.
                        bs.eventList.addByClock (clock,e); //put this newly created event into the correct position of eventList by its clock
                        cur = cur.next; // get the next event node
                        // the following part before the break is to update the array of busPosition and write it into the arrayList busPositionList
                        busPosition[0] = clock;
                        busPosition[e.bus+1] = e.stop;// because e has been updated to the new one, so we don't need to say (e.stop+1)%totalStop;
                        double[] temp = busPosition.clone();
                        busPositionList.add(temp);

                        break;
                    }
                case "arrival" :
                    if (queueOfPeople[e.stop] !=0){ // if the waiting queue is not empty
                        e = new Event("boarder",e.stop,e.bus); // create a new boarder event at this certain bus stop
                        clock = curTime; // the clock for the boarder event is same with current time
                        bs.eventList.addByClock(clock,e); // put the boarder event into the eventList
                        cur = cur.next; // get the next event node
                        break;
                    }
                    else{ // if the waiting queue is empty
                        e = new Event("arrival", (e.stop+1)%totalStop, e.bus); // create a new arrival event at the next stop
                        clock = curTime + driveTime; // clock for the arrival event
                        bs.eventList.addByClock(clock,e); // put it into the list
                        cur = cur.next; // get the next node

                        //System.out.println("it is nobody in the stop!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        // the following part before the break is to update the array of busPosition and write it into the arrayList busPositionList
                        busPosition[0] = clock;
                        busPosition[e.bus+1] = e.stop; // because e has been updated to the new one, so we don't need to say (e.stop+1)%totalStop;
                        double[] temp = busPosition.clone();
                        busPositionList.add(temp);
                        break;
                    }
            }

        }
        while(curTime<=endTime); // stop simulation
        // print bus Position by time
        System.out.println("busPosition by time is:");
        for (int j=0; j<busPositionList.size(); j = j+busPositionList.size()/16 ){
            double[] output = busPositionList.get(j);
            System.out.println(Arrays.toString(output));
        }
        double[] lastPosition = busPositionList.get(busPositionList.size()-1);
        System.out.println(Arrays.toString(lastPosition));
        System.out.println("******************************************");

        // Average, max and minimum size for waiting queue in each stop
        int[] avgOfEach = new int[totalStop];
        int[] maxOfEach = new int[totalStop];
        int[] minOfEach = new int[totalStop];
        for(int j = 0; j <totalStop; j++){
            int sum = 0;
            int max = 0;
            int min = 10000000;
            for (int i = 0; i<hour; i++){
                sum = sum + queueMatrix[i][j];
                if (max<queueMatrix[i][j]){
                    max = queueMatrix[i][j];
                }
                if (min>queueMatrix[i][j]){
                    min = queueMatrix[i][j];
                }
            }
            avgOfEach[j] = sum/hour;
            maxOfEach[j] = max;
            minOfEach[j] = min;
        }
        System.out.println("AvgSize of each stop is "+Arrays.toString(avgOfEach));
        System.out.println("Max of each stop is "+Arrays.toString(maxOfEach));
        System.out.println("Min of each stop is "+Arrays.toString(minOfEach));
        int sumAll = 0;
        for (int i = 0; i < totalStop;i++){
            sumAll = sumAll+avgOfEach[i];
        }
        int avgAll = sumAll/totalStop;
        System.out.println("avgSize of all busStop is "+avgAll);

    }
    // generate the randomTime which follows the exp distribution
    private static double randomTime(){
        //double lamda = 1.0/12; // mean rate = 5 per/min --> 1/12 per/sec, thus mean arrival rate = 1/12;
        double lamda = 1.0/120; // mean rate = 2 per/min
        double a = Math.random();
        return -(1 / lamda) * Math.log(a);
    }

}
