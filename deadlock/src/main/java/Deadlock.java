import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Deadlock {

    public static void main(String[] args) {
        Object obj1 = new Object();
        Object obj2 = new Object();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj1) {
                    System.out.println("Thread 1: locked obj1");
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    synchronized (obj2) {
                        System.out.println("Thread 1: locked obj2");
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj2) {
                    System.out.println("Thread 2: locked obj2");
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    synchronized (obj1) {
                        System.out.println("Thread 2: locked obj1");
                    }
                }
            }
        }).start();
    }

}
