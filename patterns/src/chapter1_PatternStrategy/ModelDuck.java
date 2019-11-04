package chapter1_PatternStrategy;

/**
 * created: 04.11.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 */
public class ModelDuck extends Duck {

    public ModelDuck(){
        flyBehavior = new FlyNoWay();
        quackBehaviour = new Quack();
    }

    public void display(){
        System.out.println("lsdjf");
    }
}
