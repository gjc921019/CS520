public class Queue {
    private DLL impl;
    public Queue() { impl = new DLL(); }

    public void enqueue(int processNum, int RT, int BT) { //O(1)
        impl.addAtTail(processNum,RT,BT); //O(1)
    }

    public DLL.Node dequeue() {
        return impl.removeHead();
    }

    public boolean isEmpty() {
        return impl.isEmpty();
    }

    public void enqueueByTau(int processNum, int RT, int Tau){
        //impl.addByBT(processNum,RT,Tau); // Tau is BT here;
        impl.addByTau(processNum,RT,Tau);
    }

    public void printNum(){
        impl.printNum();
    }

    public static void main(String[] args){
        Queue queueReady = new Queue();
        int[] curTau = new int[10];
        for (int i=0;i<10;i++){
            curTau[i] = randomUniTime(30,75);
        }
        for (int i = 0;i<10;i++) {
            queueReady.enqueueByTau(i, randomUniTime(2000,4000),curTau[i]);
        }
        for (int i=0; i<10;i++){
            System.out.println(queueReady.dequeue().BT);
        }
    }

    private static int randomUniTime(int min, int max){
        return min+(int)(Math.random()*(max-min+1));
    }

    /*public static void main(String[] args){
       int[] arr = new int[10000];
       for (int i = 0; i<10000;i++){
           arr[i] = randomExpTime(2);
       }
       int sum = 0;
       for (int i =0; i<10000; i++) {
           sum = sum+arr[i];
       }
       System.out.println(sum/10000);

    }

    private static int randomUniTime(){
        int max = 2*60*1000;
        int min = 5*6*1000;
        return min+(int)(Math.random()*(max-min+1));
    }

    private static int randomExpTime(int processNum){
        //double lamda = 1.0/12; // mean rate = 5 per/min --> 1/12 per/sec, thus mean arrival rate = 1/12;
        int interRate = processNum*5+30;
        double lamda = 1.0/interRate; // mean rate = 2 per/min
        double a = Math.random();
        return (int)(-(1 / lamda) * Math.log(a));
    }*/
}
