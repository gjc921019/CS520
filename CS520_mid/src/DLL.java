public class DLL {

    public class Node {
        int processNum;
        int RT;
        int BT;
        Node next, prev;

        public Node(int processNum, int RT, int BT) {
            this.processNum = processNum;
            this.RT = RT;
            this.BT = BT;
        }
    }

    private Node head;

    public DLL() {
        head = null;
    }

    public Node getNode(int index) {
        Node cur = head;
        for (int i = 0; i < index && cur != null; i++) {
            cur = cur.next;
        }
        return cur;
    }

    public Node getTail() {
        Node cur = head;
        for (int i = 0; cur != null && cur.next != null; i++) {
            cur = cur.next;
        }
        return cur;
    }

    public Node removeHead(){
        if (head==null){return null;};
        Node temp = head;
        head = head.next;
        return temp;
    }

    public void addAtHead(int processNum, int RT, int BT) {
        Node cur = new Node(processNum, RT, BT);
        cur.next = head;
        cur.prev = null;
        if (head != null) {
            head.prev = cur;
        }
        head = cur;
    }

    public void addAtTail(int processNum, int RT, int BT) {
        Node cur = new Node(processNum, RT, BT);
        Node prev = getTail();
        if (prev != null) {
            prev.next = cur;
            cur.prev = prev;
            return;
        }
        addAtHead(processNum, RT, BT);
    }

    public void addByBT(int processNum, int RT, int BT){
        Node cur = head;
        Node curPre;
        Node target = new Node(processNum,RT,BT);
        if (cur==null){
            addAtHead(processNum,RT,BT);
            return;
        }
        while (cur.BT <= BT) {
            curPre = cur;
            cur = cur.next;
            if (cur==null){
                curPre.next = target;
                target.prev = curPre;
                target.next = null;
                return;
            }
        }
        Node prev = cur.prev;
        if(prev==null){
            addAtHead(processNum,RT,BT);
            return;
        }
        prev.next = target;
        target.prev = prev;
        target.next = cur;
        cur.prev = target;
    }

    public boolean isEmpty(){
        if (head == null){
            return true;
        }
        else{
            return false;
        }
    }

    public void printNum(){
        Node cur =head;
        System.out.println("******************");
        while(cur!=null){
            System.out.println(cur.processNum+" "+cur.BT);
            cur = cur.next;
        }
        System.out.println("******************");
    }

    // addByTau is same with addByClockNew in EventList class
    public void addByTau(int processNum, int RT, int BT){
        Node cur = head;
        if (cur==null){
            addAtHead(processNum,RT,BT);
            return;
        }

        Node target = new Node(processNum,RT,BT);

        while(cur.next!=null){
            cur = cur.next;
        }
        if (cur.BT<=BT){
            cur.next = target;
            target.prev = cur;
            target.next = null;
            return;
        }

       Node p = head;
        if(p.BT>=BT){
            target.next = p;
            target.prev = null;
            p.prev = target;
            head = target;
            return;
        }

        while (p.BT<=BT){
            p=p.next;
        }
        Node pp = p.prev;
        pp.next = target;
        target.prev = pp;
        target.next = p;
        p.prev = target;
    }
}
