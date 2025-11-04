package benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class NMModel {

    public static long run(int totalTasks, int poolSize, int iterations, int sleepMillis) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        AtomicLong sink = new AtomicLong(0);
        List<Callable<Void>> tasks = new ArrayList<>(totalTasks);

        for (int i = 0; i < totalTasks; i++) {
            tasks.add(() -> {
                new Workload(iterations, sleepMillis, sink).run();
                return null;
            });
        }

        long t0 = System.nanoTime();
        List<Future<Void>> futures = pool.invokeAll(tasks);
        for (Future<Void> f : futures) {
            try {
                f.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        pool.shutdown();
        long t1 = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(t1 - t0);
    }
}
