class StackNode {
    private int aData;
    private StackNode aNext;

    StackNode(int data){
        aData = data;
        aNext = null;
    }

    public void setData(int data){
        aData = data;
    }

    public void setNext(StackNode next){
        aNext = next;
    }

    public StackNode getNext(){
        return aNext;
    }

    public int getData(){
        return aData;
    }
}
