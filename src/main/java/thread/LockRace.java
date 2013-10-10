package thread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.management.Query;

/**
 * java.util.concurrent utilizetion
 *
 * @author Paul Kulitski
 */
public class LockRace {

    public static void main(String[] args) throws Exception {
        // locks();
        futureLocks();
    }
    static Lock lock = new ReentrantLock();
    static int monitor = 1;

    static class TRunnable implements Runnable {

        public void run() {
            System.out.println("IN THREAD " + Thread.currentThread().getName());
            try {
                lock.lock();
                System.out.println("Acquired lock by " + Thread.currentThread().getName());
                monitor++;
                System.out.println("Monitor: " + monitor);
                Callable<String> call = getCall();
                synchronized (calls) {
                    calls.add(call);
                }
                try {
                    //System.out.println("Callable: " + call.call());                    
                } catch (Exception ex) {
                    System.out.println("Exception with callable");
                }
            } finally {
                System.out.println("Unlock lock by " + Thread.currentThread().getName());
                lock.unlock();
            }
        }

        public Callable<String> getCall() {
            return new TCallable();
        }
    }
    private static List<Callable> calls = new ArrayList<Callable>(1024);

    static class TCallable implements Callable<String> {

        public String call() throws Exception {
            System.out.println("Running now..");
            return Thread.currentThread().getName();
        }
    }

    private static void locks() throws Exception {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(1024);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(16, 64, 5, TimeUnit.MINUTES, queue);
        for (int i = 0; i < 10; i++) {
            pool.execute(new TRunnable());
        }
        pool.shutdown();

        pool.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(calls.size());
        for (Callable c : calls) {
            System.out.println("Call it: " + c.call());
        }
    }
    private static Queue<Future> futures = new ConcurrentLinkedQueue<Future>();
    private static ExecutorService futuresExecutor = new ThreadPoolExecutor(16, 64, 5, TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(1024));

    static class T2Runnable implements Runnable {

        public void run() {
            System.out.println("IN THREAD " + Thread.currentThread().getName());
            try {
                lock.lock();
                System.out.println("Acquired lock by " + Thread.currentThread().getName());
                monitor++;
                futures.add(futuresExecutor.submit(new TCallable()));
                System.out.println("Monitor: " + monitor);
            } finally {
                System.out.println("Unlock lock by " + Thread.currentThread().getName());
                lock.unlock();
            }
        }
    }

    private static void futureLocks() throws Exception {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(1024);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(16, 64, 5, TimeUnit.MINUTES, queue);
        for (int i = 0; i < 100; i++) {
            pool.execute(new T2Runnable());
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(futures.size());
        for (Future f : futures) {
            System.out.println("Future call it: " + f.get());
        }
        futuresExecutor.shutdown();
    }
}
