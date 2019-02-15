// correct version for SJF
public class SJF {
    private Queue queueReady;
    private Queue queueIO;
    private EventList eventList;
    private int totalJobs = 10;
    private boolean CPU_busy = true;
    private boolean IO_busy = false;
    private int[] curTau;

    private SJF(){
        queueReady = new Queue();
        queueIO = new Queue();
        curTau = new int[totalJobs];
        for (int i = 0; i<totalJobs;i++){
            //curTau[i] = randomUniTime(30,75);
            curTau[i] = 30+i*5;
        }
        for (int i = 0;i<totalJobs;i++) {
            queueReady.enqueueByTau(i, randomUniTime(2*60*1000,4*60*1000),curTau[i]);
        }
        eventList = new EventList();
        Event e;
        int clock = 0;
        e= new Event("CPU_load",-1,-1,-1);
        eventList.addByClockNew(clock,e);
        //e= new Event("IO_load",-1,-1,-1);
        //eventList.addByClock(clock,e);
    }

    public static void main(String[] args){
        SJF sjf = new SJF();
        //sjf.queueReady.printNum();
        int curTime = 0;
        Event e;
        int clock = 0;
        int CPURunningTime = 0;
        int[] enterRQTime = new int[10];
        int[] exitRQTime = new int[10];
        int[] waitingTime = new int[10];
        int[] turnAroundTime = new int[10];
        double alpha = 1;
        EventList.MyNode cur = sjf.eventList.getNode(0);
        do{
            curTime = cur.clock;
            e = cur.event;
            switch(e.type){
                case "Arrival_R" :
                    enterRQTime[e.processNum] = curTime;
                    if (!sjf.CPU_busy){
                        sjf.CPU_busy = true;
                        //System.out.println("Arrival_R empty"+" "+e.processNum +" at clock "+curTime);
                        int processNum = e.processNum;
                        int RT = e.RT;
                        int BT = e.BT;
                        sjf.curTau[processNum] = (int)(alpha*BT + (1-alpha)*sjf.curTau[processNum]);
                        sjf.queueReady.enqueueByTau(processNum,RT,sjf.curTau[processNum]);
                        //sjf.queueReady.printNum();
                        e = new Event("CPU_load",-1,-1,-1);
                        clock = curTime;
                        sjf.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        int processNum = e.processNum;
                        int RT = e.RT;
                        int BT = e.BT;
                        sjf.curTau[processNum] = (int)(alpha*BT + (1-alpha)*sjf.curTau[processNum]);
                        sjf.queueReady.enqueueByTau(processNum,RT,sjf.curTau[processNum]);
                        //System.out.println("Arrival_R NOT empty"+" "+e.processNum +" at clock "+curTime);
                        //sjf.queueReady.printNum();
                        cur = cur.next;
                        break;
                    }
                case "CPU_load" :
                    if (!sjf.queueReady.isEmpty()){
                        DLL.Node p = sjf.queueReady.dequeue();
                        int processNum = p.processNum;
                        int RT = p.RT;
                        int BT = randomExpTime(processNum);

                        exitRQTime[processNum] = curTime;
                        waitingTime[processNum] = waitingTime[processNum] +(exitRQTime[processNum]-enterRQTime[processNum]);

                        if(BT<RT){
                            //System.out.println("CPU_load NOT empty"+" "+processNum+" at clock "+curTime);
                            e = new Event("Arrival_IO",processNum,RT-BT,BT);
                            clock = curTime+BT;
                            sjf.eventList.addByClockNew(clock,e);
                            e = new Event("CPU_load",-1,-1,-1);
                            sjf.eventList.addByClockNew(clock,e);
                            cur = cur.next;
                            CPURunningTime = CPURunningTime + BT;
                            break;
                        }
                        else{
                            e = new Event("CPU_load",-1,-1,-1);
                            clock = curTime+RT;
                            sjf.eventList.addByClockNew(clock,e);
                            sjf.totalJobs--;
                            System.out.println("processNum is "+ processNum+" turn around time is "+clock);
                            cur = cur.next;
                            turnAroundTime[processNum] = clock;
                            break;
                        }
                    }
                    else{
                        sjf.CPU_busy = false;
                        cur = cur.next;
                        //System.out.println("CPU_load empty"+" at clock "+curTime);
                        break;
                    }
                case "Arrival_IO" :
                    if (!sjf.IO_busy){
                        sjf.IO_busy = true;
                        sjf.queueIO.enqueue(e.processNum,e.RT,e.BT);
                        //System.out.println("Arrival_IO empty"+" "+e.processNum+" at clock "+curTime);
                        e = new Event("IO_load",-1,-1,-1);
                        clock = curTime;
                        sjf.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        sjf.queueIO.enqueue(e.processNum,e.RT,e.BT);
                        //System.out.println("Arrival_IO NOT empty"+" "+e.processNum+" at clock "+curTime);
                        cur = cur.next;
                        break;
                    }
                case "IO_load" :
                    if(!sjf.queueIO.isEmpty()){
                        DLL.Node p = sjf.queueIO.dequeue();
                        int processNum = p.processNum;
                        int RT = p.RT;
                        int BT = p.BT;
                        //System.out.println("IO_load NOT empty"+" "+ processNum+" at clock "+curTime);
                        e = new Event("Arrival_R",processNum,RT,BT);
                        clock = curTime+60;
                        sjf.eventList.addByClockNew(clock,e);
                        e = new Event("IO_load",-1,-1,-1);
                        sjf.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        sjf.IO_busy = false;
                        cur = cur.next;
                        //System.out.println("IO_load empty"+" at clock "+curTime);
                        break;
                    }
            }
        } while (sjf.totalJobs>0);
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
}
