// rr 5 runs
public class RR1 {
    private Queue queueReady;
    private Queue queueIO;
    private EventList eventList;
    private int totalJobs = 10;
    private boolean CPU_busy = true; // first event is cpu_load
    private boolean IO_busy = false; // at clock 0, IO is not busy;

    private RR1(){
        queueReady = new Queue();
        queueIO = new Queue();
        for (int i = 0;i<totalJobs;i++) {
            queueReady.enqueue(i, 2*60*1000+i*10000,randomUniTime(30,75));
        }
        eventList = new EventList();
        Event e;
        int clock = 0;
        e= new Event("CPU_load",-1,-1,-1);
        eventList.addByClockNew(clock,e);
        //e= new Event("IO_load",-1,-1,-1);
        //eventList.addByClock(clock,e);
    }

    private void run(int q){
        System.out.println("**********************************");
        int tempTotalJobs = totalJobs;
        int curTime = 0;
        Event e;
        int clock = 0;
        int CPURunningTime = 0;
        int[] enterRQTime = new int[10];
        int[] exitRQTime = new int[10];
        int[] waitingTime = new int[10];
        int[] turnAroundTime = new int[10];
        EventList.MyNode cur = eventList.getNode(0);
        do{
            curTime = cur.clock;
            e = cur.event;
            switch(e.type){
                case "Arrival_R" :
                    enterRQTime[e.processNum] = curTime;
                    if (!CPU_busy){
                        CPU_busy = true;
                        //System.out.println("Arrival_R NOT busy"+" "+e.processNum +" at clock "+curTime);
                        queueReady.enqueue(e.processNum,e.RT,e.BT);
                        e = new Event("CPU_load",-1,-1,-1);
                        clock = curTime;
                        eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        queueReady.enqueue(e.processNum,e.RT,e.BT);
                        //System.out.println("Arrival_R busy"+" "+e.processNum +" at clock "+curTime);
                        cur = cur.next;
                        break;
                    }
                case "CPU_load" :
                    if (!queueReady.isEmpty()){
                        DLL.Node p = queueReady.dequeue();
                        int processNum = p.processNum;
                        int RT = p.RT;
                        int BT = randomExpTime(processNum);

                        exitRQTime[processNum] = curTime;
                        waitingTime[processNum] = waitingTime[processNum] +(exitRQTime[processNum]-enterRQTime[processNum]);

                        if (BT<RT){
                            if(BT>=q){
                                // return to Q_R
                                //System.out.println("CPU_load not empty BT>q"+" "+processNum +" at clock "+curTime);
                                e = new Event("Arrival_R",processNum,RT-q,BT); // BT doesn't matter
                                clock = curTime + q;
                                eventList.addByClockNew(clock,e);
                                e = new Event("CPU_load",-1,-1,-1);
                                eventList.addByClockNew(clock,e);
                                cur = cur.next;
                                CPURunningTime = CPURunningTime + q;
                                break;
                            }
                            else{
                                // go to IO_Q
                                //System.out.println("CPU_load NOT empty BT<q"+" "+processNum+" at clock "+curTime);
                                e = new Event("Arrival_IO",processNum,RT-BT,BT);
                                clock = curTime+BT;
                                eventList.addByClockNew(clock,e);
                                e = new Event("CPU_load",-1,-1,-1);
                                eventList.addByClockNew(clock,e);
                                cur = cur.next;
                                CPURunningTime = CPURunningTime + BT;
                                break;
                            }
                        }
                        else{
                            if(q<RT){
                                //return to Q_R
                                //System.out.println("CPU_load not empty q<RT"+" "+processNum +" at clock "+curTime);
                                e = new Event("Arrival_R",processNum,RT-q,BT); // BT doesn't matter
                                clock = curTime + q;
                                eventList.addByClockNew(clock,e);
                                e = new Event("CPU_load",-1,-1,-1);
                                eventList.addByClockNew(clock,e);
                                cur = cur.next;
                                CPURunningTime = CPURunningTime + q;
                                break;
                            }
                            else{
                                //finish
                                e = new Event("CPU_load",-1,-1,-1);
                                clock = curTime+RT;
                                eventList.addByClockNew(clock,e);
                                totalJobs--;
                                System.out.println("processNum is "+ processNum+" turnaround time is "+clock);
                                cur = cur.next;
                                CPURunningTime = CPURunningTime + RT;
                                turnAroundTime[processNum] = clock;
                                break;
                            }
                        }
                    }
                    else{
                        CPU_busy = false;
                        cur = cur.next;
                        //System.out.println("CPU_load empty"+" at clock "+curTime);
                        break;
                    }
                case "Arrival_IO" :
                    if (!IO_busy){
                        IO_busy = true;
                        queueIO.enqueue(e.processNum,e.RT,e.BT);
                        //System.out.println("Arrival_IO Not busy"+" "+e.processNum+" at clock "+curTime);
                        e = new Event("IO_load",-1,-1,-1);
                        clock = curTime;
                        eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        queueIO.enqueue(e.processNum,e.RT,e.BT);
                        //System.out.println("Arrival_IO busy"+" "+e.processNum+" at clock "+curTime);
                        cur = cur.next;
                        break;
                    }
                case "IO_load" :
                    if(!queueIO.isEmpty()){
                        DLL.Node p = queueIO.dequeue();
                        int processNum = p.processNum;
                        int RT = p.RT;
                        int BT = p.BT;
                        //System.out.println("IO_load NOT empty"+" "+ processNum+" at clock "+curTime);
                        e = new Event("Arrival_R",processNum,RT,BT);
                        clock = curTime+60;
                        eventList.addByClockNew(clock,e);
                        e = new Event("IO_load",-1,-1,-1);
                        eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        IO_busy = false;
                        cur = cur.next;
                        //System.out.println("IO_load empty"+" at clock "+curTime);
                        break;
                    }
            }
        } while (totalJobs>0);
        double CPU_Utilization = (double)(CPURunningTime)/(double)(clock);
        System.out.println("CPU utilization is "+CPU_Utilization);
        System.out.println("CPU running time is "+CPURunningTime);
        System.out.println("End Time is "+clock);

        double throughPut = 10.0/clock*1000;
        System.out.println("throughPut is "+throughPut);

        int sumWaitingTime = 0;
        int sumTurnAroundTime = 0;

        for (int i = 0; i<10;i++){
            System.out.println("ProcessNUM is " + i + " waiting time is " +waitingTime[i]);
            sumWaitingTime = sumWaitingTime+waitingTime[i];
            sumTurnAroundTime = sumTurnAroundTime + turnAroundTime[i];
        }

        int avgWaitingTime = sumWaitingTime/10;
        int avgTurnAroundTime = sumTurnAroundTime/10;
        System.out.println("AVG waiting time is "+avgWaitingTime);
        System.out.println("AVG turnAround time is "+avgTurnAroundTime);
    }

    private static int randomUniTime(int min, int max){
        return min+(int)(Math.random()*(max-min+1));
    }

    private static int randomExpTime(int processNum){
        //double lamda = 1.0/12; // mean rate = 5 per/min --> 1/12 per/sec, thus mean arrival rate = 1/12;
        int interRate = processNum*5+30;
        double lamda = 1.0/interRate; // mean rate = 2 per/min
        double a = Math.random();
        return (int)(-(1 / lamda) * Math.log(a));
    }

    public static void main(String[] args){
        for (int i = 0; i<5; i++){
            RR1 rr1 = new RR1();
            rr1.run(20+i*15);
        }
    }
}
