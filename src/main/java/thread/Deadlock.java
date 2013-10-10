package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Dead Lock Conditions
 *
 * @author Paul Kulitski
 */
public class Deadlock {

    public static void main(String[] args) {
        lockWithCondition();
    }

    static class TRunnnable implements Runnable {

        public void run() {
        }
    }

    static class Before {

        static Lock lock = new ReentrantLock();
        static Condition unpaused = lock.newCondition();
        static boolean isPaused = false;

        static void beforeExecute() {
            try {
                lock.lock();
                while (!isPaused) {
                    unpaused.await();
                }
                System.out.println("WORKING..");
            } catch (InterruptedException ex) {
                System.out.println("Interrupted");
            } finally {
                lock.unlock();
            }
        }

        static void pause() {
            try {
                lock.lock();
                isPaused = true;
            } finally {
                lock.unlock();
            }
        }

        static void unpause() throws InterruptedException {
            try {
                lock.lock();
                isPaused = false;
                unpaused.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    private static void lockWithCondition() {
        new Thread(new TRunnnable(), "T1").start();
        new Thread(new TRunnnable(), "T2").start();
        new Thread(new TRunnnable(), "T3").start();
        System.out.println("Exiting..");
        System.out.println((int) 1.8 / 2);
    }
}
