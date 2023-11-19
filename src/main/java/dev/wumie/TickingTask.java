package dev.wumie;

public class TickingTask extends Thread {
    public final Runnable tickTask;
    private final String threadName;

    public TickingTask(String name, Runnable tickTask) {
        this.tickTask = tickTask;
        this.threadName = name;
    }

    @Override
    @SuppressWarnings("all")
    public void run() {
        Thread.currentThread().setName(threadName);
        while (FireQQ.running) {
            try {
                tickTask.run();
                Thread.sleep(50L);
            } catch (Exception ignored) {
            }
        }
    }
}
