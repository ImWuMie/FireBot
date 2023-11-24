package dev.wumie;

import com.google.gson.Gson;
import dev.wumie.system.Configs;
import dev.wumie.system.user.UserManager;
import dev.wumie.task.TaskManager;
import dev.wumie.utils.GsonUtils;
import dev.wumie.websocket.BotMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;

public class FireQQ {
    public static final String CLIENT_NAME = "FireBot";
    public static final String CLIENT_VERSION = "1.0";
    public static final String OWNER_QQ = "3177784940";

    public static FireQQ INSTANCE;

    public FireQQ() {
        INSTANCE = this;
    }

    public final Logger LOG = LogManager.getLogger("FireQQ");
    public final AsyncLoopTask async = new AsyncLoopTask();
    public final DelayLoopTask delayLoopTask = new DelayLoopTask();
    public final File FOLDER = new File("FireBot");
    public final File GROUP_FOLDER = new File(FOLDER,"groups");
    public final File CONFIGS_JSON = new File(FOLDER, "configs.json");
    public Configs configs = Configs.newConfig();
    public final Gson gson = GsonUtils.newBuilder().create();

    public final UserManager userManager = new UserManager();
    private final HashSet<Runnable> runnableSet = new HashSet<>();

    public void submit(Runnable task) {
        runnableSet.add(task);
    }

    public static volatile boolean running;

    public static void main(String[] args) {
        new FireQQ().start();
    }

    public void start() {
        TaskManager.beginTask("Loading fire bot.");
        running = true;
        LOG.info("Starting " + CLIENT_NAME + " " + CLIENT_VERSION + ".");
        if (!FOLDER.exists()) FOLDER.mkdirs();
        LOG.info("Pre loading bot...");
        loadConfig();
        BotMain mainServer = new BotMain(4444);
        mainServer.start();
        LOG.info("Post loading bot...");
        LOG.info("FireBot load done.");
        async.start();
        delayLoopTask.start();

        saveConfig();
        while (running) {
            try {
                synchronized (runnableSet) {
                    if (runnableSet.isEmpty()) {
                        Thread.yield();
                        Thread.sleep(1000L);
                        continue;
                    }
                    Runnable runnable = runnableSet.stream().findAny().get();
                    runnableSet.remove(runnable);
                    runnable.run();
                }
            } catch (Exception ignored) {
            }
        }

        stop();
    }

    public void loadConfig() {
        if (!CONFIGS_JSON.exists()) {
            saveConfig();
        }

        String text = null;
        try {
            text = Files.readString(CONFIGS_JSON.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configs l = gson.fromJson(text, Configs.class);
        if (l == null) {
            saveConfig();
        } else {
            configs = l;
        }
        LOG.info("Successful loaded configs.");
    }

    public void saveConfig() {
        try {
            String json = gson.toJson(configs);
            if (!CONFIGS_JSON.exists()) CONFIGS_JSON.createNewFile();

            Files.writeString(CONFIGS_JSON.toPath(), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        loadConfig();
    }

    public void stop() {
        saveConfig();
    }
}
