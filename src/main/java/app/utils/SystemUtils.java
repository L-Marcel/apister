package app.utils;

public class SystemUtils {
    public static boolean isWindows() {
        return System.getProperty("os.name")
            .toLowerCase()
            .contains("win");
    };
};
