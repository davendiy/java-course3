/**
 * Auxiliary class with functions for converting.
 *
 * created: 02.10.2019
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 */
class Converter {

    /**
     * Converts an HSL color value to RGB. Conversion formula
     * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
     * Assumes h, s, and l are contained in the set [0, 1] and
     * returns r, g, and b in the set [0, 255].
     *
     * @param h       The hue
     * @param s       The saturation
     * @param l       The lightness
     * @return int array, the RGB representation
     */
    static int[] hslToRgb(float h, float s, float l){
        float r, g, b;

        if (s == 0f) {
            r = g = b = l; // achromatic
        } else {
            float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hueToRgb(p, q, h + 1f/3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f/3f);
        }
        return new int[]{to255(r), to255(g), to255(b)};
    }

    /**
     * Helper method for above one.
     * Converts [0, 1] into [0, 255].
     */
    private static int to255(float v) { return (int)Math.min(255,256*v); }

    /** Helper method that converts hue to rgb */
    private static float hueToRgb(float p, float q, float t) {
        if (t < 0f)
            t += 1f;
        if (t > 1f)
            t -= 1f;
        if (t < 1f/6f)
            return p + (q - p) * 6f * t;
        if (t < 1f/2f)
            return q;
        if (t < 2f/3f)
            return p + (q - p) * (2f/3f - t) * 6f;
        return p;
    }

    /**
     * Converts an RGB color value to HSL. Conversion formula
     * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
     * Assumes pR, pG, and bpBare contained in the set [0, 255] and
     * returns h, s, and l in the set [0, 1].
     *
     * @param pR       The red color value
     * @param pG       The green color value
     * @param pB       The blue color value
     * @return float array, the HSL representation
     */
    static float[] rgbToHsl(int pR, int pG, int pB) {
        float r = pR / 255f;
        float g = pG / 255f;
        float b = pB / 255f;

        float max = (r > g && r > b) ? r : Math.max(g, b);
        float min = (r < g && r < b) ? r : Math.min(g, b);

        float h, s, l;
        l = (max + min) / 2.0f;

        if (max == min) {
            h = s = 0.0f;
        } else {
            float d = max - min;
            s = (l > 0.5f) ? d / (2.0f - max - min) : d / (max + min);

            if (r > g && r > b)
                h = (g - b) / d + (g < b ? 6.0f : 0.0f);

            else if (g > b)
                h = (b - r) / d + 2.0f;

            else
                h = (r - g) / d + 4.0f;

            h /= 6.0f;
        }

        return new float[]{h, s, l};
    }

    /**
     *  Converts an RGB color value to CMYK. Conversion formula
     *  adapted from https://en.wikipedia.org/wiki/CMYK_color_model.
     *  Assumes pR, pG, and bpBare contained in the set [0, 255] and
     *  returns cyan, magenta, yellow, black in the set [0, 255].
     *
     * @param pR    The red color value
     * @param pG    The green color value
     * @param pB    The glue color value
     * @return int array, the CMYK representation
     */
    static int[] rgbToCmyk(int pR, int pG, int pB)
    {
        float tmp_pR = (float) pR / 255;
        float tmp_pG = (float) pG / 255;
        float tmp_pB = (float) pB / 255;

        float black = Math.min(Math.min(1 - tmp_pR, 1 - tmp_pG), 1 - tmp_pB);
        float cyan, magenta, yellow;

        if (black!=1) {
            cyan    = (1-tmp_pR-black)/(1-black);
            magenta = (1-tmp_pG-black)/(1-black);
            yellow  = (1-tmp_pB-black)/(1-black);
        } else {
            cyan = 1 - tmp_pR;
            magenta = 1 - tmp_pG;
            yellow = 1 - tmp_pB;
        }
        int res_cyan = (int) (cyan * 255);
        int res_magenta = (int) (magenta * 255);
        int res_yellow = (int) (yellow * 255);
        int res_black = (int) (black * 255);
        return new int[] {res_cyan, res_magenta, res_yellow, res_black};
    }

    /**
     * Converts an RGB color value to CMYK. Conversion formula
     * adapted from https://en.wikipedia.org/wiki/CMYK_color_model.
     * Assumes cyan, magenta, yellow, black contained in the set [0, 255] and
     * returns r, g, and b in the set [0, 255].
     *
     * @param cyan      The cyan color value
     * @param magenta   The magenta color value
     * @param yellow    The yellow color value
     * @param black     The black color value
     * @return int array, the RGB representation
     */

    static int[] cmykToRgb(int cyan, int magenta, int yellow, int black)
    {
        float tmp_cyan = (float) cyan / 255;
        float tmp_magenta = (float) magenta / 255;
        float tmp_yellow = (float) yellow / 255;
        float tmp_black = (float) black / 255;
        float R, G, B;
        if (tmp_black!=1) {
            R = ((1-tmp_cyan) * (1-tmp_black)) / 1;
            G = ((1-tmp_magenta) * (1-tmp_black)) / 1;
            B = ((1 - tmp_yellow) * (1-tmp_black)) / 1;
        } else {
            R = 1 - tmp_cyan;
            G = 1 - tmp_magenta;
            B = 1 - tmp_yellow;
        }

        int res_r = (int) (R * 255);
        int res_g = (int) (G * 255);
        int res_b = (int) (B * 255);

        return new int[] {res_r, res_g, res_b};
    }

    /**
     * Converts a CMYK color value to HSL.
     * Uses cmykToRgb and RgbToHsl.
     * Assumes cyan, magenta, yellow, black contained in the set [0, 255] and
     * returns h, s, l in the set [0, 1]
     *
     * @param cyan      The cyan color value
     * @param magenta   The magenta color value
     * @param yellow    The yellow color value
     * @param black     The black color value
     * @return float array, HSL representation
     */
    static float[] cmykToHsl(int cyan, int magenta, int yellow, int black){
        int[] tmp_res = cmykToRgb(cyan, magenta, yellow, black);
        return rgbToHsl(tmp_res[0], tmp_res[1], tmp_res[2]);
    }

    /**
     * Converts a HSL color value to CMYK.
     * Uses hslToRgb and RgbToCmyk.
     * Assumes h, s, l contained in the set [0, 1] and
     * returns c, m, y, b in the set [0, 255].
     *
     * @param h       The hue
     * @param s       The saturation
     * @param l       The lightness
     * @return int array, CMYK representation.
     */
    static int[] hslToCmyk(float h, float s, float l){
        int[] tmp_res = hslToRgb(h, s, l);
        return rgbToCmyk(tmp_res[0], tmp_res[1], tmp_res[2]);
    }
}
