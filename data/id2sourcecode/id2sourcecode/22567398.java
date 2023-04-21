    public static byte[] readURLAsBytes(URL url) {
        try {
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            return readURLConnectionAsBytes(conn);
        } catch (IOException e) {
            return null;
        }
    }
