
import java.io.*;
import java.util.ArrayList;

/**
 * created: 05.12.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 */

class Set implements Serializable{

    private static final String FILE_NAME = "set.ser";
    private ArrayList<Character> _items;


    public Set(){

        this._items = new ArrayList<Character>();

    }

    public Set(ArrayList<Character> a){
        this._items = new ArrayList<Character>();
        for (int i = 0; i < a.size(); i++){
            this.put(a.get(i));
        }

    }


    public boolean contain(char item){
        boolean success = false;
        for (int i = 0; i < this._items.size(); ++i){
            if (this._items.get(i) == item){
                success = true;
                break;
            }
        }
        return success;
    }



    public void put(char item){
        if (!this.contain(item)){
            this._items.add(item);
        }
    }



    public void pop(char item) throws SetExp {
        boolean success = false;
        for (int i = 0; i < this._items.size(); ++i){
            if (this._items.get(i) == item){
                this._items.remove(i);
                success = true;
                break;
            }
        }

        if (!success){
            throw new SetExp("no such element in set!");
        }


    }


    public String toString(){
        String res = "<Set>:";
        for (int i = 0; i < this._items.size(); ++i){
            res += " " + this._items.get(i);
        }
        return res;
    }




    public static Set intersection(Set a, Set b){
        Set res = new Set();
        for (int i = 0; i < a._items.size(); ++i){
            if (b.contain(a._items.get(i))){
                res.put(a._items.get(i));
            }
        }
        return res;
    }



    public static Set union(Set a, Set b){
        Set res = new Set();
        for (int i = 0; i < a._items.size(); ++i){
            res.put(a._items.get(i));
        }


        for (int i = 0; i < b._items.size(); ++i){
            res.put(b._items.get(i));
        }
        return res;
    }



    public static Set subtract(Set a, Set b){
        Set res = new Set();

        for (int i = 0; i < a._items.size(); ++i){
            if (!b.contain(a._items.get(i))){
                res.put(a._items.get(i));
            }
        }
        return res;
    }


    public ArrayList<Character> indexing(){
        return this._items;
    }

    public static Set add(Set a, Set b){
        Set res = new Set();
        for (int i = 0; i < a._items.size(); ++i){
            res.put(a._items.get(i));
        }
        for (int i = 0; i < b._items.size(); ++i){
            res.put(b._items.get(i));
        }
        return res;
    }

    public void assign(Set a){
        this._items = new ArrayList<Character>();
        for (int i = 0; i < a._items.size(); ++i){
            this.put(a._items.get(i));
        }
    }



    public static Set symm_sub(Set a, Set b){

        return Set.subtract(Set.union(a, b), Set.intersection(a, b));
    }

    public static Set deserialize() {
        Set set = null;
        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            set = (Set) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return set;
    }

    public static void serialize(Set set) {
        try (FileOutputStream fs = new FileOutputStream(FILE_NAME);
             ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(set);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }




}


class SetExp extends Exception{

    SetExp(String message){
        super(message);
    }
}


public class Task3{


    public static void main(String[] args) throws SetExp {
        ArrayList<Character> a = new ArrayList<Character>(){{add('a'); add('d'); add('f'); add('g');}};
        Set Mm = new Set(a);

        Set Nn = new Set();
        Nn.put('a');
        Nn.put('b');
        Nn.put('c');
        Nn.put('d');

        Set.serialize(Mm);

        Set M = Set.deserialize();

        Set.serialize(Nn);

        Set N = Set.deserialize();

        System.out.println("M " + Mm);
        System.out.println("N " + N);
        System.out.println(N.indexing());
        System.out.println(Set.add(M, N));
        System.out.println(Set.subtract(M, N));
        System.out.println(Set.intersection(M, N));
        System.out.println(Set.union(M, N));
        System.out.println(Set.symm_sub(M, N));
        N.pop('1');
    }
}

