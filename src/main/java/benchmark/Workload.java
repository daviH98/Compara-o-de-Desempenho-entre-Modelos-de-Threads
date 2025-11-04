package benchmark;

import java.util.concurrent.atomic.AtomicLong;

public class Workload implements Runnable {
    private final int iterations;
    private final int sleepMillis;
    private final AtomicLong sink;

    public Workload(int iterations, int sleepMillis, AtomicLong sink) {
        this.iterations = iterations;
        this.sleepMillis = sleepMillis;
        this.sink = sink;
    }

    @Override
    public void run() {
        long acc = 0;
        for (int i = 0; i < iterations; i++) {
            acc += (i * 31L) ^ (i >>> 1);
            acc ^= (acc << 13);
            acc = Math.abs(acc);
        }
        if (sleepMillis > 0) {
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        sink.addAndGet(acc & 0xFF);
    }
}
