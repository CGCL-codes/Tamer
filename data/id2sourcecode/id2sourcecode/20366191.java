    public void set(URL url) throws IOException, PlaylistException {
        logger.info("Opening playlist connection to: " + url);
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
        logger.info("  Connecting");
        conn.connect();
        logger.info("  Connected!");
        set(conn.getInputStream());
    }
