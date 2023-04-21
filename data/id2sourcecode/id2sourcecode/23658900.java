    public static boolean isModified(URL testUrl, long lastModified) {
        if (lastModified == 0) return false;
        URLConnection urlConn = null;
        try {
            urlConn = testUrl.openConnection();
            long lastMod = urlConn.getLastModified();
            urlConn.getInputStream().close();
            return lastMod > lastModified;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                urlConn.getInputStream().close();
            } catch (Throwable t) {
            }
        }
    }
