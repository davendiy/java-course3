
public class Stack {

    private StackNode head;

    Stack(){
        head = null;
    }

    public boolean isEmpty(){
        return head == null;
    }

    public void push(int data){
        if (isEmpty()){
            head = new StackNode(data);
        } else {
            StackNode tmp = new StackNode(data);
            tmp.setNext(head);
            head = tmp;
        }
    }

    public int pop(){
        if (isEmpty()){
            System.out.print("Error: Access to the empty stack");
            System.exit(1);
        }

        StackNode tmp = head;
        head = tmp.getNext();
        return tmp.getData();
    }

    public static void main(String[] args){
        Stack test = new Stack();
        test.push(10);
        test.push(2);
        test.push(3);
        System.out.printf("first: %d\n", test.pop());
        System.out.printf("second: %d\n", test.pop());
        System.out.printf("third: %d\n", test.pop());
        System.out.printf("fourth (exception): %d\n", test.pop());
    }
}
