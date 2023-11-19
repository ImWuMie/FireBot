package dev.wumie;

import java.util.HashSet;

public class DelayLoopTask extends Thread {
    public final HashSet<Runnable> runnableSet = new HashSet<>();

    public void submit(Runnable task) {
        runnableSet.add(task);
    }

    @Override
    @SuppressWarnings("all")
    public void run() {
        while (FireQQ.running) {
            try {
                synchronized (runnableSet) {
                    if (runnableSet.isEmpty()) {
                        Thread.sleep(10000L);
                        continue;
                    }
                    Runnable runnable = runnableSet.stream().findAny().get();
                    runnableSet.remove(runnable);
                    runnable.run();
                }
            } catch (Exception ignored) {
            }
        }
    }
}
