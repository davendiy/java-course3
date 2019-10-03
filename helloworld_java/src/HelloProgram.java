import java.util.Scanner;

public class HelloProgram {
    public static void main(String[] args){
        Scanner read = new Scanner(System.in);
//        System.out.print("What's your name?\n--> ");
//        String name = read.nextLine();
//        System.out.println("Hello, " + name);

        int[] a = new int[10];
        int sum = 0;
        for (int i = 0; i < 10; i++){
            System.out.printf("a[%d] = ", i);
            a[i] = read.nextInt();
            sum += a[i];
        }
        for (int i = 9; i >= 0; i--){
            System.out.printf("a[%d] = %d\n", i, a[i]);
        }
        System.out.printf("sum = %d", sum);
    }
}
