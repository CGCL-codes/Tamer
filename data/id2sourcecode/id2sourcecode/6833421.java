    public long lastModified() {
        URLConnection con = null;
        try {
            con = url.openConnection();
            return con.getLastModified();
        } catch (IOException e) {
            return 0;
        } finally {
            if (url.getProtocol().equals("file") && con != null) {
                try {
                    con.getInputStream().close();
                } catch (IOException e) {
                    LOG.warn("Error closing URLConnection stream after modcheck.", e);
                }
            }
        }
    }
