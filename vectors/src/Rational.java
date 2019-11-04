/*
 * created: 31.10.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 *
 *  Immutable realization of Rational numbers.
 *
 *  Invariants
 *  -----------
 *   - gcd(num, den) = 1, i.e, the rational number is in reduced form
 *   - den >= 1, the denominator is always a positive integer
 *   - 0/1 is the unique representation of 0
 *
 ******************************************************************************/

public class Rational{
    private static Rational zero = new Rational(0, 1);

    private int num;   // the numerator
    private int den;   // the denominator

    /**
     * Create and initialize a new Rational object
     * @param numerator     - int number
     * @param denominator   - int non-zero number
     * @throws ArithmeticException  if the denominator is equal to zero
     */
    public Rational(int numerator, int denominator) throws ArithmeticException{

        if (denominator == 0) {
            throw new ArithmeticException("denominator is zero");
        }

        // reduce fraction
        int g = gcd(numerator, denominator);
        num = numerator   / g;
        den = denominator / g;

        // needed only for negative numbers
        if (den < 0) { den = -den; num = -num; }
    }

    // getters
    public int numerator()   { return num; }
    public int denominator() { return den; }

    /**
     * @return  - double precision representation of (this).
     */
    public double toDouble() {
        return (double) num / den;
    }

    /**
     * @return  - string representation of (this)
     */
    public String toString() {
        if (den == 1) return num + "";
        else          return num + "/" + den;
    }

    /**
     * Compares two Rational numbers this and that.
     * @param that  - another rational number.
     * @return      -  1 if this > that;
     *                 0 if this == that;
     *                -1 if this < that
     */
    public int compareTo(Rational that) {
        Rational a = this;
        int lhs = a.num * that.den;
        int rhs = a.den * that.num;
        if (lhs < rhs) return -1;
        if (lhs > rhs) return +1;
        return 0;
    }

    /**
     * Check if this Rational object is equal to that.
     * @param that  - another rational number.
     * @return      - this == that and that is Rational
     */
    public boolean equals(Object that) {
        if (that == null) return false;
        if (that.getClass() != this.getClass()) return false;
        Rational b = (Rational) that;
        return compareTo(b) == 0;
    }

    /**
     * Auxiliary method - greatest common divisor of two numbers.
     * @param m  - some int number
     * @param n  - some int number
     * @return   - gcd(|m|, |n|) (c є I: c|m, c|n, for all d є I d|m and d\n => d|c)
     */
    private static int gcd(int m, int n) {
        if (m < 0) m = -m;
        if (n < 0) n = -n;
        if (0 == n) return m;
        else return gcd(n, m % n);
    }

    /**
     * Auxiliary method - least common multiple.
     * @param m  - some int number
     * @param n  - some int number
     * @return   - lcm(|m|, |n|) (c є I: m|c, n|c, for all d є I m|d and n|d => c <= d)
     */
    private static int lcm(int m, int n) {
        if (m < 0) m = -m;
        if (n < 0) n = -n;
        return m * (n / gcd(m, n));    // parentheses important to avoid overflow
    }

    /**
     * Multiplication of two rational numbers.
     * Stave off overflow as much as possible by cross-cancellation.
     * @param b  - another Rational number
     * @return   - this * b
     */
    public Rational mul(Rational b) {
        Rational a = this;

        // reduce p1/q2 and p2/q1, then multiply, where a = p1/q1 and b = p2/q2
        // in order to prevent overflow in multiplying
        Rational c = new Rational(a.num, b.den);
        Rational d = new Rational(b.num, a.den);
        return new Rational(c.num * d.num, c.den * d.den);
    }

    /**
     * Addition of two rational numbers.
     * Stave off overflow as much as possible.
     * @param b  - another Rational number.
     * @return   - this + b
     */
    public Rational add(Rational b) {
        Rational a = this;

        // special cases
        if (a.compareTo(zero) == 0) return b;
        if (b.compareTo(zero) == 0) return a;

        // Find gcd of numerators and denominators
        int f = gcd(a.num, b.num);
        int g = gcd(a.den, b.den);

        // add cross-product terms for numerator
        Rational s = new Rational((a.num / f) * (b.den / g) + (b.num / f) * (a.den / g),
                lcm(a.den, b.den));

        // multiply back in
        s.num *= f;
        return s;
    }

    /**
     * @return  - -(this)
     */
    public Rational negative() {
        return new Rational(-num, den);
    }

    /**
     * @return - absolute value of (this)
     */
    public Rational abs() {
        if (num >= 0) return this;
        else return negative();
    }

    /**
     * Subtraction of two Rational numbers.
     * @param b  - another Rational number.
     * @return   - this - b
     */
    public Rational sub(Rational b) {
        Rational a = this;
        return a.add(b.negative());
    }

    /**
     * @return - (1 / this)
     */
    public Rational inverse() { return new Rational(den, num);  }

    /**
     * Dividing of two Rational numbers.
     * @param b - another Rational number.
     * @return
     */
    public Rational div(Rational b) {
        Rational a = this;
        return a.mul(b.inverse());
    }


    // test client
    public static void main(String[] args) {
        Rational x, y, z;

        // 1/2 + 1/3 = 5/6
        x = new Rational(1, 2);
        y = new Rational(1, 3);
        z = x.add(y);
        System.out.println(z);

        // 8/9 + 1/9 = 1
        x = new Rational(8, 9);
        y = new Rational(1, 9);
        z = x.add(y);
        System.out.println(z);

        // 1/200000000 + 1/300000000 = 1/120000000
        x = new Rational(1, 200000000);
        y = new Rational(1, 300000000);
        z = x.add(y);
        System.out.println(z);

        // 1073741789/20 + 1073741789/30 = 1073741789/12
        x = new Rational(1073741789, 20);
        y = new Rational(1073741789, 30);
        z = x.add(y);
        System.out.println(z);

        //  4/17 * 17/4 = 1
        x = new Rational(4, 17);
        y = new Rational(17, 4);
        z = x.mul(y);
        System.out.println(z);

        // 3037141/3247033 * 3037547/3246599 = 841/961
        x = new Rational(3037141, 3247033);
        y = new Rational(3037547, 3246599);
        z = x.mul(y);
        System.out.println(z);

        // 1/6 - -4/-8 = -1/3
        x = new Rational( 1,  6);
        y = new Rational(-4, -8);
        z = x.sub(y);
        System.out.println(z);
    }

}