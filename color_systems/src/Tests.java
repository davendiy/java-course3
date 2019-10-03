import java.io.FileWriter;
import java.io.IOException;

public class Tests {

    public static void main(String[] args){
        ColorRGB test1 = new ColorRGB(12, 14, 145);
        test1.input();

        System.out.println("Testing converter for ColorRGB:");
        System.out.println(test1);
        System.out.println(test1.toHSL());
        System.out.println(test1.toCMYK());

        System.out.println("\n\nTesting direct and inverse converting:");
        System.out.println("\nRGB -> HSL -> RGB");
        System.out.println(test1.toHSL().toRGB());

        System.out.println("\nRGB -> CMYK -> RGB");
        System.out.println(test1.toCMYK().toRGB());

        System.out.println("\nRGB -> HSL -> CMYK");
        System.out.println(test1.toHSL().toCMYK());

        System.out.println("\nRGB -> CMYK -> HSL");
        System.out.println(test1.toCMYK().toHSL());

        System.out.printf("valueInt = %d\n", test1.valueInt());
        System.out.print("valueHex = " + test1.valueHex());

        Color[] test_array = {new ColorRGB(1, 2, 3),
                              new ColorHSL(0.5f, 0.3f, 0.1f),
                              new ColorCMYK(24, 25, 152, 64)};
        try{
            FileWriter output = new FileWriter("output.txt");
            Color.writeToFile(test_array, output);
            output.close();
        } catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
