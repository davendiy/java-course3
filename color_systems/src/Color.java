import java.io.FileWriter;
import java.io.IOException;

/**
 * Interface-predecessor for all the color systems.
 *
 * Any descendant needs to implement next methods:
 *      input();
 *      valueInt();
 *      valueFloat();
 *      repr();
 *
 * created: 02.10.2019
 * @author  David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 */
public interface Color {

    /**
     * Inputs the color system from console.
     */
    void input();

    /**
     * Returns the integer representation of color system.
     */
    int valueInt();

    /**
     * Returns the float representation of color system.
     */
    float valueFloat();

    /**
     * Prints color system to console.
     */
    default void print(){
        System.out.println(toString());
    }

    @Override
    String toString();

    /**
     * Writes array of Color systems into the given file.
     * Uses repr() that must be implemented.
     *
     * @param arrayOfColors    array-like collection of descendants of Color
     * @param outfile          opened file object
     */
    static void writeToFile(Color[] arrayOfColors, FileWriter outfile){
        for (Color tmp: arrayOfColors){
            try {
                outfile.write(tmp.toString());
                outfile.write('\n');
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}
