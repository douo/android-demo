package info.dourok.demo.algo.thread;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

public class PruduceCosumer {
    private LinkedList<Integer> data = new LinkedList<>();
    private final static int LIMIT = 10;
    private final Object lock = new Object();
    private int value;

    private void produre() throws InterruptedException {
        synchronized (lock) {
            while (data.size() == LIMIT) {
                lock.wait();
            }
            System.out.println("produce:" + value);
            data.offer(value++);
            lock.notify();

        }
    }

    private void consume() throws InterruptedException {
        synchronized (lock) {
            while (data.size() == 0) {
                lock.wait();
            }
            System.out.println("consume:" + data.poll());
            lock.notify();
        }
        CountDownLatch
    }

    public static void main(String[] args) {
        final PruduceCosumer pc = new PruduceCosumer();
        Thread t1 = new Thread(() -> {
            try {
                while (pc.value < 100)
                    pc.produre();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                while (pc.value < 100)
                    pc.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
    }
}
