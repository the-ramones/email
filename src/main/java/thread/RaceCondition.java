package thread;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Race conditions resource: A Thread1 read A A++ | Thread2 read A A++ | result:
 * A was incremented twice
 *
 * @author Paul Kulitski
 */
public class RaceCondition {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Race");
        race();
        System.out.println("Busy waiting");
        busyWaiting();
        System.out.println("Notify waiting");
        notifyWaiting();
    }
    public static int a = 1;

    static class TRunnable implements Runnable {

        public void run() {
            a++;
            System.out.println("IN THREAD " + Thread.currentThread().getName() + ", a: " + a);
        }
    }

    private static void race() throws InterruptedException {
        Thread t1 = new Thread(new TRunnable(), "T1");
        Thread t2 = new Thread(new TRunnable(), "T2");
        t1.start();
        System.out.println("a: " + a);
        t2.start();
        System.out.println("a: " + a);
        t2.join();
        System.out.println("a: " + a);
    }
    public static int monitor = 1;
    public static boolean hasData = true;

    static class MMonitor {

        private int monitor = 1;

        public void incMonitor() {
            monitor++;
            hasData = false;
        }

        public int getMonitor() {
            hasData = true;
            return monitor;
        }
    }

    static class TSignal {

        public boolean hasData = false;

        public synchronized boolean hasData() {
            return hasData;
        }

        public synchronized void setHasData(boolean h) {
            hasData = h;
        }
    }

    static class T2Runnable implements Runnable {

        static TSignal signal = new TSignal();

        public void run() {
            System.out.println("IN THREAD " + Thread.currentThread().getName());
            while (signal.hasData) {
                // busy waiting
            }
            signal.setHasData(true);
            incMonitor();
            System.out.println("monitor: " + monitor);
            signal.setHasData(false);
        }

        public void incMonitor() {
            monitor++;
        }
    }

    private static void busyWaiting() throws InterruptedException {
        MMonitor monitor = new MMonitor();
        TSignal signal = new TSignal();
        Thread t1 = new Thread(new T2Runnable(), "T1");
        Thread t2 = new Thread(new T2Runnable(), "T2");
        t1.start();
        t2.start();
        t2.join();
        System.out.println("monitor: " + RaceCondition.monitor);
    }

    static class T3Runnable implements Runnable {

        static TSignal signal = new TSignal();
        static MMonitor monitor = new MMonitor();
        static boolean wasSignalled = true;

        public void run() {
            System.out.println("IN THREAD " + Thread.currentThread().getName());
            try {
                doWait();
                doNotify();
            } catch (InterruptedException ex) {
                System.out.println("Interrupted thread");
            }
        }

        /*
         * 
         */
        void doWait() throws InterruptedException {
            synchronized (monitor) {
                while (!wasSignalled) {
                    monitor.wait();
                }
                monitor.incMonitor();
                System.out.println("monitor: " + monitor.getMonitor());
                wasSignalled = false;
            }
        }

        /*
         * Notifies waiting objects, set was signalled to prevent spurious 
         * wakeups
         */
        void doNotify() {
            synchronized (monitor) {
                wasSignalled = true;
                monitor.notify();
            }
        }
    }

    private static void notifyWaiting() throws InterruptedException {
        Thread t1 = new Thread(new T3Runnable(), "T1");
        Thread t2 = new Thread(new T3Runnable(), "T2");
        Thread t3 = new Thread(new T3Runnable(), "T3");
        t3.setPriority(7);
        t1.start();
        t2.start();
        t3.start();
        t2.join();
        System.out.println("monitor:" + T3Runnable.monitor.getMonitor());
    }
}
