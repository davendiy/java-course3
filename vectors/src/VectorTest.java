import org.testng.Assert;

/**
 * created: 31.10.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 *
 *  x             =  (2.0, 3.5, 6.2, 1.8, 7.7)
 *  y             =  (1.0, 2.0, 3.0, 4.0, 5.0)
 *  x + y         =  (3.0, 5.5, 9.2, 5.8, 12.7)
 *  x - y         =  (1.0, 1.5, 3.2, -2.2, 2.7)
 *  13x           =  (26.0, 45.5, 80.6, 23.4, 100.1)
 *  |x|           =  10.826818553942799
 *  |y|           =  7.416198487095663
 *  (x, y)        =  73.30000000000001
 *  angle(x, y)   =  0.4204705143417701
 *  |x - y|       =  5.061620293937506
 *  is_collinear  =  false
 *
 */

public class VectorTest {
    private Vector x, y;

    @org.testng.annotations.BeforeMethod
    public void setUp() {
        double[] xdata = {2.0, 3.5, 6.2, 1.8, 7.7};
        double[] ydata = {1.0, 2.0, 3.0, 4.0, 5.0};

        x = new Vector(xdata);
        y = new Vector(ydata);
    }

    @org.testng.annotations.AfterMethod
    public void tearDown() {
        x = null;
        y = null;
    }

    @org.testng.annotations.Test
    public void testLength() {
        final double expected = 5;
        final double actual = x.length();
        Assert.assertEquals(actual, expected);
    }

    @org.testng.annotations.Test
    public void testDot() {
        final double expected = 73.30000000000001;
        final double actual = x.dot(y);
        Assert.assertEquals(actual, expected);
    }

    @org.testng.annotations.Test
    public void testAbs() {
        final double expected = 10.826818553942799;
        final double actual = x.abs();
        Assert.assertEquals(actual, expected);
    }

    @org.testng.annotations.Test
    public void testAdd() {
        final double[] expected = {3.0, 5.5, 9.2, 5.8, 12.7};
        final double[] actual = x.add(y).testData();
        Assert.assertEquals(actual, expected);
    }

    @org.testng.annotations.Test
    public void testSub() {
        final double[] expected = {1.0, 1.5, 3.2, -2.2, 2.7};
        final double[] actual = x.sub(y).testData();
        Assert.assertEquals(actual, expected);
    }

    @org.testng.annotations.Test
    public void testGet() {
        final double expected = 2.0;
        final double actual = x.get(0);
        Assert.assertEquals(actual, expected);
    }

    @org.testng.annotations.Test
    public void testAngle() {
        final double expected = 0.4204705143417701;
        final double actual = x.angle(y);
        Assert.assertEquals(actual, expected);
    }

    @org.testng.annotations.Test
    public void testScalarMul() {
        final double[] expected = {26.0, 45.5, 80.60000000000001, 23.400000000000002, 100.10000000000001};
        final double[] actual = x.scalarMul(13).testData();
        Assert.assertEquals(actual, expected);
    }

    @org.testng.annotations.Test
    public void testTestToString() {
        final String expected = "(2.0, 3.5, 6.2, 1.8, 7.7)";
        final String actual = x.toString();
        Assert.assertEquals(actual, expected);
    }

}