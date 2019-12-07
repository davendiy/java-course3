/**
 * created: 05.12.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 */

public class Task18{
    /**
     *
     * Cкласти  програму перетворення рядка А видаленням із нього всіх ком,
     * які передують першій крапці, та заміною у ньому знаком '+' усіх цифр '3',
     * які зустрічаються після першої крапки.
     *
     */

    public static String change(String A){
        int sep = A.indexOf('.');
        String left = A.substring(0, sep);
        String right = A.substring(sep + 1, A.length());
        left = left.replace(",", "");
        right = right.replace('+', '3');
        return left + right;
    }

    public static void main(String[] args){
        String test = "a,k,s,d++ 3j.k,,,,,,33333d3f3j3g3+ =+++++lkdj++fgld.dfklgjldkfgjd.dkfjgldkjg";
            System.out.println(change(test));
        }

}