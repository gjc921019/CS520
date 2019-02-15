public class BuddySystemNew {
    private int totalSize = 2*1024;
    private BlockList bl;

    public BuddySystemNew(){
        this.bl = new BlockList();
        bl.addAtTail(this.totalSize,"idle");
    }

    public void allocate(String process, int size) {
        int r = (int)(Math.log10(size) / Math.log10(2)) + 1;
        int blockSize = (int)Math.pow(2, r);

        int min = totalSize+1;
        BlockList.Block targetBlock = null;

        BlockList.Block cur = bl.head;
        while (cur!=null){
            if (!cur.process.equals("idle")){
                cur = cur.next;
                continue;
            }
            if(cur.size>=blockSize){
                if(min>cur.size){
                    targetBlock = cur;
                    //System.out.println("targetBlock is "+targetBlock.process+" "+ targetBlock.size +" pos is " + bl.indexOf(targetBlock));
                    min = cur.size;
                }
            }
            cur = cur.next;
        }

        int pos = bl.indexOf(targetBlock);
        while(targetBlock.size/2 >= blockSize) {
                //int index = bl.indexOf(targetBlock);
                bl.deleteAtIndex(pos);
                bl.addAtIndex(pos,targetBlock.size/2,"idle");
                bl.addAtIndex(pos+1,targetBlock.size/2,"idle");
                //bl.print();
                targetBlock = this.bl.getBlock(pos);
                //pos = index;
        }

        this.bl.deleteAtIndex(pos);
        this.bl.addAtIndex(pos,targetBlock.size,process);
        bl.print();
    }

    public void deallcoate(String process) {
        BlockList.Block cur = this.bl.head;
        int pos = 0;
        while (!cur.process.equals(process)){
            cur = cur.next;
            pos++;
        }
        this.bl.deleteAtIndex(pos);
        this.bl.addAtIndex(pos,cur.size,"idle");
        bl.print();
    }

    public static void main(String[] args) {
        BuddySystemNew bs = new BuddySystemNew();
        System.out.println("After allocating process A:");
        bs.allocate("A", 20);
        System.out.println("After allocating process B:");
        bs.allocate("B", 35 );
        System.out.println("After allocating process C:");
        bs.allocate("C", 90 );
        System.out.println("After allocating process D:");
        bs.allocate("D", 40 );
        System.out.println("After allocating process E:");
        bs.allocate("E", 240);
        System.out.println("************************************************************");
        System.out.println("After deallocating process D:");
        bs.deallcoate("D");
        System.out.println("After deallocating process A:");
        bs.deallcoate("A");
        System.out.println("After deallocating process C:");
        bs.deallcoate("C");
        System.out.println("After deallocating process B:");
        bs.deallcoate("B");
        System.out.println("After deallocating process E:");
        bs.deallcoate("E");

    }

}
