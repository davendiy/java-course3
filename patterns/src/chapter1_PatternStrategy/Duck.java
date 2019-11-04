package chapter1_PatternStrategy;

/**
 * created: 04.11.2019
 *
 * @author David Zashkolny
 * 3 course, comp math
 * Taras Shevchenko National University of Kyiv
 * email: davendiy@gmail.com
 */
public abstract class Duck {
    FlyBehavior flyBehavior;
    QuackBehaviour quackBehaviour;

    public Duck(){
    }


    public void setFlyBehavior(FlyBehavior fly){
        flyBehavior = fly;
    }

    public void setQuackBehaviour(QuackBehaviour quack){
        quackBehaviour = quack;
    }

    public abstract void display();

    public void performFly(){
        flyBehavior.fly();
    }

    public void performQuack(){
        quackBehaviour.quack();
    }

    public void swim(){
        System.out.println("laksjdla");
    }
}
