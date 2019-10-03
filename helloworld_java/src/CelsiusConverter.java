import java.util.Scanner;
import java.util.Random;

public class CelsiusConverter {
    public static void main(String[] args){
        Scanner read = new Scanner(System.in);

        System.out.println("What kind of convert do you want: C2F or F2C:\n--> ");
        String tmp = read.next();

        System.out.println("Please, input the temperature using Celsius");
        float tempCelsius = read.nextFloat();
        float tempFahrenheit = C2F(tempCelsius);
        System.out.printf("In Fahrenheit: %f \n", tempFahrenheit);
    }

    private static float C2F(float temperature){
        return temperature * 9.0f / 5.0f + 32;
    }

    public static float F2C(float temperature){
        return (temperature - 32) * 5.0f / 9.0f;
    }
}
