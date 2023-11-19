package dev.wumie.task;

import dev.wumie.FireQQ;

public class TimeTask {
    public long startTime, endTime;
    private boolean ended;
    public String taskName;

    public TimeTask(String taskName) {
        this.taskName = taskName;
    }

    public void begin() {
        startTime = System.currentTimeMillis();
    }

    public void end() {
        endTime = System.currentTimeMillis();
        ended = true;
    }

    public void show() {
        if (!ended) {
            end();
        }

        FireQQ.LOG.info("Task '" + taskName + "' loaded in " + getLoadedTime() + ".");
    }

    public double getLoadedTimeTotal() {
        long offset = endTime - startTime;
        return ((double) offset / (double) 1000);
    }

    public String getLoadedTime() {
        long offset = endTime - startTime;
        return (float) ((double) offset / (double) 1000) + "s";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeTask timeTask = (TimeTask) o;

        return taskName.equals(timeTask.taskName);
    }

    @Override
    public int hashCode() {
        return taskName.hashCode();
    }
}
