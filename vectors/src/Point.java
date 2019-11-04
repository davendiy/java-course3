/**
 * created: 31.10.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 *
 * Immutable realization of Point in 3d space with rational coordinates.
 */

public class Point {
    private Rational x;
    private Rational y;
    private Rational z;

    public Point(Rational x, Rational y, Rational z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double abs(){
        Rational tmp = x.mul(x).add(y.mul(y)).add(z.mul(z));
        return Math.sqrt(tmp.toDouble());
    }

    public double distance(Point that){
        Rational tmp1 = this.x.sub(that.x);
        Rational tmp2 = this.y.sub(that.y);
        Rational tmp3 = this.x.sub(that.z);

        Rational tmp = tmp1.mul(tmp1).add(tmp2.mul(tmp2)).add(tmp3.mul(tmp3));
        return Math.sqrt(tmp.toDouble());
    }

    public static boolean is_on_line(Point a, Point b, Point c){
        Vector ab = new Vector(3);
        Rational tmp1 = b.x.sub(a.x);
        Rational tmp2 = b.y.sub(a.y);
        Rational tmp3 = b.z.sub(a.z);
        ab.set(0, tmp1.toDouble());
        ab.set(1, tmp2.toDouble());
        ab.set(2, tmp3.toDouble());

        tmp1 = c.x.sub(a.x);
        tmp2 = c.y.sub(a.y);
        tmp3 = c.z.sub(a.z);

        Vector ac = new Vector(3);
        ac.set(0, tmp1.toDouble());
        ac.set(1, tmp2.toDouble());
        ac.set(2, tmp3.toDouble());

        return ac.is_collinear(ab);
    }

    public String toString(){
        return "Point(" + x + ", " + y + ", " + z + ")";
    }

}
