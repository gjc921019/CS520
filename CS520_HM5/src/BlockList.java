public class BlockList {
    public class Block {
        public int size;
        String process;
        Block next, prev;

        public Block (int size, String process) {
            this.size = size;
            this.process = process;
        }
    }

    public Block head;
    public BlockList() {
        head = null;
    }

    public Block getBlock(int index) {
        Block cur = head;
        for (int i = 0; i < index && cur != null; i++) {
            cur = cur.next;
        }
        return cur;
    }

    public Block getTail() {
        Block cur = head;
        for (int i = 0; cur != null && cur.next != null; i++) {
            cur = cur.next;
        }
        return cur;
    }

    public void addAtHead(int size, String process) {
        Block cur = new Block(size, process);
        cur.next = head;
        cur.prev = null;
        if (head != null) {
            head.prev = cur;
        }
        head = cur;
    }

    public void addAtTail(int size, String process) {
        Block cur = new Block(size, process);
        Block prev = getTail();
        if (prev != null) {
            prev.next = cur;
            cur.prev = prev;
            return;
        }
        addAtHead(size,process);
    }

    public void addAtIndex(int index, int size, String process) {
        if (index == 0) {
            addAtHead(size, process);
            return;
        }

        Block prev = getBlock(index-1);
        if (prev == null) {
            return;
        }

        Block cur = new Block(size,process);
        Block next = prev.next;
        prev.next = cur;
        cur.prev = prev;
        cur.next = next;
        if (next != null) {
            next.prev = cur;
        }
    }

    public void deleteAtIndex(int index) {
        Block cur = getBlock(index);
        if (cur == null) {
            return;
        }

        Block prev = cur.prev;
        Block next = cur.next;
        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }

        if (next != null) {
            next.prev = prev;
        }
    }

    public int indexOf(Block target){
        if(head==null){
            return -1;
        }
        Block cur = head;
        int i = 0;
        while (!(cur.process ==target.process && cur.size == target.size)){
            cur = cur.next;
            i++;
        }
        return i;
    }

    public void print(){
        Block cur = head;
        while(cur!=null){
            System.out.print(cur.process + "(" + cur.size +"k" + ")" + "; ");
            cur = cur.next;
        }
        System.out.println();
    }

    public static void main(String[] args){
        BlockList a = new BlockList();
        a.addAtIndex(0,1,"a");
        a.addAtIndex(1,1,"b");
        a.addAtIndex(2,1,"c");
        a.print();
    }


}
