public class SJF1 {
    private Queue queueReady;
    private Queue queueIO;
    private EventList eventList;
    private int totalJobs = 10;
    private boolean CPU_busy = true;
    private boolean IO_busy = false;
    private int[] curTau;

    private SJF1(){
        queueReady = new Queue();
        queueIO = new Queue();
        curTau = new int[totalJobs];
        for (int i = 0; i<totalJobs;i++){
            //curTau[i] = randomUniTime(30,75);
            curTau[i] = 30+i*5;
        }
        for (int i = 0;i<totalJobs;i++) {
            queueReady.enqueueByTau(i, 2*60*1000+i*10000,curTau[i]);
        }
        eventList = new EventList();
        Event e;
        int clock = 0;
        e= new Event("CPU_load",-1,-1,-1);
        eventList.addByClockNew(clock,e);
        //e= new Event("IO_load",-1,-1,-1);
        //eventList.addByClock(clock,e);
    }

    public void run(double alpha){
        SJF1 sjf1 = new SJF1();
        System.out.println("************************************");
        //sjf1.queueReady.printNum();
        int curTime = 0;
        Event e;
        int clock = 0;
        int CPURunningTime = 0;
        int[] enterRQTime = new int[10];
        int[] exitRQTime = new int[10];
        int[] waitingTime = new int[10];
        int[] turnAroundTime = new int[10];
        //double alpha = 1;
        EventList.MyNode cur = sjf1.eventList.getNode(0);
        do{
            curTime = cur.clock;
            e = cur.event;
            switch(e.type){
                case "Arrival_R" :
                    enterRQTime[e.processNum] = curTime;
                    if (!sjf1.CPU_busy){
                        sjf1.CPU_busy = true;
                        //System.out.println("Arrival_R empty"+" "+e.processNum +" at clock "+curTime);
                        int processNum = e.processNum;
                        int RT = e.RT;
                        int BT = e.BT;
                        sjf1.curTau[processNum] = (int)(alpha*BT + (1-alpha)*sjf1.curTau[processNum]);
                        sjf1.queueReady.enqueueByTau(processNum,RT,sjf1.curTau[processNum]);
                        //sjf1.queueReady.printNum();
                        e = new Event("CPU_load",-1,-1,-1);
                        clock = curTime;
                        sjf1.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        int processNum = e.processNum;
                        int RT = e.RT;
                        int BT = e.BT;
                        sjf1.curTau[processNum] = (int)(alpha*BT + (1-alpha)*sjf1.curTau[processNum]);
                        sjf1.queueReady.enqueueByTau(processNum,RT,sjf1.curTau[processNum]);
                        //System.out.println("Arrival_R NOT empty"+" "+e.processNum +" at clock "+curTime);
                        //sjf1.queueReady.printNum();
                        cur = cur.next;
                        break;
                    }
                case "CPU_load" :
                    if (!sjf1.queueReady.isEmpty()){
                        DLL.Node p = sjf1.queueReady.dequeue();
                        int processNum = p.processNum;
                        int RT = p.RT;
                        int BT = randomExpTime(processNum);

                        exitRQTime[processNum] = curTime;
                        waitingTime[processNum] = waitingTime[processNum] +(exitRQTime[processNum]-enterRQTime[processNum]);

                        if(BT<RT){
                            //System.out.println("CPU_load NOT empty"+" "+processNum+" at clock "+curTime);
                            e = new Event("Arrival_IO",processNum,RT-BT,BT);
                            clock = curTime+BT;
                            sjf1.eventList.addByClockNew(clock,e);
                            e = new Event("CPU_load",-1,-1,-1);
                            sjf1.eventList.addByClockNew(clock,e);
                            cur = cur.next;
                            CPURunningTime = CPURunningTime + BT;
                            break;
                        }
                        else{
                            e = new Event("CPU_load",-1,-1,-1);
                            clock = curTime+RT;
                            sjf1.eventList.addByClockNew(clock,e);
                            sjf1.totalJobs--;
                            System.out.println("processNum is "+ processNum+" turnaround time is "+clock);
                            cur = cur.next;
                            turnAroundTime[processNum] = clock;
                            break;
                        }
                    }
                    else{
                        sjf1.CPU_busy = false;
                        cur = cur.next;
                        //System.out.println("CPU_load empty"+" at clock "+curTime);
                        break;
                    }
                case "Arrival_IO" :
                    if (!sjf1.IO_busy){
                        sjf1.IO_busy = true;
                        sjf1.queueIO.enqueue(e.processNum,e.RT,e.BT);
                        //System.out.println("Arrival_IO empty"+" "+e.processNum+" at clock "+curTime);
                        e = new Event("IO_load",-1,-1,-1);
                        clock = curTime;
                        sjf1.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        sjf1.queueIO.enqueue(e.processNum,e.RT,e.BT);
                        //System.out.println("Arrival_IO NOT empty"+" "+e.processNum+" at clock "+curTime);
                        cur = cur.next;
                        break;
                    }
                case "IO_load" :
                    if(!sjf1.queueIO.isEmpty()){
                        DLL.Node p = sjf1.queueIO.dequeue();
                        int processNum = p.processNum;
                        int RT = p.RT;
                        int BT = p.BT;
                        //System.out.println("IO_load NOT empty"+" "+ processNum+" at clock "+curTime);
                        e = new Event("Arrival_R",processNum,RT,BT);
                        clock = curTime+60;
                        sjf1.eventList.addByClockNew(clock,e);
                        e = new Event("IO_load",-1,-1,-1);
                        sjf1.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        sjf1.IO_busy = false;
                        cur = cur.next;
                        //System.out.println("IO_load empty"+" at clock "+curTime);
                        break;
                    }
            }
        } while (sjf1.totalJobs>0);
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
        for (int i = 0; i<3; i++){
            SJF1 sjf1 = new SJF1();
            sjf1.run(1.0/(i+1));
        }
    }
}
