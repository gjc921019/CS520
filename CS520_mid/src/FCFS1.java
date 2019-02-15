// correct version for FCFS
public class FCFS1 {
    private Queue queueReady;
    private Queue queueIO;
    private EventList eventList;
    private int totalJobs = 10;
    private boolean CPU_busy = true; // first event is cpu_load
    private boolean IO_busy = false; // at clock 0, IO is not busy;

    private FCFS1(){
        queueReady = new Queue();
        queueIO = new Queue();
        for (int i = 0;i<totalJobs;i++) {
            queueReady.enqueue(i, randomUniTime(2*60*1000,4*60*1000),randomUniTime(30,75));
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
        FCFS1 fcfs1 = new FCFS1();
        int curTime = 0;
        Event e;
        int clock = 0;
        int CPURunningTime = 0;
        int[] enterRQTime = new int[10];
        int[] exitRQTime = new int[10];
        int[] waitingTime = new int[10];

        int[] turnAroundTime = new int[10];

        //int[] interruptTime = new int[10];

//        int[] enterIOTime = new int[10];
//        int[] exitIOTime = new int[10];
//        int[] waitingIOTime = new int[10];

        EventList.MyNode cur = fcfs1.eventList.getNode(0);
        do{
            curTime = cur.clock;
            e = cur.event;
            switch(e.type){
                case "Arrival_R" :
                    enterRQTime[e.processNum] = curTime;
                    if (!fcfs1.CPU_busy){
                        fcfs1.CPU_busy = true;
                        //System.out.println("Arrival_R NOT busy"+" "+e.processNum +" at clock "+curTime);
                        fcfs1.queueReady.enqueue(e.processNum,e.RT,e.BT);
                        e = new Event("CPU_load",-1,-1,-1);
                        clock = curTime;
                        fcfs1.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        fcfs1.queueReady.enqueue(e.processNum,e.RT,e.BT);
                        //System.out.println("Arrival_R busy"+" "+e.processNum +" at clock "+curTime);
                        cur = cur.next;
                        break;
                    }
                case "CPU_load" :
                    if (!fcfs1.queueReady.isEmpty()){
                        DLL.Node p = fcfs1.queueReady.dequeue();
                        int processNum = p.processNum;
                        int RT = p.RT;
                        int BT = randomExpTime(processNum);

                        exitRQTime[processNum] = curTime;
//                        if (processNum==9){
//                            int temp = exitRQTime[processNum]-enterRQTime[processNum];
//                            System.out.println("job 9 waiting time is "+temp +" at clock " +curTime);
//                        }
                        waitingTime[processNum] = waitingTime[processNum] +(exitRQTime[processNum]-enterRQTime[processNum]);

                        if(BT<RT){
                           // System.out.println("CPU_load NOT empty"+" "+processNum+" at clock "+curTime);
                            e = new Event("Arrival_IO",processNum,RT-BT,BT);
                            clock = curTime+BT;
                            fcfs1.eventList.addByClockNew(clock,e);
                            e = new Event("CPU_load",-1,-1,-1);
                            fcfs1.eventList.addByClockNew(clock,e);
                            cur = cur.next;
                            CPURunningTime = CPURunningTime + BT;
                            break;
                        }
                        else{
                            e = new Event("CPU_load",-1,-1,-1);
                            clock = curTime+RT;
                            fcfs1.eventList.addByClockNew(clock,e);
                            fcfs1.totalJobs--;
                            System.out.println("processNum is "+ processNum+" turn around time is "+clock);
                            cur = cur.next;
                            CPURunningTime = CPURunningTime + RT;
                            turnAroundTime[processNum] = clock;
                            break;
                        }
                    }
                    else{
                        fcfs1.CPU_busy = false;
                        cur = cur.next;
                        //System.out.println("CPU_load empty"+" at clock "+curTime);
                        break;
                    }
                case "Arrival_IO" :
                    //interruptTime[e.processNum]++;
                    //enterIOTime[e.processNum] = curTime;
                    if (!fcfs1.IO_busy){
                        fcfs1.IO_busy = true;
                        fcfs1.queueIO.enqueue(e.processNum,e.RT,e.BT);
                        //System.out.println("Arrival_IO Not busy"+" "+e.processNum+" at clock "+curTime);
                        e = new Event("IO_load",-1,-1,-1);
                        clock = curTime;
                        fcfs1.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        fcfs1.queueIO.enqueue(e.processNum,e.RT,e.BT);
                        //System.out.println("Arrival_IO busy"+" "+e.processNum+" at clock "+curTime);
                        cur = cur.next;
                        break;
                    }
                case "IO_load" :
                    if(!fcfs1.queueIO.isEmpty()){
                        DLL.Node p = fcfs1.queueIO.dequeue();
                        int processNum = p.processNum;
                        int RT = p.RT;
                        int BT = p.BT;

                        //exitIOTime[processNum] = curTime;
                        //waitingIOTime[processNum] = waitingIOTime[processNum] +(exitIOTime[processNum]-enterIOTime[processNum]);

                        //System.out.println("IO_load NOT empty"+" "+ processNum+" at clock "+curTime);
                        e = new Event("Arrival_R",processNum,RT,BT);
                        clock = curTime+60;
                        fcfs1.eventList.addByClockNew(clock,e);
                        e = new Event("IO_load",-1,-1,-1);
                        fcfs1.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        fcfs1.IO_busy = false;
                        cur = cur.next;
                        //System.out.println("IO_load empty"+" at clock "+curTime);
                        break;
                    }
            }
        } while (fcfs1.totalJobs>0);
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
        System.out.println("AVG turn around time is "+avgTurnAroundTime);

        //int sumIOWaitingTime = 0;

//        for (int i = 0; i<10;i++){
//            System.out.println("ProcessNUM is " + i + " IO waiting time is " +waitingIOTime[i]);
//            sumIOWaitingTime = sumIOWaitingTime+waitingIOTime[i];
//        }

//        int avgIOWaitingTime = sumIOWaitingTime/10;
//        System.out.println("AVG IO waiting time is "+avgIOWaitingTime);


//        for (int i = 0; i<10;i++){
//            System.out.println("ProcessNUM is " + i + " interrupt time is " +interruptTime[i]);
//            //sumWaitingTime = sumWaitingTime+waitingTime[i];
//        }
    }

    private static int randomUniTime(int min, int max){
        return min+(int)(Math.random()*(max-min+1));
    }

    private static int randomExpTime(int processNum){
        //double lamda = 1.0/12; // mean rate = 5 per/min --> 1/12 per/sec, thus mean arrival rate = 1/12;
        int interRate = processNum*5+30;
        //double lamda = 1.0*interRate;
        double lamda = 1.0/interRate; // mean rate = 2 per/min
        double a = Math.random();
        return (int)(-(1 / lamda) * Math.log(a));
    }
}
