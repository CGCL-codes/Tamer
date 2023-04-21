    public static int getContentLength(URL url) {
        try {
            return url.openConnection().getContentLength();
        } catch (Throwable ex) {
            return -1;
        }
    }
