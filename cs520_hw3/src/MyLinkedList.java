public class MyLinkedList {
    //MyLinkedList is a doubly linked list. For each node, it can store two objects. One is the clock, the other is the event.
    // the most important method is addByClock method, which is to insert a new node into the linked List at certain position, decided by the clock value
    public class MyNode {
        double clock;
        Event event;
        MyNode next, prev;

        public MyNode(double clock, Event event) {
            this.clock = clock;
            this.event = event;
        }
    }

    private MyNode head;

    public MyLinkedList() {
        head = null;
    }

    public MyNode getNode(int index) {
        MyNode cur = head;
        for (int i = 0; i < index && cur != null; i++) {
            cur = cur.next;
        }
        return cur;
    }

    public MyNode getTail() {
        MyNode cur = head;
        for (int i = 0; cur != null && cur.next != null; i++) {
            cur = cur.next;
        }
        return cur;
    }

    public void addAtHead(double clock, Event event) {
        MyNode cur = new MyNode(clock, event);
        cur.next = head;
        cur.prev = null;
        if (head != null) {
            head.prev = cur;
        }
        head = cur;
    }

    public void addAtTail(double clock, Event event) {
        MyNode cur = new MyNode(clock, event);
        MyNode prev = getTail();
        if (prev != null) {
            prev.next = cur;
            cur.prev = prev;
            return;
        }
        addAtHead(clock, event);
    }

    public void addAtIndex(int index, double clock, Event event) {
        if (index == 0) {
            addAtHead(clock, event);
            return;
        }

        MyNode prev = getNode(index);
        if (prev == null) {
            return;
        }

        MyNode cur = new MyNode(clock, event);
        MyNode next = prev.next;
        prev.next = cur;
        cur.prev = prev;
        cur.next = next;
        if (next != null) {
            next.prev = cur;
        }
    }

    public void deleteAtIndex(int index) {
        MyNode cur = getNode(index);
        if (cur == null) {
            return;
        }

        MyNode prev = cur.prev;
        MyNode next = cur.next;
        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }

        if (next != null) {
            next.prev = prev;
        }
    }

    public void addByClock(double clock, Event event){
        MyNode cur = head;
        MyNode curPre;
        MyNode target = new MyNode(clock,event);
        if (cur==null){
            addAtHead(clock,event);
            return;
        }
        while (cur.clock <= clock) {
            curPre = cur;
            cur = cur.next;
            if (cur==null){
                curPre.next = target;
                target.prev = curPre;
                target.next = null;
                return;
            }
        }
        MyNode prev = cur.prev;
        if(prev==null){
            addAtHead(clock,event);
            return;
        }
        prev.next = target;
        target.prev = prev;
        target.next = cur;
        cur.prev = target;
    }

    public void printClock(){
        MyNode cur = head;
        while(cur!=null){
            System.out.println(cur.clock);
            cur = cur.next;
        }
    }

    public boolean isEmpty(){
        if (head == null){
            return false;
        }
        else{
            return true;
        }
    }

}
