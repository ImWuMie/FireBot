package dev.wumie;

import dev.wumie.language.I18n;
import dev.wumie.language.Language;
import dev.wumie.system.MessageHandler;
import dev.wumie.task.TaskManager;
import dev.wumie.websocket.WebServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashSet;

public class FireQQ {
    public static final String CLIENT_NAME = "FireBot";
    public static final String CLIENT_VERSION = "1.0";
    public static final String OWNER_QQ = "3177784940";

    public static final Logger LOG = LogManager.getLogger("FireQQ");
    public static final AsyncLoopTask async = new AsyncLoopTask();
    public static final DelayLoopTask delayLoopTask = new DelayLoopTask();
    public static final File FOLDER = new File("FireBot");
    public static MessageHandler HANDLER;

    private static I18n i18nLanguage;
    private static final HashSet<Runnable> runnableSet = new HashSet<>();
    public void submit(Runnable task) {
        runnableSet.add(task);
    }

    public static LaunchMode launchMode = LaunchMode.Server;
    public static volatile boolean running;


    public static void main(String[] args) throws URISyntaxException {
        TaskManager.beginTask("Loading fire bot.");
        running = true;
        LOG.info("Starting " + CLIENT_NAME + " " + CLIENT_VERSION + ".");
        if (!FOLDER.exists()) FOLDER.mkdirs();
        LOG.info("Pre loading bot...");
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

        while (running) {
            try {
                synchronized (runnableSet) {
                    if (runnableSet.isEmpty()) {
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
    }
}
