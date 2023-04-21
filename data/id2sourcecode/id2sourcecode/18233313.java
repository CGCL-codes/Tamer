    public static boolean append(String path, String text) {
        return FM.write(path, FM.read(path) + text);
    }
