package benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class OneToOneModel {

    public static long run(int totalThreads, int iterations, int sleepMillis) {
        List<Thread> threads = new ArrayList<>(totalThreads);
        AtomicLong sink = new AtomicLong(0);

        long t0 = System.nanoTime();
        for (int i = 0; i < totalThreads; i++) {
            Thread t = new Thread(new Workload(iterations, sleepMillis, sink));
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        long t1 = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(t1 - t0);
    }
}
