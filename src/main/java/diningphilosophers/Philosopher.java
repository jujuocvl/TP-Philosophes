
package diningphilosophers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher
        extends Thread {

    private static int seed = 1;
    private final Random myRandom = new Random(System.currentTimeMillis() + seed++);
    private final static int DELAY = 1000;
    private final String myName;
    private final ChopStick myLeftStick;
    private final ChopStick myRightStick;
    private boolean jeContinue = true;

    public Philosopher(String name, ChopStick left, ChopStick right) {
        myName = name;
        myLeftStick = left;
        myRightStick = right;
    }

    @Override
    public void run() {
        while (jeContinue) {
            try {
                think();
                // 2-Step locking protocol
                // 1st step : try to get resources
                if (tryTakeStick(myLeftStick)) {
                    if (tryTakeStick(myRightStick)) {
                        // success : process
                        eat();
                        // release resources
                        releaseStick(myLeftStick);
                        releaseStick(myRightStick);
                    } else {
                        // failure : release resources
                        releaseStick(myLeftStick);
                    }
                }
                // try again
            } catch (InterruptedException ex) {
                Logger.getLogger("Table").log(Level.SEVERE, "{0} Interrupted", this.getName());
            }
        }
        System.out.println("M " + myName + " quitte la table");

    }

    public void leaveTable() {
        jeContinue = false;
    }

    private boolean tryTakeStick(ChopStick stick) throws InterruptedException {
        int delay = myRandom.nextInt(100 + DELAY);
        boolean result = stick.tryTake(delay);
        if (result) {
            System.out.println("M " + myName + " prend la " + stick + " avant " + delay + " ms");
        } else {
            System.out.println("M " + myName + " ne peut pas prendre la " + stick + " avant " + delay + " ms");
        }
        return result;
    }

    private void releaseStick(ChopStick stick) {
        stick.release();
        System.out.println("M " + myName + " relâche la " + stick);
    }

    private void think() {
        int delay = myRandom.nextInt(100 + DELAY);
        System.out.println("M " + myName + " commence à penser " + delay + " ms");
        try {
            sleep(delay);
        } catch (InterruptedException ex) {
        }
        System.out.println("M " + myName + " arrête de penser");
    }

    private void eat() {
        int delay = myRandom.nextInt(100 + DELAY);
        System.out.println("M " + myName + " commence à manger " + delay + " ms");
        try {
            sleep(delay);
        } catch (InterruptedException ex) {
        }
        System.out.println("M " + myName + " arrête de manger");
    }
}