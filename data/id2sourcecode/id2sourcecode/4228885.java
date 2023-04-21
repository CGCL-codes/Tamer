    public void set(URL url) throws IOException, PlaylistException {
        logger.fine("Opening playlist connection to: " + url);
        URLConnection conn = url.openConnection();
        logger.fine("  Connecting");
        conn.connect();
        set(conn.getInputStream());
    }
