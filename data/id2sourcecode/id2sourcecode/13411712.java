    public static long getLastModified(URL url) {
        URLConnection conn = null;
        try {
            return (conn = url.openConnection()).getLastModified();
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        } finally {
            if (conn != null) try {
                conn.getInputStream().close();
            } catch (IOException e) {
                ThrowableManagerRegistry.caught(e);
            }
        }
    }
