import java.util.Scanner;

public class QuadraticSolver {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        double[] coeffs = readCoefs(in);

    }

    private static double[] readCoefs(Scanner read){
        System.out.println("Please, write the coeffitients of ax^2 + bx + c = 0: ");
        System.out.print("a = ");
        double a = read.nextDouble();
        System.out.print("\nb = ");
        double b = read.nextDouble();
        System.out.print("\nc = ");
        double c = read.nextDouble();
        return new double[]{a, b, c};
    }
}
