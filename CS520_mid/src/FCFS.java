public class FCFS {
    private Queue queueReady;
    private Queue queueIO;
    private EventList eventList;
    private int totalJobs = 5;

    private FCFS(){
        queueReady = new Queue();
        queueIO = new Queue();
        for (int i = 0;i<totalJobs;i++) {
            queueReady.enqueue(i, randomUniTime(300,400),randomUniTime(30,75));
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
        FCFS fcfs = new FCFS();
        int curTime = 0;
        Event e;
        int clock = 0;
        int CPURunningTime = 0;
        int endTime = 0;
        EventList.MyNode cur = fcfs.eventList.getNode(0);
        do{
            curTime = cur.clock;
            e = cur.event;
            switch(e.type){
                case "Arrival_R" :
                    if (fcfs.queueReady.isEmpty()){
                        System.out.println("Arrival_R empty"+" "+e.processNum +" at clock "+curTime);
                        fcfs.queueReady.enqueue(e.processNum,e.RT,e.BT);
                        e = new Event("CPU_load",-1,-1,-1);
                        clock = curTime;
                        fcfs.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        fcfs.queueReady.enqueue(e.processNum,e.RT,e.BT);
                        System.out.println("Arrival_R NOT empty"+" "+e.processNum +" at clock "+curTime);
                        cur = cur.next;
                        break;
                    }
                case "CPU_load" :
                    if (!fcfs.queueReady.isEmpty()){
                        DLL.Node p = fcfs.queueReady.dequeue();
                        int processNum = p.processNum;
                        int RT = p.RT;
                        int BT = randomExpTime(processNum);
                        if(BT<RT){
                            System.out.println("CPU_load NOT empty"+" "+processNum+" at clock "+curTime);
                            e = new Event("Arrival_IO",processNum,RT-BT,BT);
                            clock = curTime+BT;
                            fcfs.eventList.addByClockNew(clock,e);
                            e = new Event("CPU_load",-1,-1,-1);
                            fcfs.eventList.addByClockNew(clock,e);
                            cur = cur.next;
                            CPURunningTime = CPURunningTime + BT;
                            break;
                        }
                        else{
                            e = new Event("CPU_load",-1,-1,-1);
                            clock = curTime+RT;
                            fcfs.eventList.addByClockNew(clock,e);
                            fcfs.totalJobs--;
                            System.out.println("finish"+" "+ processNum+" at "+clock);
                            cur = cur.next;
                            CPURunningTime = CPURunningTime + RT;
                            endTime = clock;
                            break;
                        }
                    }
                    else{
                        cur = cur.next;
                        System.out.println("CPU_load empty"+" at clock "+curTime);
                        break;
                    }
                case "Arrival_IO" :
                    if (fcfs.queueIO.isEmpty()){
                        fcfs.queueIO.enqueue(e.processNum,e.RT,e.BT);
                        System.out.println("Arrival_IO empty"+" "+e.processNum+" at clock "+curTime);
                        e = new Event("IO_load",-1,-1,-1);
                        clock = curTime;
                        fcfs.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        fcfs.queueIO.enqueue(e.processNum,e.RT,e.BT);
                        System.out.println("Arrival_IO NOT empty"+" "+e.processNum+" at clock "+curTime);
                        cur = cur.next;
                        break;
                    }
                case "IO_load" :
                    if(!fcfs.queueIO.isEmpty()){
                        DLL.Node p = fcfs.queueIO.dequeue();
                        int processNum = p.processNum;
                        int RT = p.RT;
                        int BT = p.BT;
                        System.out.println("IO_load NOT empty"+" "+ processNum+" at clock "+curTime);
                        e = new Event("Arrival_R",processNum,RT,BT);
                        clock = curTime+60;
                        fcfs.eventList.addByClockNew(clock,e);
                        e = new Event("IO_load",-1,-1,-1);
                        fcfs.eventList.addByClockNew(clock,e);
                        cur = cur.next;
                        break;
                    }
                    else{
                        cur = cur.next;
                        System.out.println("IO_load empty"+" at clock "+curTime);
                        break;
                    }
            }
        } while (fcfs.totalJobs>0);
        //double CPU_Utilization = (double)(CPURunningTime)/(double)(clock);
        //System.out.println("CPU utilization is "+CPU_Utilization);
        Event lastEvent = fcfs.eventList.getTail().event;
        int lastClock = fcfs.eventList.getTail().clock;
        System.out.println("CPU running time is "+CPURunningTime);
        System.out.println("Last clock is "+lastClock);
        System.out.println("Last event is " +lastEvent.type);
        System.out.println("End Time is "+endTime);
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
