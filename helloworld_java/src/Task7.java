
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * created: 05.12.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 */

public class Task7 {

    /**
     * Написати програму, яка виконує зсув по ключі (ключ задається)
     * тільки для малих латинських та українських літер.
     * Наприклад: вхідні дані  anz – рядок, 2 – ключ. Результат: cpb.
     */


    public static String getShiftString(String item, int key) throws UnsupportedEncodingException {
//        char[] charItems = item.toCharArray();
        byte[] bytes = item.getBytes("ascii");
        for (int i = 0; i < bytes.length; ++i){
            bytes[i] += key;
        }

        return new String(bytes);

    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(getShiftString("abcdef", 4));
    }

}
