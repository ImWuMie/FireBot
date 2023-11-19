package dev.wumie;

import com.google.gson.Gson;
import dev.wumie.language.I18n;
import dev.wumie.language.Language;
import dev.wumie.system.Configs;
import dev.wumie.system.MessageHandler;
import dev.wumie.system.event.dick.NiuZiList;
import dev.wumie.task.TaskManager;
import dev.wumie.utils.GsonUtils;
import dev.wumie.websocket.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashSet;

public class FireQQ {
    public static final String CLIENT_NAME = "FireBot";
    public static final String CLIENT_VERSION = "1.0";
    public static final String OWNER_QQ = "3177784940";

    public static final Logger LOG = LogManager.getLogger("FireQQ");
    public static final AsyncLoopTask async = new AsyncLoopTask();
    public static final DelayLoopTask delayLoopTask = new DelayLoopTask();
    public static final File FOLDER = new File("FireBot");
    public static final File CONFIGS_JSON = new File(FOLDER,"configs.json");
    public static Configs configs = Configs.newConfig();
    public static MessageHandler HANDLER;
    public static final Gson gson = GsonUtils.newBuilder().create();

    private static I18n i18nLanguage;
    private static final HashSet<Runnable> runnableSet = new HashSet<>();
    public void submit(Runnable task) {
        runnableSet.add(task);
    }

    public static LaunchMode launchMode = LaunchMode.Server;
    public static volatile boolean running;


    public static void main(String[] args) {
        TaskManager.beginTask("Loading fire bot.");
        running = true;
        LOG.info("Starting " + CLIENT_NAME + " " + CLIENT_VERSION + ".");
        if (!FOLDER.exists()) FOLDER.mkdirs();
        LOG.info("Pre loading bot...");
        loadConfig();
        TaskManager.beginTask("Starting "+launchMode.getTitle());
        i18nLanguage = new I18n(Language.CHINESE,"FireBot",true);
        WebServer mainServer = new WebServer(4444);
        mainServer.start();
        HANDLER = new MessageHandler(mainServer,launchMode);

        TaskManager.beginTask("MessageHandler init");
        HANDLER.load();
        TaskManager.endTask("MessageHandler init");
        //WebServer server = new WebServer(55522);
        //server.start();
        TaskManager.endTask("Starting "+launchMode.getTitle());
        LOG.info("Post loading bot...");
        LOG.info("FireBot load done.");
        TaskManager.endTask("Loading fire bot.");
        TaskManager.showTotal(true);
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

    public static void loadConfig() {
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

    public static void saveConfig() {
        try {
            String json = gson.toJson(configs);
            if (!CONFIGS_JSON.exists()) CONFIGS_JSON.createNewFile();

            Files.writeString(CONFIGS_JSON.toPath(), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        loadConfig();
        HANDLER.reload();
    }

    public static void stop() {
        HANDLER.stop();
        saveConfig();
    }
}
