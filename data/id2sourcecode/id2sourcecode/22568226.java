    public static long getLastModified(URL url) {
        try {
            return url.openConnection().getLastModified();
        } catch (Throwable ex) {
            return 0;
        }
    }
