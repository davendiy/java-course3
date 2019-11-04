import java.util.ArrayList;

/**
 * created: 31.10.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com

 *****************************************************************************
 *  Implementation of a vector of real numbers.
 *
 ******************************************************************************/

public class Vector {

    private final int len;       // length of the vector
    private double[] data;       // array of vector's components

    /**
     * Creates the vector of zeros.
     * @param n - length.
     */
    public Vector(int n) {
        this.len = n;
        this.data = new double[n];
    }

    /**
     * Creates a new vector from an array.
     * @param arr - obvious.
     */
    public Vector(double[] arr) {
        len = arr.length;

        this.data = new double[len];
        System.arraycopy(arr, 0, this.data, 0, len);
    }

    // just for testing
    double[] testData(){ return data; }

    /**
     * Getter for len parameter.
     * @return - length of the vector
     */
    public int length() {
        return len;
    }

    /**
     * Getter for elements of vector.
     * @param i - index of element.
     * @return  - v[i]
     */
    public double get(int i) {
        return data[i];
    }

    /**
     * Setter for elements of vector.
     * @param i     - index of element.
     * @param value - new value of v[i].
     */
    public void set(int i, double value) {
        data[i] = value;
    }

    /**
     * Inner product of two vectors: this and that.
     * @param that - another Vector
     * @return     - real number - euclidean scalar product.
     * @throws IllegalArgumentException if this and that have different lengths.
     */
    public double dot(Vector that) throws IllegalArgumentException {
        if (this.length() != that.length())
            throw new IllegalArgumentException("Vectors must have the same dimensions.");
        double res = 0.0;
        for (int i = 0; i < len; i++)
            res = res + (this.data[i] * that.data[i]);
        return res;
    }

    /**
     * @return - Euclidean absolute value of the Vector.
     */
    public double abs() {
        return Math.sqrt(this.dot(this));
    }

    /**
     * Sum of two vectors: this and that.
     * @param that - another Vector.
     * @return     - new Vector = this + that
     * @throws IllegalArgumentException if vectors have different coordinates.
     */
    public Vector add(Vector that) throws IllegalArgumentException {
        if (this.length() != that.length())
            throw new IllegalArgumentException("Vectors must have the same dimensions.");

        Vector c = new Vector(len);
        for (int i = 0; i < len; i++)
            c.data[i] = this.data[i] + that.data[i];
        return c;
    }

    /**
     * Multiplies this vector at scalar constant.
     * @param k - some scalar
     * @return  - new Vector multiplied by k
     */
    public Vector scalarMul(double k) {
        Vector c = new Vector(len);
        for (int i = 0; i < len; i++)
            c.data[i] = k * data[i];
        return c;
    }

    /**
     * Subtraction of two vectors: this and that.
     * @param that - another Vector.
     * @return     - new Vector = this - that
     * @throws IllegalArgumentException if vectors have different coordinates.
     */
    public Vector sub(Vector that) throws IllegalArgumentException {
        return this.add(that.scalarMul(-1));
    }

    /**
     * Finds the angle between two vectors.
     * @param that - another vector
     * @return     - real number from [-pi, pi]
     * @throws IllegalArgumentException - if vectors have different lengths
     * or any of them is equal to zero
     */
    public double angle(Vector that) throws IllegalArgumentException {
        if (this.length() != that.length())
            throw new IllegalArgumentException("Vectors must have the same dimensions.");
        if (this.length() == 0){
            throw new IllegalArgumentException("Can't find the angle between zero vector and another.");
        }

        double mod1 = this.abs();
        double mod2 = that.abs();
        return Math.acos(this.dot(that) / (mod1 * mod2));
    }

    /**
     * Checks if two vectors are collinear. It means that cos of angle between those
     * vectors is equal to 1 or -1.
     * @param that - another vector.
     * @param eps  - accuracy
     * @return     - true if collinear, else vise versa.
     * @throws IllegalArgumentException - if vectors have different lengths
     * or any of them is equal to zero or the accuracy is non-positive.
     */
    public boolean is_collinear(Vector that, double eps) throws IllegalArgumentException{
        if (eps <= 0){
            throw new IllegalArgumentException("Accuracy must be positive.");
        }

        double tmp = Math.cos(this.angle(that));
        return (Math.abs(tmp - 1) < eps) || (Math.abs(tmp + 1) < eps);
    }


    /**
     * Cover for is_collinear(Vector that, double eps) with default epsilon.
     * @param that  - another vector
     * @return      - true if collinear, else vise versa.
     * @throws IllegalArgumentException - if vectors have different lengths
     * or any of them is equal to zero.
     */
    public boolean is_collinear(Vector that) throws IllegalArgumentException{
        return is_collinear(that, 1e-5);
    }

    /**
     * Checks if two vectors are orthogonal. It means that cos of angle between those
     * vectors is equal to 0.
     * @param that - another vector.
     * @param eps  - accuracy
     * @return     - true if collinear, else vise versa.
     * @throws IllegalArgumentException - if vectors have different lengths
     * or any of them is equal to zero or the accuracy is non-positive.
     */
    public boolean is_orthogonal(Vector that, double eps){
        if (eps <= 0){
            throw new IllegalArgumentException("Accuracy must be positive.");
        }

        return Math.abs(Math.cos(this.angle(that))) < eps;
    }

    public boolean is_orthogonal(Vector that){
        return is_orthogonal(that, 1e-5);
    }

    /**
     * Method used by builtin functions of transforming data to String.
     * @return - string representation of Vector
     */
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append('(');
        for (int i = 0; i < len; i++) {
            res.append(data[i]);
            if (i < len - 1) res.append(", ");
        }
        res.append(')');
        return res.toString();
    }

    // main function for final task
    public static void main(String[] args){
        int m = 10;
        int n = 10;
        int mult = 1000;
        ArrayList<Vector> array = new ArrayList<Vector>(m);
        for (int i = 0; i < m; i++){
            Vector tmp = new Vector(n);
            for (int j = 0; j < n; j++){
                tmp.set(j, Math.random() * mult);
            }
            array.add(i, tmp);
        }

        for (int i = 0; i < m; i++){
            for (int j = 0; j < m; j++){
                if (i == j){
                    continue;
                }
                System.out.println("Check collinearity for  " + array.get(i) + " and " + array.get(j) + ":");
                System.out.println(array.get(i).is_collinear(array.get(j)) + "\n");
                System.out.println("Check orthogonality for  " + array.get(i) + " and " + array.get(j) + ":");
                System.out.println(array.get(i).is_orthogonal(array.get(j)) + "\n");

            }
        }
    }
}
