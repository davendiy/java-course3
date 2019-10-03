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
public class ColorRGB implements Color {

    private int red;
    private int green;
    private int blue;

    /**
     * Constructor. Just assigns respective variables.
     *
     * @param pR - red value, must be in range [0, 255]
     * @param pG - green value, must be in range [0, 255]
     * @param pB - blue value, must be in range [0, 255]
     * @throws IllegalArgumentException if above conditions are violated
     */
    ColorRGB(int pR, int pG, int pB) throws IllegalArgumentException {
        if (pR < 0 || pR > 255 ||
                pG < 0 || pG > 255 ||
                pB < 0 || pB > 255){
            throw new IllegalArgumentException("Arguments must be in range [0, 255]");
        }
        red = pR;
        green = pG;
        blue = pB;
    }

    /**
     * Implementation of abstract method Color.input().
     * Just inputs necessary params via console.
     */
    @Override
    public void input(){
        Scanner in = new Scanner(System.in);
        System.out.print("Please, enter RGB values in range [0, 255]:\n--> R = ");
        red = in.nextInt();
        System.out.print("--> G = ");
        green = in.nextInt();
        System.out.print("--> B = ");
        blue = in.nextInt();
    }

    /**
     * Implementation of abstract method Color.repr()
     *
     * @return String 'ColorRGB(<red>, <green>, <blue>)'
     */
    @Override
    public String toString(){
        return String.format("ColorRGB[red=%d, green=%d, blue=%d]", red, green, blue);
    }

    /**
     * Sum (mix) of two colors.
     *
     * Result will be ColorRGB with parameters that are arithmetic average
     * of respective parameters of a and b.
     */
    public static ColorRGB sum(ColorRGB a, ColorRGB b){
        int res_r = (a.red + b.red) / 2;
        int res_g = (a.green + b.green) / 2;
        int res_b = (a.blue + b.blue) / 2;
        return new ColorRGB(res_r, res_g, res_b);
    }

    /**
     * Bitwise Intersection of two colors.
     *
     * Result will be ColorRGB with parameters that are bitwise AND
     * of respective parameters of a and b.
     */
    public static ColorRGB bitwiseIntersection(ColorRGB a, ColorRGB b){
        int res_r = a.red | b.red;
        int res_g = a.green | b.green;
        int res_b = a.blue | b.blue;
        return new ColorRGB(res_r, res_g, res_b);
    }

    /**
     * Bitwise Intersection of two colors.
     *
     * Result will be ColorRGB with parameters that are bitwise OR
     * of respective parameters of a and b.
     */
    public static ColorRGB bitwiseUnion(ColorRGB a, ColorRGB b){
        int res_r = a.red & b.red;
        int res_g = a.green & b.green;
        int res_b = a.blue & b.blue;
        return new ColorRGB(res_r, res_g, res_b);
    }

    /**
     * Bitwise Intersection of two colors.
     *
     * Result will be ColorRGB with parameters that are bitwise XOR
     * of respective parameters of a and b.
     */
    public static ColorRGB bitwiseXOR(ColorRGB a, ColorRGB b){
        int res_r = a.red ^ b.red;
        int res_g = a.green ^ b.green;
        int res_b = a.blue ^ b.blue;
        return new ColorRGB(res_r, res_g, res_b);
    }

    /**
     * Converts into HSL color system.
     * Uses class Converter.
     *
     * @return ColorHSL - result of converting.
     */
    public ColorHSL toHSL(){
        float[] tmp = Converter.rgbToHsl(red, green, blue);
        return new ColorHSL(tmp[0], tmp[1], tmp[2]);
    }

    /**
     * Converts into CMYK color system.
     * Uses class Converter.
     *
     * @return ColorCMYK - result of converting.
     */
    public ColorCMYK toCMYK(){
        int[] tmp = Converter.rgbToCmyk(red, green, blue);
        return new ColorCMYK(tmp[0], tmp[1], tmp[2], tmp[3]);
    }

    /**
     * Returns CSS hex representation of RGB color.
     * Example:
     *      ColorRGB(145, 53, 201) -> #9135c9
     *
     * @return String "#<red><greed><blue>", where r,g,b in base 16.
     */
    public String valueHex(){
        String resHex = formatHex(Integer.toHexString(red), 2)
                      + formatHex(Integer.toHexString(green), 2)
                      + formatHex(Integer.toHexString(blue), 2);
        return "#" + formatHex(resHex, 6);
    }

    /**
     * Implementation of abstract method Color.valueInt().
     * Returns dec(valueHex).
     *
     * @return integer number - integer representation of color.
     */
    @Override
    public int valueInt(){
        String resHex = formatHex(Integer.toHexString(red), 2)
                      + formatHex(Integer.toHexString(green), 2)
                      + formatHex(Integer.toHexString(blue), 2);
        return Integer.parseInt(resHex, 16);
    }

    /**
     * Auxiliary method for valueHex.
     * Adding extra zeros while the length doesn't equal to given.
     *
     * @param hex     - initial string
     * @param len     - required length
     * @return        - changed string
     */
    private String formatHex(String hex, int len){
        if (hex.length() < len){
            return formatHex("0" + hex, len);
        } else {
            return hex;
        }

    }

    /**
     * Implementation of abstract method Color.valueFloat.
     *
     * Just returns float version of valueInt()
     */
    @Override
    public float valueFloat(){
        return (float) valueInt();
    }
}
