package dev.wumie.utils;

public class StringUtils {
    public static String getReplaced(String str,Object... args) {
        String s = str;
        for (Object a : args) {
            s = s.replaceFirst("\\{}",a.toString());
        }
        return s;
    }

    public static String replaceAll(String str,String regex,String next) {
        String s = str;
        while (s.contains(regex)) {
            s = s.replaceFirst(regex,next);
        }
        return s;
    }
}
