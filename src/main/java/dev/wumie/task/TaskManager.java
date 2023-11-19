package dev.wumie.task;

import java.util.ArrayList;
import java.util.List;

import static dev.wumie.FireQQ.LOG;

public class TaskManager {
    private static final List<TimeTask> tasks = new ArrayList<>();
    private static long totalTime = 0L;
    private static final List<TimeTask> loadedTasks = new ArrayList<>();

    public static void beginTask(String name) {
        TimeTask task = getTask(name);
        if (task == null || tasks.contains(task)) {
            return;
        }
        tasks.add(task);
        task.begin();
    }

    public static void endTask(String name) {
        TimeTask task = getTask(name);
        if (task == null || !tasks.contains(task)) {
            return;
        }
        tasks.remove(task);
        task.show();
        loadedTasks.add(task);
        totalTime += task.getLoadedTimeTotal();
    }

    public static void showTotal() {
        showTotal(false);
    }

    public static void showTotal(boolean clear) {
        LOG.info("------------------Total------------------(" + loadedTasks.size() + " loaded)");
        List<TimeTask> copied = new ArrayList<>(loadedTasks);
        for (int i = 0;i < copied.size();i++) {
            TimeTask task = copied.get(i);
            LOG.info(i+". "+task.taskName+" - "+task.getLoadedTime());
        }
        LOG.info("------------------Total-------------------(Total: " + (double)totalTime / (double)1000 + "s)");
        if (clear) clearTotal();
    }

    public static void clearTotal() {
        loadedTasks.clear();
        totalTime = 0L;
    }

    public static TimeTask getTask(String taskName) {
        if (tasks.isEmpty()) return null;
        return tasks.stream().filter((t) -> t.taskName.equals(taskName)).findFirst().orElse(null);
    }
}
