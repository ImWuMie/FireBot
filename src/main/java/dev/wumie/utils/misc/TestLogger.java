package dev.wumie.utils.misc;

import java.util.Arrays;

public class TestLogger {
    private final String title;

    public TestLogger(String title) {
        this.title = title;
    }

    public void info(String msg, Object... o) {
        System.out.println("[" + title + ":INFO] " + msg + ((o.length != 0) ? (" " + Arrays.toString(o)) : ""));
    }

    public void error(String msg, Object... o) {
        System.out.println("[" + title + ":ERROR] " + msg + ((o.length != 0) ? (" " + Arrays.toString(o)) : ""));
    }

    public void warning(String msg, Object... o) {
        System.out.println("[" + title + ":WARN] " + msg + ((o.length != 0) ? (" " + Arrays.toString(o)) : ""));
    }

    public void warn(String msg, Object... o) {
        this.warning(msg, o);
    }
}
