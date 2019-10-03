import java.util.Scanner;

public class TestScanner {

    public static void main(String[] args){
        Scanner in = new Scanner (System.in);
        System.out.print("input a number:");
        int num = in.nextInt();

        System.out.printf("Your number:%d \n", num);
        in.close();
    }
}
