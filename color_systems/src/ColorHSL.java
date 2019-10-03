import java.util.Scanner;

/**
 * Implementation of HSL color system.
 *
 * created: 02.10.2019
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 */
public class ColorHSL implements Color {

    private float hue;
    private float saturation;
    private float lightness;

    /**
     * Constructor. Just assigns respective variables.
     *
     * @param pH - hue value, must be in range [0, 1]
     * @param pS - saturation value, must be in range [0, 1]
     * @param pL - lightness value, must be in range [0, 1]
     * @throws IllegalArgumentException if above conditions are violated
     */
    ColorHSL(float pH, float pS, float pL) throws IllegalArgumentException {
        if (pH < 0 || pH > 1 ||
                pS < 0 || pS > 1 ||
                pL < 0 || pL > 1){
            throw new IllegalArgumentException("Arguments must be in range [0, 1]");
        }
        hue = pH;
        saturation = pS;
        lightness = pL;
    }

    /**
     * Implementation of abstract method Color.input().
     * Just inputs necessary params via console.
     */
    @Override
    public void input(){
        Scanner in = new Scanner(System.in);
        System.out.print("Please, enter HSL values in range [0, 1]:\n--> H = ");
        hue = in.nextFloat();
        System.out.print("--> S = ");
        saturation = in.nextFloat();
        System.out.print("--> L = ");
        lightness = in.nextFloat();
    }

    /**
     * Implementation of abstract method Color.repr()
     *
     * @return String 'ColorRGB(<hue>, <saturation>, <lightness>)'
     */
    @Override
    public String toString(){
        return String.format("ColorHSL[hue=%f, saturation=%f, lightness=%f]", hue, saturation, lightness);
    }

    /**
     * Sum (mix) of two colors.
     *
     * Result will be converted sum of respective ColorRGBs.
     */
    public static ColorHSL sum(ColorHSL a, ColorHSL b){
        return ColorRGB.sum(a.toRGB(), b.toRGB()).toHSL();
    }

    /**
     * Bitwise Intersection of two colors.
     *
     * Result will be converted bitwise intersection of respective
     * ColorRGBs.
     */
    public static ColorHSL bitwiseIntersection(ColorHSL a, ColorHSL b){
        return ColorRGB.bitwiseIntersection(a.toRGB(), b.toRGB()).toHSL();
    }

    /**
     * Bitwise Union of two colors.
     *
     * Result will be converted bitwise Union of respective
     * ColorRGBs.
     */
    public static ColorHSL bitwiseUnion(ColorHSL a, ColorHSL b){
        return ColorRGB.bitwiseUnion(a.toRGB(), b.toRGB()).toHSL();
    }

    /**
     * Bitwise XOR of two colors.
     *
     * Result will be converted bitwise xor of respective
     * ColorRGBs.
     */
    public static ColorHSL bitwiseXOR(ColorHSL a, ColorHSL b){
        return ColorRGB.bitwiseXOR(a.toRGB(), b.toRGB()).toHSL();
    }

    /**
     * Converts into RGB color system.
     * Uses class Converter.
     *
     * @return ColorRGB - result of converting.
     */
    public ColorRGB toRGB(){
        int[] tmp = Converter.hslToRgb(hue, saturation, lightness);
        return new ColorRGB(tmp[0], tmp[1], tmp[2]);
    }

    /**
     * Converts into CMYK color system.
     * Uses class Converter.
     *
     * @return ColorCMYK - result of converting.
     */
    public ColorCMYK toCMYK(){
        int[] tmp = Converter.hslToCmyk(hue, saturation, lightness);
        return new ColorCMYK(tmp[0], tmp[1], tmp[2], tmp[3]);
    }

    /**
     * Implementation of abstract method Color.valueInt().
     * Returns the valueInt for respective ColorRGB.
     *
     * @return integer number - integer representation of color.
     */
    @Override
    public int valueInt(){
        return toRGB().valueInt();
    }

    /**
     * Implementation of abstract method Color.valueFloat().
     * Returns the valueFloat for respective ColorRGB.
     *
     * @return float number - float representation of color.
     */
    @Override
    public float valueFloat(){
        return toRGB().valueFloat();
    }
}
