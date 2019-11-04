import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PointTest {

    @Test
    public void testAbs() {
        Point test = new Point(new Rational(2, 3),
                new Rational(5, 4),
                new Rational(8, 7));
        double expected = 1.8201832032584073;
        double actual = test.abs();
        assertEquals(actual, expected);
    }


    @Test
    public void testDistance() {
        Point test1 = new Point(new Rational(2, 3),
                new Rational(5, 4),
                new Rational(8, 7));

        Point test2 = new Point(new Rational(2, 7),
                new Rational(37, 45),
                new Rational(48, 16));

        double expected = 2.402615863628272;
        double actual = test1.distance(test2);
        assertEquals(actual, expected);
    }

    @Test
    public void testIs_on_line() {
        Point test1 = new Point(new Rational(2, 3),
                new Rational(5, 4),
                new Rational(8, 7));

        Point test2 = new Point(new Rational(2, 7),
                new Rational(37, 45),
                new Rational(48, 16));

        Point test3 = new Point(new Rational(7, 6),
                new Rational(8, 13),
                new Rational(7, 45));

        boolean actual = Point.is_on_line(test1, test2, test3);
        boolean expected = false;
        assertEquals(actual, expected);
    }

    @Test
    public void testToString(){
        Point test1 = new Point(new Rational(2, 3),
                new Rational(5, 4),
                new Rational(8, 7));

        String actual = test1.toString();
        String expected = "Point(2/3, 5/4, 8/7)";
        assertEquals(actual, expected);
    }
}