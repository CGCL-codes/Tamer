    public static void copyAll(URL url, OutputStream out) {
        InputStream in = null;
        try {
            in = url.openStream();
            copyAll(in, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(in);
        }
    }
