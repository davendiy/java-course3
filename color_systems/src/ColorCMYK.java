import java.util.Scanner;

/**
 * Implementation of RGB color system.
 *
 * created: 02.10.2019
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 */
public class ColorCMYK implements Color{

    private int cyan;
    private int magenta;
    private int yellow;
    private int black;

    /**
     * Constructor. Just assigns respective variables.
     *
     * @param pC - cyan value, must be in range [0, 255]
     * @param pM - magenta value, must be in range [0, 255]
     * @param pY - yellow value, must be in range [0, 255]
     * @param pK - black value, must be in range [0, 255]
     * @throws IllegalArgumentException if above conditions are violated
     */
    ColorCMYK(int pC, int pM, int pY, int pK) throws IllegalArgumentException {
        if (pC < 0 || pC > 255 ||
                pM < 0 || pM > 255 ||
                pY < 0 || pY > 255 ||
                pK < 0 || pK > 255){
            throw new IllegalArgumentException("Arguments must be in range [0, 255]");
        }
        cyan = pC;
        magenta = pM;
        yellow = pY;
        black = pK;
    }

    /**
     * Implementation of abstract method Color.input().
     * Just inputs necessary params via console.
     */
    @Override
    public void input(){
        Scanner in = new Scanner(System.in);
        System.out.print("Please, enter CMYK values in range [0, 255]:\n--> C = ");
        cyan = in.nextInt();
        System.out.print("--> M = ");
        magenta = in.nextInt();
        System.out.print("--> Y = ");
        yellow = in.nextInt();
        System.out.print("--> K = ");
        black = in.nextInt();
    }

    /**
     * Implementation of abstract method Color.repr()
     *
     * @return String 'ColorRGB(<cyan>, <magenta>, <yellow>, <black>)'
     */
    @Override
    public String toString(){
        return String.format("ColorCMYK[cyan=%d, magenta=%d, yellow=%d, black=%d]", cyan, magenta, yellow, black);
    }

    /**
     * Sum (mix) of two colors.
     *
     * Result will be converted sum of respective ColorRGBs.
     */
    public static ColorCMYK sum(ColorCMYK a, ColorCMYK b) {
        return ColorRGB.sum(a.toRGB(), b.toRGB()).toCMYK();
    }

    /**
     * Bitwise Intersection of two colors.
     *
     * Result will be converted bitwise intersection of respective
     * ColorRGBs.
     */
    public static ColorCMYK bitwiseIntersection(ColorCMYK a, ColorCMYK b) {
        return ColorRGB.bitwiseIntersection(a.toRGB(), b.toRGB()).toCMYK();
    }

    /**
     * Bitwise Union of two colors.
     *
     * Result will be converted bitwise Union of respective
     * ColorRGBs.
     */
    public static ColorCMYK bitwiseUnion(ColorCMYK a, ColorCMYK b) {
        return ColorRGB.bitwiseUnion(a.toRGB(), b.toRGB()).toCMYK();
    }

    /**
     * Bitwise XOR of two colors.
     *
     * Result will be converted bitwise xor of respective
     * ColorRGBs.
     */
    public static ColorCMYK bitwiseXOR(ColorCMYK a, ColorCMYK b) {
        return ColorRGB.bitwiseXOR(a.toRGB(), b.toRGB()).toCMYK();
    }

    /**
     * Converts into RGB color system.
     * Uses class Converter.
     *
     * @return ColorRGB - result of converting.
     */
    public ColorRGB toRGB(){
        int[] tmp = Converter.cmykToRgb(cyan, magenta, yellow, black);
        return new ColorRGB(tmp[0], tmp[1], tmp[2]);
    }

    /**
     * Converts into HSL color system.
     * Uses class Converter.
     *
     * @return ColorHSL - result of converting.
     */
    public ColorHSL toHSL(){
        float[] tmp = Converter.cmykToHsl(cyan, magenta, yellow, black);
        return new ColorHSL(tmp[0], tmp[1], tmp[2]);
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

